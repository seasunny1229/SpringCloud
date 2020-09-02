package com.mooc.house.hsrv.service;

import com.google.common.collect.Lists;
import com.mooc.house.hsrv.common.LimitOffset;
import com.mooc.house.hsrv.mapper.CommunityMapper;
import com.mooc.house.hsrv.mapper.HouseMapper;
import com.mooc.house.hsrv.model.Community;
import com.mooc.house.hsrv.model.House;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HouseService {

    @Autowired
    private HouseMapper houseMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Value("${file.prefix}")
    private String imgPrefix;

    /**
     * base
     *
     */
    public List<House> getAllHouses(){
        House house = new House();
        return houseMapper.selectHouses(house);
    }

    public List<House> getHouseById(Long id){
        House house = new House();
        house.setId(id);
        return houseMapper.selectHouses(house);
    }

    public List<House> getHouseByIds(List<Long> ids){
        House query = new House();
        query.setIds(ids);
        return houseMapper.selectHouses(query);
    }

    public List<House> getHouseByLimit(Integer limit, Integer offset, String order){
        House house = new House();
        LimitOffset limitOffset = LimitOffset.build(limit, offset);
        return houseMapper.selectHouseByLimit(house, limitOffset, order);
    }

    public Long getNumberHouses(House house){
        return houseMapper.getNumberHouses(house);
    }

    public int updateHouse(House house){
        return houseMapper.updateHouse(house);
    }


    /**
     * service
     *
     */
    public Pair<List<House>, Long> queryHouse(House query, LimitOffset limitOffset){
        List<House> houses = Lists.newArrayList();
        House houseQuery = query;

        // get houses if the house's name is present
        if(StringUtils.isNoneBlank(houseQuery.getName())){
            Community community = new Community();
            community.setName(houseQuery.getName());
            List<Community> communities = communityMapper.selectCommunities(community);
            if(!communities.isEmpty()){
                houseQuery = new House();
                houseQuery.setCommunityId(communities.get(0).getId());

            }
        }

        // get houses
        houses = queryAndImg(houseQuery, limitOffset);

        // get total amount of houses
        Long count = houseMapper.getNumberHouses(houseQuery);

        return ImmutablePair.of(houses, count);
    }

    public House queryOneHouse(Long id){
        House query = new House();
        query.setId(id);
        List<House> houses = queryAndImg(query, LimitOffset.build(1,0));
        if(!houses.isEmpty()){
            return houses.get(0);
        }

        return null;
    }

    public int updateRating(Long id, Double rating){

        // updating house data
        House house = queryOneHouse(id);
        Double oldRating = house.getRating();

        Double newRating = oldRating.equals(0D) ? rating :  Math.min(Math.round(oldRating + rating)/2, 5);

        House updateHouse = new House();
        updateHouse.setId(id);
        updateHouse.setRating(newRating);

        // update house info
        int result = houseMapper.updateHouse(updateHouse);

        return result;
    }

    private List<House> queryAndImg(House query, LimitOffset pageParams){

        // query houses
        List<House> houses = houseMapper.selectHouses(query);

        // set image
        houses.forEach(h -> {
          h.setFirstImg(imgPrefix + h.getFirstImg());
          h.setImageList(h.getImageList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
          h.setFloorPlanList(h.getFloorPlanList().stream().map(img -> imgPrefix + img).collect(Collectors.toList()));
        });

        return houses;
    }



}
