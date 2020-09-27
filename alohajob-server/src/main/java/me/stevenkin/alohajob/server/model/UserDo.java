package me.stevenkin.alohajob.server.model;

import lombok.Data;

import java.util.Date;

@Data
public class UserDo {
    private Long id;

    private String username;
    private String password;

    // 手机号
    private String phone;
    // 邮箱地址
    private String email;

    private Date createTime;
    private Date updateTime;
}
