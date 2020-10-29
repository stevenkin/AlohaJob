package me.stevenkin.alohajob.common.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JobInstanceNewReq {
    private String triggerId;

    private String parentInstanceId;

    private String subInstanceName;

    private String instanceParam;
}
