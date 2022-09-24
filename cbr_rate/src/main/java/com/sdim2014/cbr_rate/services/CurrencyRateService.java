package com.sdim2014.cbr_rate.services;

import com.sdim2014.cbr_rate.configuration.CbrConfiguration;
import com.sdim2014.cbr_rate.exceptions.CurrencyRateNotFoundException;
import com.sdim2014.cbr_rate.model.CachedCurrencyRates;
import com.sdim2014.cbr_rate.model.CurrencyRate;
import com.sdim2014.cbr_rate.parser.CurrencyRateParser;
import com.sdim2014.cbr_rate.requester.CbrRequester;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.ehcache.Cache;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CurrencyRateService {

    static String Date_Format = "dd/MM/yyyy";
    static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Date_Format);

    CbrRequester cbrRequester;
    CurrencyRateParser currencyRateParser;
    CbrConfiguration cbrConfiguration;
    Cache<LocalDate, CachedCurrencyRates> currencyRateCache;

    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        log.info("getCurrencyRate. currency:{}, date:{}", currency, date);
        List<CurrencyRate> rates;

        CachedCurrencyRates cachedCurrencyRate = currencyRateCache.get(date);
        if (cachedCurrencyRate == null) {
            String urlWithParams = String.format("%s?date_req=%s", cbrConfiguration.getUrl(), DATE_FORMATTER.format(date));
            String ratesAsXml = cbrRequester.getRatesAsXml(urlWithParams);
            rates = currencyRateParser.parse(ratesAsXml);
            currencyRateCache.put(date, new CachedCurrencyRates(rates));
        } else {
            rates = cachedCurrencyRate.getCurrencyRates();
        }
        return rates.stream().filter(rate -> currency.equals(rate.getCharCode()))
                .findFirst()
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency Rate not found. Currency: "
                        + currency + " , date: " + date));

    }

}
