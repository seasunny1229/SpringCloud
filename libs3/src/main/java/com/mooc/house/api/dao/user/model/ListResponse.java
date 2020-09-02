package com.mooc.house.api.dao.user.model;

import java.util.List;

public class ListResponse<T> {

    public static <T> ListResponse<T> build(List<T> list, Long count){
        ListResponse<T> listResponse = new ListResponse<>();
        listResponse.setList(list);
        listResponse.setCount(count);
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
}
