package com.course.business.exception;

/**
 *
 * 输入校验时抛出的异常
 *
 * 此异常的message为回给用户，表示校验失败的文案
 *
 */

public class ValidatorException extends RuntimeException{

    public ValidatorException(String s) {
        super(s);
    }
}
