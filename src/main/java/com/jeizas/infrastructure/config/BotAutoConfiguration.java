package com.jeizas.infrastructure.config;


import com.jeizas.common.constant.StringConstant;
import com.jeizas.infrastructure.listener.TgLongPollingBot;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * The type Bot auto configuration.
 */
@Configuration
public class BotAutoConfiguration {

    /**
     * Create local bot listener tg long polling bot.
     *
     * @return the tg long polling bot
     * @throws TelegramApiException the telegram api exception
     */
    @Bean(value = "TgLongPollingBot")
    @ConditionalOnProperty(name = "env", havingValue = "local")
    public TgLongPollingBot createLocalBotListener() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        DefaultBotOptions botOptions = new DefaultBotOptions();
        botOptions.setProxyHost("127.0.0.1");
        botOptions.setProxyPort(8888);
        botOptions.setProxyType(DefaultBotOptions.ProxyType.HTTP);
        TgLongPollingBot myBot = new TgLongPollingBot(System.getProperty(StringConstant.PRO_KEY_TOKEN),
                System.getProperty(StringConstant.PRO_KEY_NAME));
        telegramBotsApi.registerBot(myBot);
        return myBot;
    }

    /**
     * Create release bot listener tg long polling bot.
     *
     * @return the tg long polling bot
     * @throws TelegramApiException the telegram api exception
     */
    @Bean(value = "TgLongPollingBot")
    @ConditionalOnMissingBean
    public TgLongPollingBot createReleaseBotListener() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        TgLongPollingBot myBot = new TgLongPollingBot(System.getProperty(StringConstant.PRO_KEY_TOKEN),
                System.getProperty(StringConstant.PRO_KEY_NAME));
        telegramBotsApi.registerBot(myBot);
        return myBot;
    }
}
