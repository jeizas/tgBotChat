package com.jeizas;

import com.jeizas.common.constant.StringConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

/**
 * 启动类
 */
@Slf4j
@EnableScheduling
@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class App {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        if (args != null) {
            Arrays.stream(args).filter(i -> i.contains(StringConstant.ARGS_SEPARATOR)).forEach(i -> {
                String[] sp = i.split(StringConstant.ARGS_SEPARATOR);
                System.setProperty(sp[0], sp[1]);
            });
        }
        String botToken = System.getProperty(StringConstant.PRO_KEY_TOKEN);
        String botName = System.getProperty(StringConstant.PRO_KEY_NAME);
        String botChatId = System.getProperty(StringConstant.PRO_KEY_CHAT);
        if (StringUtils.isEmpty(botToken) || StringUtils.isEmpty(botName) || StringUtils.isEmpty(botChatId)) {
            log.error("tg bot token or name is null");
            throw new RuntimeException("请添加启动参数，如：botToken=xxx botName=xxx botChatId=xxx");
        }
        SpringApplication.run(App.class, args);
    }
}
