package com.mooc.house.api.dao.comment.model;

import java.util.List;

public class ListResponse<T> {

    public static <T> ListResponse<T> build(List<T> list, Long count){
        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setCount(count);
        listResponse.setList(list);
        return listResponse;
    }

    private List<T> list;

    private Long count;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ListResponse{" +
                "list=" + list +
                ", count=" + count +
                '}';
    }
}
