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
     * 客服点击是否开始 true已点击开始
     */
    private Boolean isStart;

}
