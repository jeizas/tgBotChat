package com.jeizas.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Check dto.
 */
@Data
public class CheckDTO implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 用户email邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String captchaCode;
}
