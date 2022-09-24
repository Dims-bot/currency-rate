package com.sdim2014.cbr_rate.parser;

import com.sdim2014.cbr_rate.model.CurrencyRate;

import java.util.List;

public interface CurrencyRateParser {
    List<CurrencyRate> parse(String ratesAsString);
}
