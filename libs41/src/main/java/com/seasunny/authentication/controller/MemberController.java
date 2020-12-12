package com.seasunny.authentication.controller;

import com.alibaba.fastjson.JSON;
import com.seasunny.authentication.dto.LoginMemberDto;
import com.seasunny.authentication.dto.MemberDto;
import com.seasunny.authentication.dto.ResponseDto;
import com.seasunny.authentication.dto.SmsDto;
import com.seasunny.authentication.enums.SmsUseEnum;
import com.seasunny.authentication.exception.BusinessException;
import com.seasunny.authentication.service.MemberService;
import com.seasunny.authentication.service.SmsService;
import com.seasunny.authentication.util.UuidUtil;
import com.seasunny.authentication.util.ValidatorUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;


/**
 *
 * 功能
 *
 * 检测手机号码是否存在
 *
 * 用户注册
 * 用户登录
 * 用户登出
 *
 *
 * 修改密码信息
 *
 *
 */
@RestController("webMemberController")
@RequestMapping("/web/member")
public class MemberController {

    private static final Logger LOG = LoggerFactory.getLogger(MemberController.class);
    public static final String BUSINESS_NAME = "会员";

    @Resource
    private MemberService memberService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Resource
    private SmsService smsService;

    /**
     * 保存，id有值时更新，无值时新增
     *
     * 网站用户注册
     *
     * url: /web/member/register
     *
     * 输入参数：手机号码，密码，昵称，头像的url，短信验证码 (memberDto)
     * 手机号码：mobile
     * 密码：password
     * 昵称：name
     * 头像url: photo
     * 短信验证码：smsCode
     *
     * 输入校验：
     * 不为空：手机号码，密码
     * 长度限制：手机号码，昵称，头像url
     *
     * 密码加密：
     * MD5
     *
     * 短信验证码验证
     * 输入：手机号码，验证码，使用类型
     * SmsService: 2次数据库验证码的数据表的操作：查询 -- 更新
     *
     *
     * MemberService
     * 添加用户id：8位短uuid, 从uuid中计算出来
     * 添加注册时间registerTime: now
     * 添加新用户数据到数据库
     *
     *
     * 返回用户
     *
     *
     * 输入：MemberDto
     * 输出：ResponseDto，(封装MemberDto)
     *
     *
     */
    @PostMapping("/register")
    public ResponseDto register(@RequestBody MemberDto memberDto) {
        // 保存校验
        ValidatorUtil.require(memberDto.getMobile(), "手机号");
        ValidatorUtil.length(memberDto.getMobile(), "手机号", 11, 11);
        ValidatorUtil.require(memberDto.getPassword(), "密码");
        ValidatorUtil.length(memberDto.getName(), "昵称", 1, 50);
        ValidatorUtil.length(memberDto.getPhoto(), "头像url", 1, 200);

        // 密码加密
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));

        // 校验短信验证码
        SmsDto smsDto = new SmsDto();
        smsDto.setMobile(memberDto.getMobile());
        smsDto.setCode(memberDto.getSmsCode());
        smsDto.setUse(SmsUseEnum.REGISTER.getCode());
        smsService.validCode(smsDto);
        LOG.info("短信验证码校验通过");

        ResponseDto responseDto = new ResponseDto();
        memberService.save(memberDto);
        responseDto.setContent(memberDto);
        return responseDto;
    }

    /**
     * 登录
     *
     * url: /web/member/login
     *
     * 输入参数 (memberDto)
     * mobile
     * password
     * imageCodeToken
     * imageCode
     *
     *
     * 密码加密
     * MD5
     *
     * 缓存中取验证码
     * Redis缓存
     * imageCodeToken --> imageCode
     * 没有验证码 --> 返回前端
     * 验证码错误 --> 返回前端
     * 验证码正确 --> 移除imageCode缓存
     *
     * memberService
     * 查询数据库
     * 查询手机号码是否有注册过
     * 密码验证
     *
     * 单点登录
     * Redis缓存
     * key: short UUID
     * value: loginMemberDto(json)
     *
     * 返回前端用户
     *
     */
    @PostMapping("/login")
    public ResponseDto login(@RequestBody MemberDto memberDto) {
        LOG.info("用户登录开始");
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));
        ResponseDto responseDto = new ResponseDto();

        // 根据验证码token去获取缓存中的验证码，和用户输入的验证码是否一致
        String imageCode = (String) redisTemplate.opsForValue().get(memberDto.getImageCodeToken());
        LOG.info("从redis中获取到的验证码：{}", imageCode);
        if (StringUtils.isEmpty(imageCode)) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码已过期");
            LOG.info("用户登录失败，验证码已过期");
            return responseDto;
        }
        if (!imageCode.toLowerCase().equals(memberDto.getImageCode().toLowerCase())) {
            responseDto.setSuccess(false);
            responseDto.setMessage("验证码不对");
            LOG.info("用户登录失败，验证码不对");
            return responseDto;
        } else {
            // 验证通过后，移除验证码
            redisTemplate.delete(memberDto.getImageCodeToken());
        }

        LoginMemberDto loginMemberDto = memberService.login(memberDto);
        String token = UuidUtil.getShortUuid();
        loginMemberDto.setToken(token);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginMemberDto), 3600, TimeUnit.SECONDS);
        responseDto.setContent(loginMemberDto);
        return responseDto;
    }

    /**
     * 退出登录
     *
     * url: /web/member/logout/{token}
     *
     * 输入参数token
     * 从redis移除缓存
     *
     */
    @GetMapping("/logout/{token}")
    public ResponseDto logout(@PathVariable String token) {
        ResponseDto responseDto = new ResponseDto();
        redisTemplate.delete(token);
        LOG.info("从redis中删除token:{}", token);
        return responseDto;
    }

    /**
     * 校验手机号是否存在
     * 存在则success=true，不存在则success=false
     *
     * 注册时，前端页面发出AJAX请求
     *
     * url: /web/member/is-mobile-exist/{mobile}
     *
     * memberService
     * 查询数据库
     *
     */
    @GetMapping(value = "/is-mobile-exist/{mobile}")
    public ResponseDto isMobileExist(@PathVariable(value = "mobile") String mobile) throws BusinessException {
        LOG.info("查询手机号是否存在开始");
        ResponseDto responseDto = new ResponseDto();
        MemberDto memberDto = memberService.findByMobile(mobile);
        if (memberDto == null) {
            responseDto.setSuccess(false);
        } else {
            responseDto.setSuccess(true);
        }
        return responseDto;
    }

    @PostMapping("/reset-password")
    public ResponseDto resetPassword(@RequestBody MemberDto memberDto) throws BusinessException {
        LOG.info("会员密码重置开始:");
        memberDto.setPassword(DigestUtils.md5DigestAsHex(memberDto.getPassword().getBytes()));
        ResponseDto<MemberDto> responseDto = new ResponseDto();

        // 校验短信验证码
        SmsDto smsDto = new SmsDto();
        smsDto.setMobile(memberDto.getMobile());
        smsDto.setCode(memberDto.getSmsCode());
        smsDto.setUse(SmsUseEnum.FORGET.getCode());
        smsService.validCode(smsDto);
        LOG.info("短信验证码校验通过");

        // 重置密码
        memberService.resetPassword(memberDto);

        return responseDto;
    }
}
