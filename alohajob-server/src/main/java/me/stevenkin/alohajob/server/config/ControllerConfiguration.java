package me.stevenkin.alohajob.server.config;

import lombok.extern.slf4j.Slf4j;
import me.stevenkin.alohajob.server.advice.GlobalExceptionHandler;
import me.stevenkin.alohajob.server.api.WorkerController;
import me.stevenkin.alohajob.server.cluster.WorkersClusterManager;
import me.stevenkin.alohajob.server.service.AppService;
import me.stevenkin.alohajob.server.service.ServerElectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Slf4j
public class ControllerConfiguration implements WebMvcConfigurer {
    @Autowired
    private AlohaJobServerProperties alohaJobServerProperties;

    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        return new GlobalExceptionHandler();
    }
    @Bean
    public WorkerController workerController(AppService appService, ServerElectionService serverElectionService, WorkersClusterManager workersClusterManager) {
        log.info("worker controller");
        return new WorkerController(appService, serverElectionService, workersClusterManager, alohaJobServerProperties);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer.defaultContentType(MediaType.APPLICATION_JSON, MediaType.TEXT_XML, MediaType.APPLICATION_XML);
    }
}
