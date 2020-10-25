package me.stevenkin.alohajob.node.api;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.common.request.NodeStartReq;
import me.stevenkin.alohajob.common.request.NodeStopReq;
import me.stevenkin.alohajob.common.response.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/node")
@Slf4j
public class NodeController {
    @PostMapping(value = "/start")
    public Response<NodeStartResp> start(@RequestBody NodeStartReq nodeStartReq) {
        return null;
    }

    @PostMapping(value = "/stop")
    public Response<NodeStopResp> stop(@RequestBody NodeStopReq nodeStopReq) {
        return null;
    }

    @GetMapping("/status")
    public Response<NodeStatus> status() {
        return null;
    }
}
