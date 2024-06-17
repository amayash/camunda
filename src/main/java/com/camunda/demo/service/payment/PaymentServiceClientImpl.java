package com.camunda.demo.service.payment;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.SavePaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Service
public class PaymentServiceClientImpl implements PaymentServiceClient {
    private final WebClient paymentServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public Boolean payment(SavePaymentDto dto) {
        return paymentServiceWebClient
                .post()
                .uri(properties.paymentServiceProperties().getHost() + properties.paymentServiceProperties().getMethods().getPayment())
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Boolean>() {})
                .block();
    }
}
