package com.mooc.house.api.dao.comment.model;

import com.google.common.collect.Lists;

import java.util.Date;
import java.util.List;

public class Blog {

    private Integer id;
    private String tags;
    private String content;
    private String title;
    private Date createTime;
    private Integer cat;

    // extra
    private String digest;
    private List<String> tagList = Lists.newArrayList();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCat() {
        return cat;
    }

    public void setCat(Integer cat) {
        this.cat = cat;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", tags='" + tags + '\'' +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                ", createTime=" + createTime +
                ", cat=" + cat +
                ", digest='" + digest + '\'' +
                ", tagList=" + tagList +
                '}';
    }
}
