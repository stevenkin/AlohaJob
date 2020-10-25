package me.stevenkin.alohajob.node.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.node.AlohaJobNode;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.utils.DtoUtils;
import me.stevenkin.alohajob.sdk.Promise;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/callback")
@Slf4j
public class CallbackController {
    @Autowired
    private AlohaJobNode node;
    @Autowired
    private SchedulerServerClient client;

    @GetMapping(value = "/complete")
    public Response complete(@RequestParam String parentInstanceId, @RequestParam String subInstanceId) {
        Promise<ProcessResult> future = node.getTaskExecutor().getFuture(parentInstanceId, subInstanceId);
        if (future == null)
            return Response.failed("not found future");
        ProcessResult result = DtoUtils.toProcessResult(client.getJobInstanceResult(subInstanceId));
        if (result == null)
            return Response.failed("not found process result");
        future.complete(result);
        return Response.success(null);
    }

    @GetMapping(value = "/callbackComplete")
    public Response callbackComplete(@RequestParam String parentInstanceId, @RequestParam String subInstanceId) {
        Promise<ProcessResult> future = node.getTaskExecutor().getFuture(parentInstanceId, subInstanceId);
        if (future == null)
            return Response.failed("not found future");
        future.callbackComplete();
        return Response.success(null);
    }

    @GetMapping(value = "/cancel")
    public Response cancel(@RequestParam String parentInstanceId, @RequestParam String subInstanceId) {
        Promise<ProcessResult> future = node.getTaskExecutor().getFuture(parentInstanceId, subInstanceId);
        if (future == null)
            return Response.failed("not found future");
        future.cancel();
        return Response.success(null);
    }

}
