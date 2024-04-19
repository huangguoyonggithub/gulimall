package com.hgy.thirdparty.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author hgy
 * @Description
 * @created 2024/4/10 17:18
 */
@Component
@ConfigurationProperties(prefix = "hgy.alioss")
@Data
public class AliOssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucket;
}
