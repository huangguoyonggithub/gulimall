package com.hgy.product.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.hgy.product.properties.AliOssProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * # 自定义配置，配置对象存储 oss
 * @author hgy
 * @Description
 * @created 2024/4/10 0:53
 */
@Configuration
public class OSSConfig {

    @Autowired
    AliOssProperties aliOssProperties;

    @Bean
    public OSS ossClient() {
        return new OSSClientBuilder().build(aliOssProperties.getEndpoint(), aliOssProperties.getAccessKeyId(), aliOssProperties.getAccessKeySecret());
    }
}
