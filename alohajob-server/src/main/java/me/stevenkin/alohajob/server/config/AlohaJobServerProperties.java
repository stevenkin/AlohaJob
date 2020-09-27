package me.stevenkin.alohajob.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("alohajob.server")
@Data
public class AlohaJobServerProperties {
    /**
     * 调度器线程数
     */
    private int scheduleThreadNum = 5;
    /**
     * scheduler name
     */
    private String scheduler;
    /**
     * lock service name
     */
    private String lockService;
    /**
     * registry service name
     */
    private String registryService;
    /**
     * server版本
     */
    private String version;
    /**
     * read timeout(unit ms)
     */
    private int readTime = 5000;
    /**
     * connect timeout(unit ms)
     */
    private int connectTimeout = 5000;
}
