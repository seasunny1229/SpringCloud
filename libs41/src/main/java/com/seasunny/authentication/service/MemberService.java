package com.seasunny.authentication.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.seasunny.authentication.domain.Member;
import com.seasunny.authentication.domain.MemberExample;
import com.seasunny.authentication.dto.LoginMemberDto;
import com.seasunny.authentication.dto.MemberDto;
import com.seasunny.authentication.dto.PageDto;
import com.seasunny.authentication.exception.BusinessException;
import com.seasunny.authentication.exception.BusinessExceptionCode;
import com.seasunny.authentication.mapper.MemberMapper;
import com.seasunny.authentication.util.CopyUtil;
import com.seasunny.authentication.util.UuidUtil;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

/**
 *
 * 操作数据库
 *
 * 抛出异常 BusinessException
 *
 */
@Service
public class MemberService {

    @Resource
    private MemberMapper memberMapper;

    /**
     * list query
     */
    public void list(PageDto pageDto) {
        PageHelper.startPage(pageDto.getPage(), pageDto.getSize());
        MemberExample memberExample = new MemberExample();
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        PageInfo<Member> pageInfo = new PageInfo<>(memberList);
        pageDto.setTotal(pageInfo.getTotal());
        List<MemberDto> memberDtoList = CopyUtil.copyList(memberList, MemberDto.class);
        pageDto.setList(memberDtoList);
    }

    /**
     * save
     */
    public void save(MemberDto memberDto) {
        Member member = CopyUtil.copy(memberDto, Member.class);
        if (StringUtils.isEmpty(memberDto.getId())) {
            this.insert(member);
        } else {
            this.update(member);
        }
    }

    /**
     * insert
     */
    private void insert(Member member) {
        Date now = new Date();
        member.setId(UuidUtil.getShortUuid());
        member.setRegisterTime(now);
        memberMapper.insert(member);
    }

    /**
     * update
     */
    private void update(Member member) {
        memberMapper.updateByPrimaryKey(member);
    }

    /**
     * delete
     */
    public void delete(String id) {
        memberMapper.deleteByPrimaryKey(id);
    }


    public MemberDto findByMobile(String mobile){
        Member member = this.selectByMobile(mobile);
        return CopyUtil.copy(member, MemberDto.class);
    }

    public Member selectByMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return null;
        }

        MemberExample memberExample = new MemberExample();
        memberExample.createCriteria().andMobileEqualTo(mobile);
        List<Member> memberList = memberMapper.selectByExample(memberExample);
        if(memberList == null || memberList.size() == 0){
            return null;
        }
        else {
            return memberList.get(0);
        }
    }

    public LoginMemberDto login(MemberDto memberDto){
        Member member = selectByMobile(memberDto.getMobile());
        if(member == null){
            throw new BusinessException((BusinessExceptionCode.LOGIN_MEMBER_ERROR));
        }
        else {
            if(member.getPassword().equals(memberDto.getPassword())){
                LoginMemberDto loginMemberDto = CopyUtil.copy(member, LoginMemberDto.class);
                return loginMemberDto;
            }
            else {
                throw new BusinessException(BusinessExceptionCode.LOGIN_MEMBER_ERROR);
            }
        }
    }

    public void resetPassword(MemberDto memberDto) throws BusinessException{
        Member memberDb = this.selectByMobile(memberDto.getMobile());
        if(memberDb == null){
            throw new BusinessException(BusinessExceptionCode.MEMBER_NOT_EXIST);
        }
        else {
            Member member = new Member();
            member.setId(memberDb.getId());
            member.setPassword(memberDb.getPassword());
            memberMapper.updateByPrimaryKeySelective(member);
        }
    }

}
