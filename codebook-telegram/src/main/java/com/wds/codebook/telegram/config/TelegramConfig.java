package com.wds.codebook.telegram.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * dms
 * @author: wds
 * @DateTime: 2023/6/8 20:52
 */

@Data
@RefreshScope
@Configuration
@ConfigurationProperties(prefix = "telegram.config")
public class TelegramConfig {

    private String token = "7510127937:AAHRhPAaOWJzILZITamRblKbtXnSla82aLE";
    private String username ="wds_010_bot";
    private Long chartId = -4565501052L;


}