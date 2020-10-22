package me.stevenkin.alohajob.node.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.dto.JobInstanceDto;
import me.stevenkin.alohajob.common.response.Response;
import me.stevenkin.alohajob.node.AlohaJobNode;
import me.stevenkin.alohajob.node.core.SchedulerServerClient;
import me.stevenkin.alohajob.node.utils.DtoUtils;
import me.stevenkin.alohajob.sdk.AlohaFuture;
import me.stevenkin.alohajob.sdk.ProcessResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exec")
@Slf4j
public class ExecController {
    @Autowired
    private AlohaJobNode node;
    @Autowired
    private SchedulerServerClient client;

    @PostMapping(value = "/onComplete")
    public Response complete(@RequestBody JobInstanceDto dto) {
        AlohaFuture<ProcessResult> future = node.getTaskExecutor().getFuture(dto.getParentInstanceId(), dto.getInstanceId());
        if (future == null)
            return Response.failed("not found future");
        ProcessResult result = DtoUtils.toProcessResult(client.getJobInstanceResult(dto.getInstanceId()));
        if (result == null)
            return Response.failed("not found process result");
        future.complete(result);
        return Response.success(null);
    }

    @PostMapping(value = "/onCallbackComplete")
    public Response callbackComplete(@RequestBody JobInstanceDto dto) {
        AlohaFuture<ProcessResult> future = node.getTaskExecutor().getFuture(dto.getParentInstanceId(), dto.getInstanceId());
        if (future == null)
            return Response.failed("not found future");
        future.callbackComplete();
        return Response.success(null);
    }
}
