package com.mooc.house.api.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;


@Configuration
@ConditionalOnClass(HttpClient.class)
@EnableConfigurationProperties(HttpClientProperties.class)
public class HttpClientAutoConfiguration {

    public HttpClientAutoConfiguration(HttpClientProperties httpClientProperties){
        this.httpClientProperties = httpClientProperties;
    }

    private final HttpClientProperties httpClientProperties;

    @Autowired
    private LogbookHttpRequestInterceptor logbookHttpRequestInterceptor;

    @Autowired
    private LogbookHttpResponseInterceptor logbookHttpResponseInterceptor;

    @Bean
    @ConditionalOnMissingBean(HttpClient.class)
    public HttpClient httpClient(){
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(httpClientProperties.getConnectTimeOut())
                .setSocketTimeout(httpClientProperties.getSocketTimeOut())
                .build();

        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setUserAgent(httpClientProperties.getAgent())
                .setMaxConnPerRoute(httpClientProperties.getMaxConnPerRoute())
                .setMaxConnTotal(httpClientProperties.getMaxConnTotal())
                .addInterceptorFirst(logbookHttpRequestInterceptor)
                .addInterceptorFirst(logbookHttpResponseInterceptor)
                .build();

        return httpClient;
    }


}
