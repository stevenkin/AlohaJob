package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppLogoutReq {
    private Long appId;

    private String workerAddress;//host:port
}
