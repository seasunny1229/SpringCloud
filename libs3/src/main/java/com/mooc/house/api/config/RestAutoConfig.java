package com.mooc.house.api.config;

import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.apache.http.client.HttpClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Configuration
public class RestAutoConfig {

    public static class RestTemplateConfig{

        @Bean
        @LoadBalanced
        RestTemplate lbRestTemplate(HttpClient httpClient){
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            restTemplate.getMessageConverters().add(1, new FastJsonHttpMessageConverter5());
            return restTemplate;
        }

        @Bean
        RestTemplate directRestTemplate(HttpClient httpClient){
            RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
            restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
            restTemplate.getMessageConverters().add(1, new FastJsonHttpMessageConverter5());
            return restTemplate;
        }


        public static class FastJsonHttpMessageConverter5 extends FastJsonHttpMessageConverter {
            static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
            public FastJsonHttpMessageConverter5(){
                setDefaultCharset(DEFAULT_CHARSET);
                setSupportedMediaTypes(Arrays.asList(MediaType.APPLICATION_JSON,new MediaType("application","*+json")));
            }
        }

    }

}
