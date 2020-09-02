package com.mooc.house.hsrv.exception;

import com.google.common.collect.ImmutableMap;
import com.mooc.house.hsrv.common.RestCode;

public class Exception2CodeMap {

    private static final ImmutableMap<Object, RestCode> MAP = ImmutableMap.<Object, RestCode>builder()
            .put(IllegalParamsException.Type.WRONG_PAGE_NUMBER, RestCode.WRONG_PAGE)
            .build();


    public static ImmutableMap<Object, RestCode> getMap(){
        return MAP;
    }

    public static RestCode getCode(Object exOrType){
        RestCode restCode = MAP.get(exOrType);
        if(restCode == null){
            return RestCode.UNKNOWN_ERROR;
        }
        return restCode;
    }

}
