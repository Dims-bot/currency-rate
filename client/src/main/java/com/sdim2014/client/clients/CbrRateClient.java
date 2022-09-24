package com.sdim2014.client.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdim2014.client.configuration.CbrRateClientConfiguration;
import com.sdim2014.client.models.CurrencyRate;
import com.sdim2014.client.configuration.CbrRateClientConfiguration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service("cbr")
@RequiredArgsConstructor
@SpringBootApplication
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CbrRateClient implements RateClient {
    public static String DATE_FORMAT = "dd-MM-yyyy";
    static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    CbrRateClientConfiguration configuration;
    com.sdim2014.client.clients.HttpClient httpClient;
    ObjectMapper objectMapper;


    @Override
    public CurrencyRate getCurrencyRate(String currency, LocalDate date) {
        log.info("getCurrencyRate currency:{}, date{}", currency, date);
        String urlWithParam = String.format("%s/%s/%s", configuration.getUrl(), currency, DATE_FORMATTER.format(date));
        log.info("urlWithParam:{}", urlWithParam);

        try {
            String response = httpClient.performRequest(urlWithParam);
            return objectMapper.readValue(response, CurrencyRate.class);
        } catch (HttpClientException exception) {
            throw new RateClientException("Error from Cbr Client host: " + exception.getMessage());
        } catch (Exception exception) {
            log.error("Getting currencyRate error, currency:{}, date:{}", currency, date, exception);
            throw new RateClientException("Can't get currencyRate. currency: " + currency + "date: " + date);

        }

    }
}
