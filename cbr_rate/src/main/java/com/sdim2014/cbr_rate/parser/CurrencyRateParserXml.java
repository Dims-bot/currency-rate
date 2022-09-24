package com.sdim2014.cbr_rate.parser;

import com.sdim2014.cbr_rate.exceptions.CurrencyRateParsingException;
import com.sdim2014.cbr_rate.model.CurrencyRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class CurrencyRateParserXml implements CurrencyRateParser {

    @Override
    public List<CurrencyRate> parse(String ratesAsString) {
        List<CurrencyRate> rates = new ArrayList<>();

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        dbf.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
        try {
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder db = dbf.newDocumentBuilder();

            try (StringReader reader = new StringReader(ratesAsString)) {
                Document document = db.parse(new InputSource(reader));
                document.getDocumentElement().normalize();

                NodeList list = document.getElementsByTagName("Valute");


                for (int valuteIdx = 0; valuteIdx < list.getLength(); valuteIdx++) {
                    Node node = list.item(valuteIdx);
                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element element = (Element) node;

                        CurrencyRate currencyRate = CurrencyRate.builder()
                                .numCode(element.getElementsByTagName("NumCode").item(0).getTextContent())
                                .charCode(element.getElementsByTagName("CharCode").item(0).getTextContent())
                                .nominal(element.getElementsByTagName("Nominal").item(0).getTextContent())
                                .name(element.getElementsByTagName("Name").item(0).getTextContent())
                                .value(element.getElementsByTagName("Value").item(0).getTextContent())
                                .build();
                        rates.add(currencyRate);

                    }
                }
            }
        } catch (Exception exception) {
            log.error("xml parsing error, xml:{}", ratesAsString, exception);
            throw new CurrencyRateParsingException(exception);

        }

        return rates;
    }
}
