package com.seasunny.test.dto;

public class UploadStringDto {

    private String key;

    private String content;

    private boolean append;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAppend() {
        return append;
    }

    public void setAppend(boolean append) {
        this.append = append;
    }
}
