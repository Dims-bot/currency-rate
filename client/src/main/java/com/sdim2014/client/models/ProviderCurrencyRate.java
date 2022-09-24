package com.sdim2014.client.models;

public enum ProviderCurrencyRate {
    CBR("cbr");

    String serviceName;


    ProviderCurrencyRate(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceName() {
        return serviceName;
    }
}
