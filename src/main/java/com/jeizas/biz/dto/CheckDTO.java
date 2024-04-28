package com.jeizas.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Check dto.
 */
@Data
public class CheckDTO implements Serializable {

    private String userId;

    private String email;

    private String captchaCode;
}
