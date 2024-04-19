package com.hgy.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 因为引入公共模块的时候导入了Mybatis-plus，而本模块不用（或者在pom文件直接排除Mybatis-plus依赖）
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class}) //排除数据源相关的自动配置
@EnableDiscoveryClient   //开启API网关
public class GulimallGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallGatewayApplication.class, args);
    }

}
