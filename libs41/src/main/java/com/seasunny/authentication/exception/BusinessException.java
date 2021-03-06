package com.seasunny.authentication.exception;

/**
 *
 * business exception
 *
 * 返回用户失败信息
 *
 */
public class BusinessException extends RuntimeException{

    private BusinessExceptionCode code;

    public BusinessException(BusinessExceptionCode code){
        super(code.getDesc());
        this.code = code;
    }

    public BusinessExceptionCode getCode() {
        return code;
    }

    public void setCode(BusinessExceptionCode code) {
        this.code = code;
    }

    // do not write heap-stack info
    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
