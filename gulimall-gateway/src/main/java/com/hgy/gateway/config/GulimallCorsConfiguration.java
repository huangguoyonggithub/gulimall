package com.hgy.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author hgy
 * @Description 配置跨域
 * @created 2024/4/5 0:24
 */
@Configuration
public class GulimallCorsConfiguration {

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        //所有跨域有关的信息
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1、配置跨域
        corsConfiguration.addAllowedHeader("*");  //允许所有头进行跨域
        corsConfiguration.addAllowedMethod("*");  //允许所有请求方式进行跨域
//        corsConfiguration.addAllowedOrigin("*");  //允许所有请求来源进行跨域
        corsConfiguration.addAllowedOriginPattern("*");
        corsConfiguration.setAllowCredentials(true);  //是否允许携带cooker进行跨域

        source.registerCorsConfiguration("/**",corsConfiguration); //所有请求都支持跨域

        return new CorsWebFilter(source);
    }
}
