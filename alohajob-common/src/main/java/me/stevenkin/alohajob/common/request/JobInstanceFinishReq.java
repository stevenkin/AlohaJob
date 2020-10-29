package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobInstanceFinishReq {
    private String instanceId;

    private Integer processResultType;

    private String msg;
}
