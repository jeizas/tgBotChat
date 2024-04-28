package com.jeizas.biz.service;

/**
 * The interface Tg service.
 */
public interface TgService {

    /**
     * Send text.
     *
     * @param chatId the chat id
     * @param text   the text
     */
    void sendText(String chatId, String text, boolean isSystem);

    /**
     * Send text hello.
     */
    void sendTextHello();

    /**
     * Del message.
     *
     * @param messageId the message id
     * @param chatId    the chat id
     */
    void delMessage(Integer messageId, String chatId);
}
