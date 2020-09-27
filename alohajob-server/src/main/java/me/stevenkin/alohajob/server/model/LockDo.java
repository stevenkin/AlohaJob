package me.stevenkin.alohajob.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LockDo {
    private Long id;

    private String lockName;
    private String ownerIP;

    private Long maxLockTime;

    private Date createTime;
    private Date updateTime;
}
