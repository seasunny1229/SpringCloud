package com.mooc.house.api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GenericRest {
    public static final String DIRECT_FLAG = "direct://";

    // load balance
    @Autowired
    private RestTemplate lbRestTemplate;

    // direct
    @Autowired
    private RestTemplate directRestTemplate;

    public <T> ResponseEntity<T> post(String url, Object reqBody, ParameterizedTypeReference<T> responseType){
        RestTemplate restTemplate = getRestTemplate(url);
        url.replace(DIRECT_FLAG, "");
        return restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(reqBody), responseType);
    }

    public <T> ResponseEntity<T> get(String url, ParameterizedTypeReference<T> responseType){
        RestTemplate restTemplate = getRestTemplate(url);
        url.replace(DIRECT_FLAG, "");
        return restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, responseType);
    }

    private RestTemplate getRestTemplate(String url){
        if(url.contains(DIRECT_FLAG)){
            return directRestTemplate;
        }
        else {
            return lbRestTemplate;
        }
    }


}
