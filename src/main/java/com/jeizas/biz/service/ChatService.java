package com.jeizas.biz.service;

import com.jeizas.domain.User;

/**
 * The interface Chat service.
 */
public interface ChatService {

    /**
     * Push message.
     *
     * @param chatId the chat id
     * @param text   the text
     */
    void pushMessage(String chatId, String text);

    /**
     * Disconnect message.
     *
     * @param text the text
     */
    void disconnectMessage(String text);

    /**
     * Add user boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean addUser(User user);

    /**
     * Update user boolean.
     *
     * @param type the is start
     */
    void updateUser(String type);

    /**
     * Gets user.
     *
     * @return the user
     */
    User getUser();

    /**
     * Del user.
     */
    void delUser();

}
