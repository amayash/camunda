package com.camunda.demo.service.delivery;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.SaveDeliveryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeliveryServiceClientImpl implements DeliveryServiceClient {
    private final WebClient deliveryServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public LocalDate getDate(SaveDeliveryDto deliveryDto) {
        return deliveryServiceWebClient
                .post()
                .uri(properties.deliveryServiceProperties().getHost() + properties.deliveryServiceProperties().getMethods().getGetDate())
                .bodyValue(deliveryDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<LocalDate>() {})
                .block();
    }

    @Override
    public void cancellation(UUID uuid) {
        deliveryServiceWebClient
                .delete()
                .uri(properties.deliveryServiceProperties().getHost() + properties.deliveryServiceProperties().getMethods().getCancellation() + "/" + uuid)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
