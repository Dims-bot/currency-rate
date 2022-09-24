package com.sdim2014.cbr_rate.controllers;

import com.sdim2014.cbr_rate.model.CurrencyRate;
import com.sdim2014.cbr_rate.services.CurrencyRateService;
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
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping(path = "${app.rest.api.prefix}")
public class CurrencyRateController {

    final CurrencyRateService currencyRateService;


    @GetMapping("currencyRate/{currency}/{date}")
    public CurrencyRate getCurrencyRate(@PathVariable("currency") String currency,
                                        @DateTimeFormat(pattern = "dd-MM-yyyy") @PathVariable("date") LocalDate date) {
        log.info("getCurrencyRate, currency:{}, date:{}", currency, date);

        CurrencyRate rate = currencyRateService.getCurrencyRate(currency, date);
        log.info("rate:{}", rate);

        return rate;
    }

}
