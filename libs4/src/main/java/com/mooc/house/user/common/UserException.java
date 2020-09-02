package com.mooc.house.user.common;

import com.mooc.house.user.exception.WithTypeException;

public class UserException extends RuntimeException implements WithTypeException {

    private static final long serialVersionUID = 1L;

    private Type type;

    public UserException(String msg) {
        super(msg);
        this.type = Type.LACK_PARAMETER;
    }

    public UserException(String message, Type type) {
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUMBER,
        LACK_PARAMETER,
        USER_NOT_LOGIN,
        USER_NOT_FOUND,
        USER_AUTH_FAIL
    }


}
