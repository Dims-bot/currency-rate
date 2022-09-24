package com.sdim2014.cbr_rate.controller;

import com.sdim2014.cbr_rate.configuration.ApplicationConfiguration;
import com.sdim2014.cbr_rate.configuration.CbrConfiguration;
import com.sdim2014.cbr_rate.configuration.JsonConfiguration;
import com.sdim2014.cbr_rate.controllers.CurrencyRateController;
import com.sdim2014.cbr_rate.parser.CurrencyRateParserXml;
import com.sdim2014.cbr_rate.requester.CbrRequester;
import com.sdim2014.cbr_rate.services.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CurrencyRateController.class)
@Import({ApplicationConfiguration.class, JsonConfiguration.class, CurrencyRateService.class, CurrencyRateParserXml.class})
public class CurrencyRateControllerTest {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CbrRequester cbrRequester;

    @MockBean
    CbrConfiguration cbrConfiguration;

    @Test
    @DirtiesContext
    void getCurrencyRateTest() throws Exception {

        String currency = "EUR";
        String date = "30-04-2022";

        prepareCbrRequesterMock(date);

        String result = mockMvc.perform(get(String.format("/api/currencyRate/%s/%s", currency, date)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString(StandardCharsets.UTF_8);

        assertThat(result).isEqualTo("{\"numCode\":\"978\",\"charCode\":\"EUR\",\"nominal\":\"1\",\"name\":\"Евро\",\"value\":\"70,0662\"}");

    }

    @Test
    @DirtiesContext
    void cacheUseTest() throws Exception {
        prepareCbrRequesterMock(null);

        String currency = "EUR";
        String date = "30-04-2022";

        mockMvc.perform(get(String.format("/api/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());
        mockMvc.perform(get(String.format("/api/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());

        currency = "USD";
        mockMvc.perform(get(String.format("/api/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());

        date = "04-05-2022";
        mockMvc.perform(get(String.format("/api/currencyRate/%s/%s", currency, date))).andExpect(status().isOk());

        verify(cbrRequester, times(2)).getRatesAsXml(any());

    }


    private void prepareCbrRequesterMock(String date) throws IOException, URISyntaxException {
        URI uri = ClassLoader.getSystemResource("cbr_response.xml").toURI();
        String rateXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        if (date == null) {
            when(cbrRequester.getRatesAsXml(any())).thenReturn(rateXml);
        } else {
            LocalDate dateParameter = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            String cbrUrl = String.format("%s?date_req=%s", cbrConfiguration.getUrl(), DATE_FORMATTER.format(dateParameter));
            when(cbrRequester.getRatesAsXml(cbrUrl)).thenReturn(rateXml);
        }
    }

}
