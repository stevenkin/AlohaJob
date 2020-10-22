package me.stevenkin.alohajob.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceDto {
    private Long jobId;

    private String triggerId;

    private String instanceId;

    private String parentInstanceId;

    private String instanceName;

    private String jobParam;

    private String instanceParam;

    private Integer maxRetryTimes;

    private Integer currentRetryTimes;

    private Integer status;

    private Boolean callbackFinish;

    private Boolean ignore;
}
