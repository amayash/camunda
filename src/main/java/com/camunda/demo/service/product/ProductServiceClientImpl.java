package com.camunda.demo.service.product;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.SaveOrderStatusDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ProductServiceClientImpl implements ProductServiceClient {
    private final WebClient productServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public void changeStatus(UUID id, SaveOrderStatusDto dto) {
        productServiceWebClient
                .patch()
                .uri(properties.productServiceProperties().getHost() + "/orders/" + id + properties.productServiceProperties().getMethods().getChangeStatus())
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
