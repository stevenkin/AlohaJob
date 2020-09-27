package me.stevenkin.alohajob.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDo {
    private Long id;
    /**
     * username@appname
     */
    private String appName;

    private String secret;

    private String currentServer;

    private Date createTime;
    private Date updateTime;
}
