package com.mooc.house.api.dao;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class TestAppZuulFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 2;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request  = ctx.getRequest();
        return request.getRequestURI().contains("/test");
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest  request  = ctx.getRequest();
        if(request.getRequestURI().contains("/test/numbers")){
            throw new IllegalStateException();
        }
        return null;
    }
}
