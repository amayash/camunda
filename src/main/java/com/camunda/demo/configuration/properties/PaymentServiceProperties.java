package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentServiceProperties {
    private String host;
    private PaymentServiceMethodsProperties methods;
    private MockProperties mock;

    @Getter
    @Setter
    public static class PaymentServiceMethodsProperties {
        private String payment;
    }

    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}
