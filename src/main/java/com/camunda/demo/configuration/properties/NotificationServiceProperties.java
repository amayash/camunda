package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationServiceProperties {
    private String host;
    private NotificationServiceMethodsProperties methods;
    private MockProperties mock;

    @Getter
    @Setter
    public static class NotificationServiceMethodsProperties {
        private String orderCancellation;
        private String requestCancellation;
    }

    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}
