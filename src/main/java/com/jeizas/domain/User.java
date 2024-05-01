package com.jeizas.domain;

import lombok.Data;

/**
 * The type User.
 */
@Data
public class User {

    /**
     * 用户id
     */
    private String userName;

    /**
     * 用户email
     */
    private String email;

    /**
     * 请求会话uuid
     */
    private String uuid;

    /**
     * connect time
     */
    private Long connectTime;

    /**
     * 聊天开始时间
     */
    private Long chartStartTime;

    /**
     * 聊天更新时间
     */
    private Long chatUpdateTime;

}
