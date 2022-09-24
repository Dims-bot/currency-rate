package com.sdim2014.client.service;

import com.sdim2014.client.clients.RateClient;
import com.sdim2014.client.models.CurrencyRate;
import com.sdim2014.client.models.ProviderCurrencyRate;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CurrencyRateEndpointService {

    Map<String, RateClient> clients;

    public CurrencyRateEndpointService(Map<String, RateClient> clients) {
        this.clients = clients;
    }

    public CurrencyRate getCurrencyRate(ProviderCurrencyRate providerCurrencyRate, String currency, LocalDate date) {
        log.info("getCurrentRate. providerCurrencyRate:{}, currency:{}, date:{}", providerCurrencyRate, currency, date);
        RateClient client = clients.get(providerCurrencyRate.getServiceName());
        return client.getCurrencyRate(currency, date);
    }
}
