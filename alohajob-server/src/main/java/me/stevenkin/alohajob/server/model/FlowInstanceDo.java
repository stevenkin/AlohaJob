package me.stevenkin.alohajob.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowInstanceDo {
    private Long id;
    // 任务所属应用的ID，冗余提高查询效率
    private Long appId;

    // flowInstanceId（任务实例表都使用单独的ID作为主键以支持潜在的分表需求）
    private Long fInstanceId;

    private Long flowId;

    // flow 状态（WorkflowInstanceStatus）
    private Integer status;

    private String dag;

    private String result;

    // 实际触发时间
    private Long actualTriggerTime;
    // 结束时间
    private Long finishedTime;

    private Date createTime;
    private Date updateTime;
}
