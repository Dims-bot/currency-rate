package com.sdim2014.cbr_rate.parser;

import com.sdim2014.cbr_rate.model.CurrencyRate;
import com.sdim2014.cbr_rate.model.CurrencyRate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CurrencyRateParserXmlTest {

    @Test
    void parserTest() throws IOException, URISyntaxException {
        CurrencyRateParser parser = new CurrencyRateParserXml();

        URI uri = ClassLoader.getSystemResource("cbr_response.xml").toURI();
        String ratesXml = Files.readString(Paths.get(uri), Charset.forName("Windows-1251"));

        List<CurrencyRate> rates = parser.parse(ratesXml);

        assertThat(rates.size()).isEqualTo(34);
        assertThat(rates.contains(getUSDRate())).isTrue();


    }

    CurrencyRate getUSDRate() {
        return CurrencyRate.builder()
                .numCode("840")
                .charCode("USD")
                .nominal("1")
                .name("Доллар США")
                .value("66,2378")
                .build();


    }
}
