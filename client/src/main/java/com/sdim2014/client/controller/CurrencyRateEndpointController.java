package com.sdim2014.client.controller;

import com.sdim2014.client.models.CurrencyRate;
import com.sdim2014.client.models.ProviderCurrencyRate;
import com.sdim2014.client.service.CurrencyRateEndpointService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(path = "${app.rest.api.prefix}")
public class CurrencyRateEndpointController {
    CurrencyRateEndpointService currencyRateEndpointService;

    @GetMapping("currencyRate/{provider}/{currency}/{date}")
    public CurrencyRate getCurrencyRate(@PathVariable("provider") ProviderCurrencyRate providerCurrencyRate,
                                        @PathVariable("currency") String currency,
                                        @DateTimeFormat(pattern = "dd-MM-yyyy") @PathVariable("date") LocalDate date) {

        log.info("getCurrencyRate, currency:{}, date:{}", currency, date);
        CurrencyRate currencyRate = currencyRateEndpointService.getCurrencyRate(providerCurrencyRate, currency, date);
        log.info("currencyRate:{}", currencyRate);
        return currencyRate;
    }
}
