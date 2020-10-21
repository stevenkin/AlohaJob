package me.stevenkin.alohajob.server.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.request.AppLoginReq;
import me.stevenkin.alohajob.common.request.AppLogoutReq;
import me.stevenkin.alohajob.common.request.WorkerHeartBeatReq;
import me.stevenkin.alohajob.common.response.AppLoginResp;
import me.stevenkin.alohajob.common.response.ElectionResp;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.common.response.WorkerHeartBeatResp;
import me.stevenkin.alohajob.server.cluster.WorkersClusterManager;
import me.stevenkin.alohajob.server.config.AlohaJobServerProperties;
import me.stevenkin.alohajob.server.service.AppService;
import me.stevenkin.alohajob.server.service.ServerElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/node")
@Slf4j
public class NodeController {
    @Autowired
    private AppService appService;
    @Autowired
    private ServerElectionService serverElectionService;
    @Autowired
    private WorkersClusterManager workersClusterManager;
    @Autowired
    private AlohaJobServerProperties serverProperties;

    @PostMapping(value = "/login")
    public Response<AppLoginResp> login(@RequestBody AppLoginReq appLoginReq) {
        return Response.success(new AppLoginResp(appService.login(appLoginReq.getUsername(), appLoginReq.getAppName(), appLoginReq.getSecret())));
    }

    @PostMapping("/logout")
    public Response<Void> logout(@RequestBody AppLogoutReq appLogoutReq) {
        appService.logout(appLogoutReq.getAppId(), appLogoutReq.getWorkerAddress());
        return Response.success(null);
    }

    @GetMapping("/election")
    public Response<ElectionResp> election(@RequestParam Long appId, @RequestParam String currentServer, @RequestParam Integer failNum) {
        return Response.success(new ElectionResp(serverElectionService.electServer(appId, currentServer, failNum)));
    }

    @PostMapping("/heartbeat")
    public Response<WorkerHeartBeatResp> heartbeat(@RequestBody WorkerHeartBeatReq workerHeartBeatReq) {
        workersClusterManager.heartbeat(workerHeartBeatReq.getAppId(), workerHeartBeatReq.getWorkerAddress(), workerHeartBeatReq.getSystemMetrics());
        return Response.success(new WorkerHeartBeatResp(new Date().getTime(), serverProperties.getVersion()));
    }
}
