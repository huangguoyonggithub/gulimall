package com.hgy.product.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * # 自定义配置，配置对象存储 oss
 * @author hgy
 * @Description
 * @created 2024/4/10 15:55
 */
@Component
@ConfigurationProperties(prefix = "hgy.alioss")
@Data
public class AliOssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
}
