package com.sdim2014.client.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "cbr-rate-client")
public class CbrRateClientConfiguration {
    String url;
}
