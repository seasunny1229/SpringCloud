package com.seasunny.authentication.util;


import com.seasunny.authentication.exception.ValidatorException;

import org.springframework.util.StringUtils;

/**
 *
 * 输入校验
 *
 * 输入是否为空
 * 输入长度限制
 *
 */

public class ValidatorUtil {


    public static void require(Object str, String fieldName){
        if(StringUtils.isEmpty(str)){
            throw new ValidatorException(fieldName +  "不能为空");
        }
    }


    public static void length(String str, String fieldName, int min, int max){
        if(StringUtils.isEmpty(str)){
            return;
        }

        int length = 0;

        if(!StringUtils.isEmpty(str)){
            length = str.length();
        }

        if(length < min || length > max){
            throw new ValidatorException(fieldName + "长度" + min + "~" + max + "位");
        }

    }


}
