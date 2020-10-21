package me.stevenkin.alohajob.server.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobTriggerDo {
    private Long id;

    // 任务ID
    private Long jobId;
    // 任务所属应用的ID，冗余提高查询效率
    private Long appId;
    // 任务实例ID
    private Long triggerId;

    // 触发时间
    private Long triggerTime;

    private Date createTime;
    private Date updateTime;
}
