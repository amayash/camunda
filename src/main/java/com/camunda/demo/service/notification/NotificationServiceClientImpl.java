package com.camunda.demo.service.notification;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.OrderCancellationDto;
import com.camunda.demo.dto.RequestCancellationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class NotificationServiceClientImpl implements NotificationServiceClient {
    private final WebClient notificationServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public void sendOrderCancellationNotification(OrderCancellationDto order) {
        notificationServiceWebClient
                .post()
                .uri(properties.notificationServiceProperties().getMethods().getOrderCancellation())
                .bodyValue(order)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }

    @Override
    public void sendRequestCancellationNotification(RequestCancellationDto dto) {
        notificationServiceWebClient
                .post()
                .uri(properties.notificationServiceProperties().getMethods().getRequestCancellation())
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
