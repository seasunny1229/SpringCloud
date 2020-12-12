package com.seasunny.authentication.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

import javax.sql.DataSource;

/**
 *
 * 配置数据库连接池 Druid
 */
@Configuration
public class DruidConfig {

    /**
     *
     * 连接池 DataSource
     *
     * @return
     */
    @ConfigurationProperties(prefix = "spring.druid")
    @Bean(initMethod = "init", destroyMethod = "close")
    public DataSource dataSource(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setProxyFilters(Collections.singletonList(statFilter()));
        return dataSource;
    }

    /**
     *
     * Druid 连接池参数配置
     *
     * @return
     */
    @Bean
    public Filter statFilter(){
        StatFilter statFilter = new StatFilter();

        // 慢SQL
        statFilter.setSlowSqlMillis(5000);

        // 慢SQL log打印
        statFilter.setLogSlowSql(true);

        statFilter.setMergeSql(true);

        return statFilter;
    }

    /**
     *
     * 可视化
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> servletRegistrationBean(){
        return new ServletRegistrationBean<>(new StatViewServlet(), "/druid/");
    }

}
