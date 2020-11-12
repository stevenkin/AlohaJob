package me.stevenkin.alohajob.server.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 调度服务器不提供优雅下线功能，只能手动kill
 */
@RestController
@RequestMapping("/server")
@Slf4j
public class ServerController {
}
