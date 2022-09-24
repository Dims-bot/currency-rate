package com.sdim2014.client.clients;

//import currencyRate.models.CurrencyRate;

import com.sdim2014.client.models.CurrencyRate;

import java.time.LocalDate;


public interface RateClient {
    CurrencyRate getCurrencyRate(String currency, LocalDate date);

}
