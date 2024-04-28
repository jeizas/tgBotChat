package com.jeizas.biz.controller;

import com.jeizas.biz.dto.CheckDTO;
import com.jeizas.biz.dto.Response;
import com.jeizas.biz.service.ChatService;
import com.jeizas.domain.User;
import com.wf.captcha.GifCaptcha;
import com.wf.captcha.base.Captcha;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 消息处理
 *
 * @author jeizas
 */
@Slf4j
@RestController
@RequestMapping("/api")
public class MessageController {

    @Resource
    private ChatService chatService;

    /**
     * Captcha.
     *
     * @param request  the request
     * @param response the response
     * @throws Exception the exception
     */
    @GetMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 设置请求头为输出图片类型
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 三个参数分别为宽、高、位数
        GifCaptcha captcha = new GifCaptcha(130, 48);
        // 设置字体
        captcha.setFont(new Font("Verdana", Font.PLAIN, 32));  // 有默认字体，可以不用设置
        // 设置类型，纯数字、纯字母、字母数字混合
        captcha.setCharType(Captcha.TYPE_ONLY_NUMBER);

        // 验证码存入session
        request.getSession().setAttribute("captcha", captcha.text().toLowerCase());

        // 输出图片流
        captcha.out(response.getOutputStream());
    }

    /**
     * Check response.
     *
     * @param req     the req
     * @param request the request
     * @return the response
     */
    @PostMapping("/check")
    public Response<?> check(@RequestBody CheckDTO req, HttpServletRequest request) {
        if (StringUtils.isEmpty(req.getCaptchaCode()) || StringUtils.isEmpty(req.getEmail())
                || StringUtils.isEmpty(req.getUserId())) {
            return Response.error("参数异常");
        }
        String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        if (!Pattern.matches(regex, req.getEmail())) {
            return Response.error("邮箱格式异常");
        }
        String sessionCode = (String) request.getSession().getAttribute("captcha");
        if (!req.getCaptchaCode().equals(sessionCode)) {
            return Response.error("验证码错误");
        }
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        User user = new User();
        user.setUserName(req.getUserId());
        user.setEmail(req.getEmail());
        user.setUuid(uuid);
        if (chatService.addUser(user)) {
            return Response.success(user);
        }
        return Response.error("当前已有用户进线中，请稍后再试");
    }

}