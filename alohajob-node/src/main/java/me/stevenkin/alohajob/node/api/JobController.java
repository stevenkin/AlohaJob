package me.stevenkin.alohajob.node.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.request.JobTriggerReq;
import me.stevenkin.alohajob.common.response.JobTriggerResp;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.node.AlohaJobNode;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.Future;

@RestController
@RequestMapping("/job")
@Slf4j
public class JobController {
    @Autowired
    private AlohaJobNode node;
    @PostMapping(value = "/trigger")
    public Response trigger(@RequestBody JobTriggerReq jobTriggerReq) {
        node.trigger(jobTriggerReq.getAppId(), jobTriggerReq.getJobId(), jobTriggerReq.getTriggerId());
        return Response.success(null);
    }

    @GetMapping(value = "/cancel")
    public Response cancel(@RequestParam String instanceId) {
        Future<ProcessResult> future = node.getTaskExecutor().getProcessFuture(instanceId);
        if (future == null)
            return Response.failed("not found process future");
        future.cancel(true);
        return Response.success(null);
    }
}
