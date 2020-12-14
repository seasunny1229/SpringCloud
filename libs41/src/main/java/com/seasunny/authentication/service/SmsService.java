package com.seasunny.authentication.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seasunny.authentication.domain.Sms;
import com.seasunny.authentication.domain.SmsExample;
import com.seasunny.authentication.dto.PageDto;
import com.seasunny.authentication.dto.SmsDto;
import com.seasunny.authentication.enums.SmsStatusEnum;
import com.seasunny.authentication.exception.BusinessException;
import com.seasunny.authentication.exception.BusinessExceptionCode;
import com.seasunny.authentication.mapper.SmsMapper;
import com.seasunny.authentication.util.CopyUtil;
import com.seasunny.authentication.util.UuidUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class SmsService {

    private static final Logger LOG = LoggerFactory.getLogger(SmsService.class);

    @Resource
    private SmsMapper smsMapper;

    /**
     * 列表查询
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        SmsExample smsExample = new SmsExample();
        List<Sms> smsList = smsMapper.selectByExample(smsExample);
        PageInfo<Sms> pageInfo = new PageInfo<>(smsList);
        pageDto.setTotal(pageInfo.getTotal());
        List<SmsDto> smsDtoList = CopyUtil.copyList(smsList, SmsDto.class);
        pageDto.setList(smsDtoList);
    }

    /**
     * 保存，id有值时更新，无值时新增
     */
    public void save(SmsDto smsDto) {
        Sms sms = CopyUtil.copy(smsDto, Sms.class);
        if (StringUtils.isEmpty(smsDto.getId())) {
            this.insert(sms);
        } else {
            this.update(sms);
        }
    }

    /**
     * 新增
     */
    private void insert(Sms sms) {
        Date now = new Date();
        sms.setId(UuidUtil.getShortUuid());
        smsMapper.insert(sms);
    }

    /**
     * 更新
     */
    private void update(Sms sms) {
        smsMapper.updateByPrimaryKey(sms);
    }

    /**
     * 删除
     */
    public void delete(String id) {
        smsMapper.deleteByPrimaryKey(id);
    }

    /**
     * 发送短信验证码
     * 同手机号同操作1分钟内不能重复发送短信
     *
     * 查询验证码是否存在
     * 条件：手机号码，使用类型，1分钟以内
     *
     * 查询到 --> 抛出异常
     *
     * 未查询到
     * 生成6位验证码
     * 存到数据库
     *
     * 调用第三方短信验证码服务发送给用户
     *
     *
     * @param smsDto
     */
    public void sendCode(SmsDto smsDto) {
        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria = example.createCriteria();
        // 查找1分钟内有没有同手机号同操作发送记录且没被用过
        criteria.andMobileEqualTo(smsDto.getMobile())
                .andUseEqualTo(smsDto.getUse())
                .andStatusEqualTo(SmsStatusEnum.NOT_USED.getCode())
                .andAtGreaterThan(new Date(new Date().getTime() - 1 * 60 * 1000));
        List<Sms> smsList = smsMapper.selectByExample(example);

        if (smsList == null || smsList.size() == 0) {
            saveAndSend(smsDto);
        } else {
            LOG.warn("短信请求过于频繁, {}", smsDto.getMobile());
            throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_TOO_FREQUENT);
        }
    }

    /**
     * 保存并发送短信验证码
     * @param smsDto
     */
    private void saveAndSend(SmsDto smsDto) {
        // 生成6位数字
        String code = String.valueOf((int)(((Math.random() * 9) + 1) * 100000));
        smsDto.setAt(new Date());
        smsDto.setStatus(SmsStatusEnum.NOT_USED.getCode());
        smsDto.setCode(code);
        this.save(smsDto);

        // TODO 调第三方短信接口发送短信
    }

    /**
     * 验证码5分钟内有效，且操作类型要一致
     * @param smsDto
     *
     *
     * 查询数据库
     * 查询条件：手机号码，使用类型，过去5分钟内的
     *
     * 1. 查询到验证码
     *
     * 验证码正确：
     * 设置验证码状态为已经使用过
     * 更新数据库
     *
     *
     * 验证码不正确：抛出异常，返回用户失败信息
     *
     *
     * 2. 没有查询到验证码
     * 抛出异常，返回用户失败信息
     *
     */
    public void validCode(SmsDto smsDto) {
        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria = example.createCriteria();
        // 查找5分钟内同手机号同操作发送记录
        criteria.andMobileEqualTo(smsDto.getMobile()).andUseEqualTo(smsDto.getUse()).andAtGreaterThan(new Date(new Date().getTime() - 1 * 60 * 1000));
        List<Sms> smsList = smsMapper.selectByExample(example);

        if (smsList != null && smsList.size() > 0) {
            Sms smsDb = smsList.get(0);
            if (!smsDb.getCode().equals(smsDto.getCode())) {
                LOG.warn("短信验证码不正确");
                throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_ERROR);
            } else {
                smsDb.setStatus(SmsStatusEnum.USED.getCode());
                smsMapper.updateByPrimaryKey(smsDb);
            }
        } else {
            LOG.warn("短信验证码不存在或已过期，请重新发送短信");
            throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_EXPIRED);
        }
    }
}