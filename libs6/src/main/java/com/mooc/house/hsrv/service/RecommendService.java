package com.mooc.house.hsrv.service;

import com.mooc.house.hsrv.common.LimitOffset;
import com.mooc.house.hsrv.model.House;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecommendService {

    private static final String HOT_HOUSE_KEY = "hot_house";

    @Autowired
    private HouseService houseService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void increaseHot(Long id){
        redisTemplate.opsForZSet().incrementScore(HOT_HOUSE_KEY, ""+id, 1.0D);
        redisTemplate.opsForZSet().removeRange(HOT_HOUSE_KEY, 0, -11);
    }

    public List<House> getHotHouses(int size){
        Set<String> idSet = redisTemplate.opsForZSet().reverseRange(HOT_HOUSE_KEY, 0, -1);
        assert idSet != null;
        List<Long> ids = idSet.stream().map(Long::parseLong).collect(Collectors.toList());
        House query = new House();
        query.setIds(ids);
        Pair<List<House>, Long> pair = houseService.queryHouse(query, LimitOffset.build(size, 0));
        return pair.getLeft();
    }

    public List<House> getLatest(int size){
        Pair<List<House>, Long> pair = houseService.queryHouse(new House(), LimitOffset.build(size, 0));
        return pair.getLeft();
    }


}
