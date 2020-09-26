package com.course.system.service;

import com.course.common.dto.PageDto;
import com.course.common.util.CopyUtil;
import com.course.common.util.UuidUtil;
import com.course.system.domain.Sms;
import com.course.system.domain.SmsExample;
import com.course.system.dto.SmsDto;
import com.course.system.enums.SmsStatusEnum;
import com.course.system.exception.BusinessException;
import com.course.system.exception.BusinessExceptionCode;
import com.course.system.mapper.SmsMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

@Service
public class SmsService {

    @Resource
    private SmsMapper smsMapper;

    /**
     * list query
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
     * save
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
     * insert
     */
    private void insert(Sms sms) {
        Date now = new Date();
        sms.setId(UuidUtil.getShortUuid());
        smsMapper.insert(sms);
    }

    /**
     * update
     */
    private void update(Sms sms) {
        smsMapper.updateByPrimaryKey(sms);
    }

    /**
     * delete
     */
    public void delete(String id) {
        smsMapper.deleteByPrimaryKey(id);
    }



    public void sendCode(SmsDto smsDto){
        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria = example.createCriteria();

        criteria.andMobileEqualTo(smsDto.getMobile())
                .andUseEqualTo(smsDto.getUse())
                .andStatusEqualTo(SmsStatusEnum.NOT_USED.getCode())
                .andAtGreaterThan(new Date(new Date().getTime() - 1 * 60 * 1000));

        List<Sms> smsList = smsMapper.selectByExample(example);
        if(smsList == null || smsList.size() == 0){
            saveAndSend(smsDto);
        }
        else {
            throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_TOO_FREQUENT);
        }
    }


    private void saveAndSend(SmsDto smsDto){
        String code = String.valueOf((int)(((Math.random() * 9) + 1) * 100000));
        smsDto.setAt(new Date());
        smsDto.setStatus(SmsStatusEnum.NOT_USED.getCode());
        smsDto.setCode(code);
        this.save(smsDto);
    }

    public void validCode(SmsDto smsDto){

        SmsExample example = new SmsExample();
        SmsExample.Criteria criteria = example.createCriteria();
        criteria.andMobileEqualTo(smsDto.getMobile())
                .andUseEqualTo(smsDto.getUse())
                .andAtGreaterThan(new Date(new Date().getTime() - 1 * 60 * 1000));

        List<Sms> smsList = smsMapper.selectByExample(example);
        if(smsList != null && smsList.size() > 0){
            Sms smsDb = smsList.get(0);
            if(!smsDb.getCode().equals(smsDto.getCode())){
                throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_ERROR);
            }
            else {
                smsDb.setStatus(SmsStatusEnum.USED.getCode());
                smsMapper.updateByPrimaryKey(smsDb);
            }
        }
        else {
            throw new BusinessException(BusinessExceptionCode.MOBILE_CODE_EXPIRED);
        }

    }

}
