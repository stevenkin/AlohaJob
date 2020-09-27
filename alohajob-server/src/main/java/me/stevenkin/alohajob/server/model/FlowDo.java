package me.stevenkin.alohajob.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class FlowDo {
    private Long id;

    private String fName;
    private String fDescription;

    // 所属应用ID
    private Long appId;

    // 工作流的DAG图信息（点线式DAG的json）
    private String peDAG;

    /* ************************** 定时参数 ************************** */
    // 时间表达式类型（CRON/API/FIX_RATE/FIX_DELAY）
    private Integer timeExpressionType;
    // 时间表达式，CRON/NULL/LONG/LONG
    private String timeExpression;

    // 最大同时运行的工作流个数，默认 1
    private Integer maxFInstanceNum;

    // 1 正常运行，2 停止（不再调度）
    private Integer status;
    // 下一次调度时间
    private Long nextTriggerTime;

    // 工作流整体失败的报警
    private String notifyUserIds;

    private Date createTime;
    private Date updateTime;
}
