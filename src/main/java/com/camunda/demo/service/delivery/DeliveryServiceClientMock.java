package com.camunda.demo.service.delivery;

import com.camunda.demo.dto.SaveDeliveryDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Random;
import java.util.UUID;

@Primary
@ConditionalOnProperty(name = "app.rest.delivery-service.mock.enabled")
@Component
@Slf4j
public class DeliveryServiceClientMock implements DeliveryServiceClient {
    @Override
    public LocalDate getDate(SaveDeliveryDto deliveryDto) {
        Random random = new Random();
        int chance = random.nextInt(3);
        if (chance == 0) {
            throw new RuntimeException("Ошибка при оформлении доставки");
        }
        return LocalDate.now().plusDays(7);
    }

    @Override
    public void cancellation(UUID orderId) {
        log.info("Delivery cancellation ...");
    }
}
