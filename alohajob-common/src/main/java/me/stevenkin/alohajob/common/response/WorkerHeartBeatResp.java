package me.stevenkin.alohajob.common.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorkerHeartBeatResp {
    // 时间戳
    private long serverTimestamp;
    // server 版本信息
    private String version;
}
