package com.seasunny.test.controller;

import com.seasunny.test.dto.UserDto;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/user")
@RestController
public class UserController {

    @PostMapping("/value")
    public UserDto value(@RequestParam(value = "name", required = false) String name,
                         @RequestParam("id") String id){
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        return userDto;
    }

    @PostMapping("model")
    public String model(@ModelAttribute UserDto userDto){

        UserDto userResponseDto = new UserDto();

        // pass value
        userResponseDto.setId(userDto.getId());
        userResponseDto.setName(userDto.getName());

        return userResponseDto.toString();
    }

    @PostMapping("json")
    public String json(@RequestBody UserDto userDto){

        UserDto userResponseDto = new UserDto();

        // pass value
        userResponseDto.setId(userDto.getId());
        userResponseDto.setName(userDto.getName());

        return userResponseDto.toString();
    }


}
