package com.mooc.house.comment.service;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Strings;
import com.mooc.house.comment.mapper.CommentMapper;
import com.mooc.house.comment.model.Comment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CommentService implements ICommentService{

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * base
     */
    public Integer insert(Comment comment){
        return commentMapper.insert(comment);
    }

    public List<Comment> selectComments(Long houseId, int size){
        return commentMapper.selectComments(houseId, size);
    }

    public List<Comment> selectBlogComments(Long blogId, int size){
        return commentMapper.selectBlogComments(blogId, size);
    }

    /**
     * service
     */
    @Override
    public List<Comment> getHouseComments(Long houseId, Integer size) {

        // key
        String key = "house_comments" + "_" + houseId + "_" + size;

        // get value
        String json = redisTemplate.opsForValue().get(key);

        List<Comment> lists;

        if(Strings.isNullOrEmpty(json)){
            lists = doGetHouseComments(houseId, size);

            redisTemplate.opsForValue().set(key, JSON.toJSONString(lists));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        }
        else {
            lists = JSON.parseObject(json, new ParameterizedTypeReference<List<Comment>>(){}.getType());
        }

        return lists;
    }

    private List<Comment> doGetHouseComments(Long houseId, Integer size){

        // get comments
        List<Comment> comments = commentMapper.selectComments(houseId, size);

        // get user info


        return comments;
    }

    @Override
    public List<Comment> getBlogComments(Long blogId, Integer size) {

        // key
        String key = "blog_comments" + "_" + blogId + "_" + size;

        // get value
        String json = redisTemplate.opsForValue().get(key);

        List<Comment> comments = null;

        if(Strings.isNullOrEmpty(json)){
            comments = doGetBlogComments(blogId, size);

            redisTemplate.opsForValue().set(key, JSON.toJSONString(comments));
            redisTemplate.expire(key, 5, TimeUnit.MINUTES);
        }
        else {
            comments = JSON.parseObject(json, new ParameterizedTypeReference<List<Comment>>(){}.getType());
        }

        return comments;
    }

    private List<Comment> doGetBlogComments(Long blogId, Integer size){

        // get blog comments
        List<Comment> comments = commentMapper.selectBlogComments(blogId, size);

        // get user info


        return comments;
    }

    @Override
    public void addHouseComment(Long houseId, String content, Long userId){
        addComment(houseId, null, content, userId, Comment.TYPE_HOUSE);
    }

    @Override
    public void addBlogComment(Long blogId, String content, Long userId){
        addComment(null, blogId, content, userId, Comment.TYPE_COMMENT);
    }

    private void addComment(Long houseId, Long blogId, String content, Long userId, int type){

        String key = null;

        Comment comment = new Comment();

        if(type == Comment.TYPE_HOUSE){
            comment.setHouseId(houseId);
            key = "house_comments" + houseId;
            comment.setBlogId(0L);
        }
        else {
            comment.setBlogId(blogId);
            key = "blog_comments" + blogId;
            comment.setHouseId(0L);
        }

        comment.setContent(content);
        comment.setUserId(userId);
        comment.setType(type);

        //BeanHelper.onInsert(comment);
        //BeanHelper.setDefaultProp(comment, Comment.class);
        comment.setCreateTime(new Date());

        commentMapper.insert(comment);

        //redisTemplate.delete(redisTemplate.keys(key + "*"));
    }

}
