package me.stevenkin.alohajob.node.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.response.*;
import me.stevenkin.alohajob.node.AlohaJobNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/node")
@Slf4j
public class NodeController {
    @Autowired
    private AlohaJobNode node;
    @GetMapping(value = "/start")
    public Response start() {
        node.start();
        return Response.success(null);
    }

    @GetMapping(value = "/stop")
    public Response stop() {
        node.stop();
        return Response.success(null);
    }

    @GetMapping("/status")
    public Response<NodeStatus> status() {
        NodeStatus status = node.getStatus();
        if (status == null)
            return Response.failed("node already stop");
        return Response.success(status);
    }
}
