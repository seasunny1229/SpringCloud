package com.seasunny.test;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private long duration;

    private List<String> fields;

    private List<String[]> data;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String[]> getData() {
        return data;
    }

    public void setData(List<String[]> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "duration=" + duration +
                ", fields=" + fields +
                ", data=" + data +
                '}';
    }
}
