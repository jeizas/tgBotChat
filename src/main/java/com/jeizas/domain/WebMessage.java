package com.jeizas.domain;

import lombok.Data;

/**
 * The type Web message.
 */
@Data
public class WebMessage {

    /**
     * 消息
     */
    private String content;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();
}
