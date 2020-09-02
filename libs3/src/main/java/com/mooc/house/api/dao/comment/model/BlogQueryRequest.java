package com.mooc.house.api.dao.comment.model;

public class BlogQueryRequest {

    private Blog blog;

    private Integer limit;

    private Integer offset;

    public Blog getBlog() {
        return blog;
    }

    public void setBlog(Blog blog) {
        this.blog = blog;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "BlogQueryRequest{" +
                "blog=" + blog +
                ", limit=" + limit +
                ", offset=" + offset +
                '}';
    }
}
