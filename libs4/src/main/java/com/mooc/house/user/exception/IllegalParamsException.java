package com.mooc.house.user.exception;

public class IllegalParamsException extends RuntimeException implements WithTypeException{

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Type type;

    public IllegalParamsException() {

    }

    public IllegalParamsException(Type type, String msg){
        super(msg);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUMBER,
        WRONG_TYPE
    }
}
