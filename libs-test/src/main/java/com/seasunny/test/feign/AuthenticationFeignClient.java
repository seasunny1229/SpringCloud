package com.seasunny.test.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "authentication")
public interface AuthenticationFeignClient {


    @GetMapping("authentication/test/net")
    String testNet();

}
