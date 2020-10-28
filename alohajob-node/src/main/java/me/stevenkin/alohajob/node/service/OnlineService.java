package me.stevenkin.alohajob.node.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.request.AppLoginReq;
import me.stevenkin.alohajob.common.request.AppLogoutReq;
import me.stevenkin.alohajob.common.request.WorkerHeartBeatReq;
import me.stevenkin.alohajob.common.response.AppLoginResp;
import me.stevenkin.alohajob.common.response.GetServerResp;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.common.response.WorkerHeartBeatResp;
import me.stevenkin.alohajob.common.utils.Holder;
import me.stevenkin.alohajob.common.utils.Retry;
import me.stevenkin.alohajob.node.NodeAddress;
import me.stevenkin.alohajob.node.config.AlohaJobNodeProperties;
import me.stevenkin.alohajob.node.utils.SystemInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.*;

@Slf4j
@Service
public class OnlineService {
    private static final String get_server_url = "http://%s/worker/election?appId=%d&currentServer=%s&failNum=%d";

    private static final String heartbeat_url = "http://%s/worker/heartbeat";

    private static final String login_url = "http://%s/worker/login";

    private static final String logout_url = "http://%s/worker/logout";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AlohaJobNodeProperties properties;
    @Autowired
    private NodeAddress nodeAddress;
    @Getter
    private Set<String> serverAddress = new CopyOnWriteArraySet<>();

    private ScheduledExecutorService heartBeatTiming = new ScheduledThreadPoolExecutor(1);

    private Long appId;

    private String currentServer;

    private Integer failNum;//同一台server被连续服务发现但心跳失败的次数(可能因为网络分区)

    public void serServers(List<String> servers) {
        serverAddress.addAll(servers);
    }

    public void online() throws Exception {
        Iterator<String> iterator = serverAddress.iterator();
        //1.worker登录，获取appId
        AppLoginResp appLoginResp = Retry.execute(() -> {
            String url = String.format(login_url, iterator.next());
            return restTemplate
                    .exchange(url, HttpMethod.POST,
                            new HttpEntity<>(new AppLoginReq(properties.getUsername(), properties.getAppName(), properties.getSecret())),
                            new ParameterizedTypeReference<Response<AppLoginResp>>() {
                            })
                    .getBody().getData();
        }, iterator::hasNext, 1000);
        appId = appLoginResp.getAppId();
        //2.服务选举
        currentServer = getServer(appId, 0);
        //3.开始心跳
        heartBeatTiming.schedule(this::heartBeat, 5, TimeUnit.SECONDS);
    }

    private String getServer(Long appId, int failNum) throws Exception {
        Iterator<String> iterator = serverAddress.iterator();
        GetServerResp getServerResp = Retry.execute(() -> {
            String url = String.format(get_server_url, iterator.next(), appId, currentServer, failNum);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<GetServerResp>>() {})
                    .getBody().getData();
        }, iterator::hasNext, 1000);
        return getServerResp.getCurrentServer();
    }

    private void heartBeat() {
        try {
            Holder<Integer> num = new Holder<>();
            num.setObject(3);
            Retry.execute(() -> {
                num.setObject(num.getObject() - 1);
                String url = String.format(heartbeat_url, currentServer);
                return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new WorkerHeartBeatReq(nodeAddress.get(), properties.getUsername(), properties.getAppName(), appId, new Date().getTime(), SystemInfoUtils.getSystemMetrics(), properties.getVersion())), new ParameterizedTypeReference<Response<WorkerHeartBeatResp>>() {})
                        .getBody().getData();
            }, () -> num.getObject() > 0, 1000);
        } catch (Exception e) {
            log.warn("heart beat fail", e);
            failNum = failNum == null ? 1 : failNum + 1;
            try {
                String server = getServer(appId, failNum);
                if (!server.equals(currentServer))
                    failNum = 0;
                currentServer = server;
            } catch (Exception e1) {
                //重新获取服务器失败，这时就说明出大问题了,必须告警
                //TODO 告警
                log.error("re get server fail", e1);
            }
        }
    }

    public void outline() throws Exception {
        String url = String.format(logout_url, currentServer);
        heartBeatTiming.shutdown();

        Holder<Integer> num = new Holder<>();
        num.setObject(3);
        Retry.execute(() -> {
            num.setObject(num.getObject() - 1);
            return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new AppLogoutReq(appId, nodeAddress.get())), Response.class)
                            .getBody();
            }, () -> num.getObject() > 0, 1000);
    }
}
