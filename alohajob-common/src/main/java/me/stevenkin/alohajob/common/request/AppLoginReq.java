package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppLoginReq {
    private String username;

    private String appName;

    private String secret;
}
