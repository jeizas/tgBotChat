package com.jeizas.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Chat dto.
 */
@Data
public class ChatDTO implements Serializable {

    private String content;

    private Long time;

    private String userName;
}
