package me.stevenkin.alohajob.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobInstanceDo {
    private Long id;

    // 任务ID
    private Long jobId;
    // 任务所属应用的ID，冗余提高查询效率
    private Long appId;
    // 任务实例ID
    private Long instanceId;
    // 任务实例参数
    private String instanceParams;

    // 该任务实例的类型，普通/工作流（InstanceType）
    private Integer type;
    // 该任务实例所属的 workflow ID，仅 workflow 任务存在
    private Long wfInstanceId;
    //任务状态
    private Integer status;
    // 执行结果（允许存储稍大的结果）
    private String result;
    // 预计触发时间
    private Long expectedTriggerTime;
    // 实际触发时间
    private Long actualTriggerTime;
    // 结束时间
    private Long finishedTime;
    // 最后上报时间
    private Long lastReportTime;
    // TaskTracker地址
    private String taskTrackerAddress;

    // 总共执行的次数（用于重试判断）
    private Long runningTimes;

    private Date createTime;
    private Date updateTime;
}
