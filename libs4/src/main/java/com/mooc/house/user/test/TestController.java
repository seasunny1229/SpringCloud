package com.mooc.house.user.test;

import com.google.common.collect.ImmutableMap;
import com.mooc.house.user.common.PageParams;
import com.mooc.house.user.mapper.AgencyMapper;
import com.mooc.house.user.mapper.AgentMapper;
import com.mooc.house.user.mapper.UserMapper;
import com.mooc.house.user.model.Agency;
import com.mooc.house.user.model.User;
import com.mooc.house.user.service.UserService;
import com.mooc.house.user.utils.JwtHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    // region field
    @Autowired
    private PersonMapper personMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AgencyMapper agencyMapper;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    // endregion

    // region archive
    @GetMapping
    public String test(){
        return "hello user";
    }

    @GetMapping("/numbers/{input}")
    public String testNumbers(@PathVariable("input") int input){
        return String.valueOf(input);
    }

    @GetMapping("/strings")
    public String testStrings(@RequestParam("input") String input){
        return input;
    }

    @GetMapping("/persons/{id}")
    public Person testPersons(@PathVariable("id") int id){
        return personMapper.selectById(id);
    }

    @GetMapping("/redis/{input}")
    public String testRedisSet(@PathVariable("input") String input){
        stringRedisTemplate.opsForValue().set("test", input);
        return "success";
    }

    @GetMapping("/redis")
    public String testRedisGet(){
        return stringRedisTemplate.opsForValue().get("test");
    }

    @GetMapping("/simulations/time-consuming/default")
    public String simulateDefaultTimeConsumingProcess(){
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "finish default time consuming process";
    }

    @GetMapping("/simulations/time-consuming")
    public String simulateCustomTimeConsumingProcess(@RequestParam("duration") long duration){
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "finish custom time consuming process";
    }

    @GetMapping("/jwt/gen")
    public String genToken(@RequestParam("name") String name, @RequestParam("email") String email){
        String token = JwtHelper.genToken(ImmutableMap.of("email", email, "name", name, "ts", String.valueOf(Instant.now().getEpochSecond())));
        return token;
    }

    @GetMapping("/jwt/verify")
    public String verifyToken(@RequestParam("token") String token){
        Map<String, String> map = JwtHelper.verifyToken(token);
        if(map == null){
            return "failure";
        }
        String email = map.get("email");
        String name = map.get("name");

        return "email: " + email + " name: " + name;
    }

    @GetMapping("/agencies/insert")
    public int insertAgency(){
        Agency agency = new Agency();
        agency.setName("hello");
        agency.setAboutUs("hello");
        agency.setAddress("hello");
        agency.setEmail("1155");
        agency.setPhone("1122");
        agency.setWebSite("gagg");
        agency.setMobile("ga");
        return agencyMapper.insert(agency);
    }

    @GetMapping("/agencies/select")
    public List<Agency> selectAgencies(){
        Agency agency = new Agency();
        agency.setEmail("1155");
        return agencyMapper.select(agency);
    }

    @GetMapping("/agents/count")
    public Long getNumAgents(){
        User user = new User();
        return agentMapper.selectAgentCount(user);
    }

    @GetMapping("/agents")
    public List<User> getAgents(){
        User user = new User();
        PageParams pageParams = PageParams.build();
        pageParams.setOffset(1);
        pageParams.setLimit(5);
        return agentMapper.selectAgent(user, pageParams);
    }

    @GetMapping("/users/total")
    public List<User> getTotalUser() {
        return userMapper.select(new User());
    }

    @GetMapping("/users/part")
    public List<User> getPartUser() {
        User user = new User();
        user.setType(1);
        return userMapper.select(user);
    }

    @GetMapping("/users/id")
    public List<User> getUserById() {
        return userMapper.selectById(13L);
    }

    @GetMapping("/users/email")
    public List<User> getUserByEmail() {
        return userMapper.selectByEmail("ugx1zl@163.com");
    }

    @GetMapping("/users/insert")
    public int insertUser(){
        User user = new User();
        user.setName("hello");
        user.setPhone("hello");
        user.setEmail("hello");
        user.setAboutme("hello");
        user.setPasswd("hello");
        user.setAvatar("hello");
        user.setType(1);
        user.setCreateTime(new Date());
        user.setEnable(1);
        user.setAgencyId(0);
        return userMapper.insert(user);
    }

    @GetMapping("/users/delete")
    public int deleteUser(){
        return userMapper.delete("hello");
    }

    @GetMapping("/users/update")
    public int updateUser(){
        User user = new User();
        user.setName("world");
        user.setEmail("hello");
        return userMapper.update(user);
    }

    // endregion

    // region service
    @GetMapping("/users/insert_new")
    public int insertNewUser(){
        User user = new User();

        user.setEmail("sea_sunny1229@163.com");
        user.setPasswd("123456");
        user.setType(User.TYPE_USER);

        return userService.register(user, "sssss");
    }


    // endregion



}
