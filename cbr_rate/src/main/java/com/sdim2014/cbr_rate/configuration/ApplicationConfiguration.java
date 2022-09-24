package com.sdim2014.cbr_rate.configuration;

import com.sdim2014.cbr_rate.model.CachedCurrencyRates;
import com.sdim2014.cbr_rate.model.CachedCurrencyRates;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@EnableConfigurationProperties(CbrConfiguration.class)
public class ApplicationConfiguration {
    final CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder().build(true);

    @Bean
    public Cache<LocalDate, CachedCurrencyRates> currencyRatesCache(@Value("${app.cache.size}") int cacheSize) {
        return cacheManager.createCache("CurrencyRate-Cache",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(LocalDate.class,
                        CachedCurrencyRates.class,
                        ResourcePoolsBuilder.heap(cacheSize)).
                        build());
    }
}
