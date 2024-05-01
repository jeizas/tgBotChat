package com.jeizas.infrastructure.config;

import com.alibaba.fastjson.JSON;
import com.jeizas.biz.dto.Response;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@Slf4j
@ControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * Exception handler string.
     *
     * @param e exception
     * @return the string
     */
    @ExceptionHandler(value = RuntimeException.class)
    @ResponseBody
    public String exceptionHandler(HttpServletResponse response, Exception e) {
        log.error("全局异常捕获>>>:", e);
        response.setContentType("application/json");
        return JSON.toJSONString(Response.error("系统异常，请稍后再试"));
    }
}
