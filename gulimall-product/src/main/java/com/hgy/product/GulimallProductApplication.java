package com.hgy.product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hgy.product.feign")  //开启远程调用功能
@SpringBootApplication
@MapperScan("com.hgy.product.dao")
@EnableDiscoveryClient
public class GulimallProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(GulimallProductApplication.class, args);
    }

}
