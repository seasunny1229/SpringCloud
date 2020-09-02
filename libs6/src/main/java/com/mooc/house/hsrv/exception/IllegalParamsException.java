package com.mooc.house.hsrv.exception;

public class IllegalParamsException extends RuntimeException {

    private Type type;

    public IllegalParamsException(Type type, String message){
        super(message);
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public enum Type{
        WRONG_PAGE_NUMBER;
    }

}
