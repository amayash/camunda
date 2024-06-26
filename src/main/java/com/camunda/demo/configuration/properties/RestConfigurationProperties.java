package com.camunda.demo.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RestConfigurationProperties {
    @Bean
    @ConfigurationProperties(prefix = "app.rest.agreement-service")
    public AgreementServiceProperties agreementServiceProperties() {
        return new AgreementServiceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.rest.notification-service")
    public NotificationServiceProperties notificationServiceProperties() {
        return new NotificationServiceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.rest.delivery-service")
    public DeliveryServiceProperties deliveryServiceProperties() {
        return new DeliveryServiceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.rest.payment-service")
    public PaymentServiceProperties paymentServiceProperties() {
        return new PaymentServiceProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "app.rest.product-service")
    public ProductServiceProperties productServiceProperties() {
        return new ProductServiceProperties();
    }
}
