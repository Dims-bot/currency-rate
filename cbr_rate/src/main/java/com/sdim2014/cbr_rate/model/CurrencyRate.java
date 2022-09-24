package com.sdim2014.cbr_rate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@AllArgsConstructor(onConstructor = @__(@JsonCreator))
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CurrencyRate {
    String numCode;
    String charCode;
    String nominal;
    String name;
    String value;

}
