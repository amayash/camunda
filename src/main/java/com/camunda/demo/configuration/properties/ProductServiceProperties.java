package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductServiceProperties {
    private String host;
    private ProductServiceMethodsProperties methods;
    private MockProperties mock;

    @Getter
    @Setter
    public static class ProductServiceMethodsProperties {
        private String changeStatus;
    }

    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}
