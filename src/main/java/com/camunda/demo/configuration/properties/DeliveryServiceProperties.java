package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryServiceProperties {
    private String host;
    private DeliveryServiceMethodsProperties methods;
    private MockProperties mock;

    @Getter
    @Setter
    public static class DeliveryServiceMethodsProperties {
        private String getDate;
        private String cancellation;
    }

    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}
