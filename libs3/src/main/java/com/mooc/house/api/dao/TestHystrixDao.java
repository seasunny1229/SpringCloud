package com.mooc.house.api.dao;

import com.mooc.house.api.config.GenericRest;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

@Repository
@DefaultProperties(
        groupKey = "testHystrixDao",
        commandProperties = @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
        threadPoolProperties = {
                @HystrixProperty(name = "coreSize", value = "3"),
                @HystrixProperty(name = "maxQueueSize", value = "3"),
        },
        threadPoolKey = "testHystrixDao"
)
public class TestHystrixDao {

    @Autowired
    private GenericRest genericRest;

    @HystrixCommand(fallbackMethod = "simulateDefaultTimeConsumingFallback")
    public String simulateDefaultTimeConsuming(){
        String url =  "http://user/test/simulations/time-consuming/default";
        ResponseEntity<String> result = genericRest.get(url, new ParameterizedTypeReference<String>() {});
        return "the result from rest template is: " + result.getBody();
    }

    @HystrixCommand
    public String simulateCustomTimeConsuming(String input){
        String url =  "http://user/test/simulations/time-consuming?duration=" + input;
        ResponseEntity<String> result = genericRest.get(url, new ParameterizedTypeReference<String>() {});
        return "the result from rest template is: " + result.getBody();
    }

    public String simulateDefaultTimeConsumingFallback(){
        return "the result from rest template is: simulate default time consuming fallback";
    }


}
