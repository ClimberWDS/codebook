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
@ConfigurationProperties(prefix = "telegram.config.proxy")
public class TelegramProxyConfig {

    private String host = "127.0.0.1";
    private Integer port = 21881;
    private String userName = "wds_94@outlook.com";
    private String password = "wds@20180704";



}