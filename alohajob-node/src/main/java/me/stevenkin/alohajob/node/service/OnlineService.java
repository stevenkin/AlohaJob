package me.stevenkin.alohajob.node.service;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.request.AppLoginReq;
import me.stevenkin.alohajob.common.request.AppLogoutReq;
import me.stevenkin.alohajob.common.request.WorkerHeartBeatReq;
import me.stevenkin.alohajob.common.response.AppLoginResp;
import me.stevenkin.alohajob.common.response.ElectionResp;
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
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class OnlineService {
    private static final String election_url = "http://%s/worker/election?appId=%d&currentServer=%s&failNum=%d";

    private static final String heartbeat_url = "http://%s/worker/heartbeat";

    private static final String login_url = "http://%s/worker/login";

    private static final String logout_url = "http://%s/worker/logout";
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private AlohaJobNodeProperties properties;
    @Autowired
    private NodeAddress nodeAddress;
    @Setter
    private volatile List<String> serverAddress;

    private ScheduledExecutorService heartBeatTiming = new ScheduledThreadPoolExecutor(1);

    private Long appId;

    private String currentServer;

    private Integer failNum;//同一台server被连续服务发现但心跳失败的次数(可能因为网络分区)

    public void online() throws Exception {
        Holder<Integer> index = new Holder<>(0);
        //1.worker登录，获取appId
        AppLoginResp appLoginResp = Retry.execute(() -> {
            int i = index.getObject();
            String url = String.format(login_url, serverAddress.get(i));
            index.setObject(i + 1 >= serverAddress.size() ? 0 : i + 1);
            return restTemplate
                    .exchange(url, HttpMethod.POST,
                            new HttpEntity<>(new AppLoginReq(properties.getUsername(), properties.getAppName(), properties.getSecret())),
                            new ParameterizedTypeReference<Response<AppLoginResp>>() {
                            })
                    .getBody().getData();
        }, serverAddress.size(), 1000);
        appId = appLoginResp.getAppId();
        //2.服务选举
        currentServer = election(appId, 0, 0);
        //3.开始心跳
        heartBeatTiming.schedule(this::heartBeat, 5, TimeUnit.SECONDS);
    }

    private String election(Long appId, int begin, int failNum) throws Exception {
        Holder<Integer> index = new Holder<>(begin);
        ElectionResp electionResp = Retry.execute(() -> {
            int i = index.getObject();
            i = i >= serverAddress.size() ? 0 : i;
            String url = String.format(election_url, serverAddress.get(i), appId, currentServer, failNum);
            index.setObject(i + 1);
            return restTemplate
                    .exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<Response<ElectionResp>>() {})
                    .getBody().getData();
        }, -1, 1000);
        return electionResp.getCurrentServer();
    }

    private void heartBeat() {
        try {
            Retry.execute(() -> {
                String url = String.format(heartbeat_url, currentServer);
                return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new WorkerHeartBeatReq(nodeAddress.get(), properties.getUsername(), properties.getAppName(), appId, new Date().getTime(), SystemInfoUtils.getSystemMetrics(), properties.getVersion())), new ParameterizedTypeReference<Response<WorkerHeartBeatResp>>() {})
                        .getBody().getData();
            }, 3, 1000);
        } catch (Exception e) {
            log.warn("heart beat fail", e);
            failNum = failNum == null ? 1 : failNum + 1;
            int index = serverAddress.indexOf(currentServer);
            try {
                String server = election(appId, index + 1, failNum);
                if (!server.equals(currentServer))
                    failNum = 0;
                currentServer = server;
            } catch (Exception e1) {

            }
        }
    }

    public void outline() throws Exception {
        String url = String.format(logout_url, currentServer);
        heartBeatTiming.shutdown();
        Retry.execute(() ->
            restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(new AppLogoutReq(appId, nodeAddress.get())), Response.class)
                    .getBody()
        , 3, 1000);
    }
}
