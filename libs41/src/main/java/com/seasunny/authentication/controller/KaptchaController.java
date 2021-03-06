package com.seasunny.authentication.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * 给前端发送验证码图片
 *
 */
@RestController
@RequestMapping("/web/kaptcha")
public class KaptchaController {

    @Qualifier("getWebKaptcha")
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @Autowired
    public StringRedisTemplate redisTemplate;

    /**
     *
     * 生成验证码
     *
     * redis缓存
     * imageCodeToken --> imageCode
     *
     * 发送验证码到前端
     *
     *
     * @param imageCodeToken 前端生成的imageCodeToken
     *
     * @param request  httpServletRequest (servlet参数)
     *
     * @param httpServletResponse httpServletResponse (servlet参数)
     *
     * @throws Exception
     */
    @GetMapping("/image-code/{imageCodeToken}")
    public void imageCode(@PathVariable(value = "imageCodeToken") String imageCodeToken, HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception{
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            // 生成验证码字符串
            String createText = defaultKaptcha.createText();

            // 将生成的验证码放入会话缓存中，后续验证的时候用到
            // request.getSession().setAttribute(imageCodeToken, createText);
            // 将生成的验证码放入redis缓存中，后续验证的时候用到
            redisTemplate.opsForValue().set(imageCodeToken, createText, 300, TimeUnit.SECONDS);

            // 使用验证码字符串生成验证码图片
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }
}
