package me.stevenkin.alohajob.node.config;

import com.google.common.collect.Lists;
import lombok.Data;
import me.stevenkin.alohajob.common.utils.NetUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties("alohajob.node")
public class AlohaJobNodeProperties {
    /**
     * 用户名
     */
    private String username;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * node 版本号
     */
    private String version;
    /**
     * 应用密码
     */
    private String secret;
    /**
     * node host 或 域名
     */
    private String host = NetUtils.getLocalHost();
    /**
     * 调度服务器地址，ip:port 或 域名
     */
    private List<String> serverAddress = Lists.newArrayList();
    /**
     * read timeout(unit ms)
     */
    private int readTime = 5000;
    /**
     * connect timeout(unit ms)
     */
    private int connectTimeout = 5000;
    /*********** worker组件配置 ***********/
    /**
     * 服务发现组件
     */
    private String discoveryService;
}
