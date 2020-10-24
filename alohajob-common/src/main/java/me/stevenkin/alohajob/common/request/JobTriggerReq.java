package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobTriggerReq {
    private Long appId;

    private Long jobId;

    private String triggerId;
}
