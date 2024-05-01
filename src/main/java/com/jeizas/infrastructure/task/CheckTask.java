package com.jeizas.infrastructure.task;

import com.alibaba.fastjson2.JSON;
import com.jeizas.biz.service.ChatService;
import com.jeizas.domain.User;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
public class CheckTask {

    private static final long MINUTE = 1000 * 60;
    @Resource
    private ChatService chatService;
    @Value("${timeout.waitKfReply: 3}")
    private Integer waitKfReply;
    @Value("${timeout.waitReply: 5}")
    private Integer waitReply;


    @Scheduled(cron = "0 0/1 * * * *")
    public void scheduledTask() {
        User user = chatService.getUser();
        log.info("任务执行时间：now={}, user={}",LocalDateTime.now(), JSON.toJSONString(user));
        if (user == null || user.getConnectTime() == null) {
            return;
        }
        long connectTime = user.getConnectTime();
        long curTime = System.currentTimeMillis();
        Long kfStartTime = user.getChartStartTime();
        if (kfStartTime == null) {
            if (diffMin(connectTime, curTime) > waitKfReply) {
                log.info("connectTime={}, curTime={}", connectTime, curTime);
                chatService.disconnectMessage("客服超时无响应，已结束会话");
            }
        } else {
            Long updateTime = Optional.ofNullable(user.getChatUpdateTime()).orElse(0L);
            if (diffMin(updateTime, curTime) > waitReply) {
                log.info("updateTime={}, curTime={}", updateTime, curTime);
                chatService.disconnectMessage("长时间无响应，已结束会话");
            }
        }
    }

    private int diffMin(Long startTime, Long endTime) {
        if (startTime == null || endTime == null) {
            return -1;
        }
        return (int) ((endTime - startTime) / MINUTE);
    }
}
