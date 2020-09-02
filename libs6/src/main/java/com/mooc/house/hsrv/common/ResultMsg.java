package com.mooc.house.hsrv.common;

import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class ResultMsg {

    public static final String ERROR_MSG_KEY = "error_msg";
    public static final String SUCCESS_MSG_KEY = "success_msg";


    public static ResultMsg errorMsg(String msg){
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setErrorMsg(msg);
        return resultMsg;
    }

    public static ResultMsg successMsg(String msg){
        ResultMsg resultMsg = new ResultMsg();
        resultMsg.setSuccessMsg(msg);
        return resultMsg;
    }

    public static ResultMsg success(){
        return new ResultMsg();
    }

    private String errorMsg;
    private String successMsg;

    public boolean isSuccess(){
        return errorMsg == null;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getSuccessMsg() {
        return successMsg;
    }

    public void setSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
    }

    @Override
    public String toString() {
        return "ResultMsg{" +
                "errorMsg='" + errorMsg + '\'' +
                ", successMsg='" + successMsg + '\'' +
                '}';
    }

    public Map<String, String> asMap(){
        Map<String, String> map = Maps.newHashMap();
        map.put(SUCCESS_MSG_KEY, successMsg);
        map.put(ERROR_MSG_KEY, errorMsg);
        return map;
    }

    public String asUrlParams(){
        Map<String, String> map = asMap();
        Map<String, String> newMap = Maps.newHashMap();
        map.forEach((s, s2) -> {
            if(s2 != null){
                try{
                    newMap.put(s, URLEncoder.encode(s2, "utf-8"));
                }catch (UnsupportedEncodingException e){

                }
            }
        });
        return Joiner.on("&").useForNull("").withKeyValueSeparator("=").join(newMap);
    }


}
