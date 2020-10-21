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
    private String instanceId;
    // 关联的trigger，代表属于哪次触发
    private String triggerId;
    // 任务实例名
    private String instanceName;
    // 任务实例参数
    private String instanceParams;

    //任务状态
    private Integer status;
    // 执行结果（允许存储稍大的结果）
    private String result;
    // 结束时间
    private Long finishedTime;
    // 总共执行的次数（用于重试判断）
    private Long runningTimes;

    private Date createTime;
    private Date updateTime;
}
