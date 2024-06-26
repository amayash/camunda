package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AgreementServiceProperties {
    private String host;
    private AgreementServiceMethodsProperties methods;
    private MockProperties mock;

    @Getter
    @Setter
    public static class AgreementServiceMethodsProperties {
        private String registrationAgreement;
        private String deleteAgreement;
    }

    @Getter
    @Setter
    public static class MockProperties {
        private boolean enabled;
    }
}
