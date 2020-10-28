package me.stevenkin.alohajob.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetServerResp {
    private String currentServer;
}
