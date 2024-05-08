package com.wds.codebook.gpt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @author wds
 * @DateTime: 2023/10/3 15:01
 */
@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "openapi.config")
public class OpenApiConfig {
    private String openApiToken = "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";
}