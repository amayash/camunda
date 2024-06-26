package com.camunda.demo.service.product;

import com.camunda.demo.dto.SaveOrderStatusDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Primary
@ConditionalOnProperty(name = "app.rest.product-service.mock.enabled")
@Component
@Slf4j
public class ProductServiceClientMock implements ProductServiceClient {
    @Override
    public void changeStatus(UUID id, SaveOrderStatusDto dto) {
        log.info("Order status changed to {}", dto.getStatus());
    }
}
