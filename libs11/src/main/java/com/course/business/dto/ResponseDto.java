package com.course.business.dto;

/**
 *
 * 返回前端的实体类 REST API json格式
 *
 * boolean: success
 * string: code
 * string: message
 * object: content
 *
 *
 * @param <T>
 */
public class ResponseDto<T> {

    // business success flag
    private boolean success = true;

    // response code
    private String code;

    // response message
    private String message;

    // data
    private T content;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ResponseDto{" +
                "success=" + success +
                ", code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", content=" + content +
                '}';
    }
}
