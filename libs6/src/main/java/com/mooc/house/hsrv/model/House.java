package com.mooc.house.hsrv.model;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.List;

public class House {
    public static final int STATE_DOWN = 2;

     // bigint unsigned
     private Long id;

     // varchar(20)
     private String name;

     // tinyint(1)
     private Integer type;

     // int
     private Integer price;

     // varchar(1024)
     private String images;

     // int
     private Integer area;

     // int
     private Integer beds;

     // int
     private Integer baths;

     // double
     private Double rating;

     // varchar(512)
     private String remarks;

     // varchar(512)
     private String properties;

     // varchar(250)
     private String floorPlan;

     // varchar(20)
     private String tags;

     // date
     private Date createTime;

     // int
     private Integer cityId;

     // int
     private Integer communityId;

     // varchar(20)
     private String address;

     // int
     private Integer state;

/*   private Integer roundRating = 0;
     private Long userId;
     private boolean bookmarked;
     private List<Long> ids;
     private String sort = "time_desc";
     private List<String> featureList = Lists.newArrayList();
     private String priceStr;*/

    private String firstImg;
    private List<String> imageList = Lists.newArrayList();
    private List<String> floorPlanList = Lists.newArrayList();


    /**
     *
     * Query
     *
     */
    private List<Long> ids;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
        if(StringUtils.isNotBlank(images)){
            List<String> list = Splitter.on(",").splitToList(images);
            if(!list.isEmpty()){
                this.firstImg = list.get(0);
                this.imageList = list;
            }
        }
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }

    public String getFloorPlan() {
        return floorPlan;
    }

    public void setFloorPlan(String floorPlan) {
        this.floorPlan = floorPlan;
        if(StringUtils.isNotBlank(floorPlan)){
            this.floorPlanList = Splitter.on(",").splitToList(floorPlan);
        }
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getCityId() {
        return cityId;
    }

    public void setCityId(Integer cityId) {
        this.cityId = cityId;
    }

    public Integer getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Integer communityId) {
        this.communityId = communityId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public String getFirstImg() {
        return firstImg;
    }

    public void setFirstImg(String firstImg) {
        this.firstImg = firstImg;
    }

    public List<String> getImageList() {
        return imageList;
    }

    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public List<String> getFloorPlanList() {
        return floorPlanList;
    }

    public void setFloorPlanList(List<String> floorPlanList) {
        this.floorPlanList = floorPlanList;
    }
}
