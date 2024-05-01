package com.jeizas.biz.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Chat dto.
 */
@Data
public class ChatDTO implements Serializable {

    /**
     * 聊天文本
     */
    private String content;

    /**
     * 时间
     */
    private Long time;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 是否需要断开连接
     */
    private boolean disconnect = false;
}
