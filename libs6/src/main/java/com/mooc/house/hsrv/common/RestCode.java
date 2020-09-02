package com.mooc.house.hsrv.common;

public enum  RestCode {

    OK(0,"OK"),
    UNKNOWN_ERROR(1,"house service error"),
    WRONG_PAGE(10100, "page illegal"),
    USER_NOT_FOUND(10101, "user not found"),
    ILLEGAL_PARAMS(10102,"param illegal");


    public final int code;

    public final String msg;

    private RestCode(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
