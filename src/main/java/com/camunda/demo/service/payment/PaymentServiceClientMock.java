package com.camunda.demo.service.payment;

import com.camunda.demo.dto.SavePaymentDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Random;

@Primary
@ConditionalOnProperty(name = "app.rest.payment-service.mock.enabled")
@Component
@Slf4j
public class PaymentServiceClientMock implements PaymentServiceClient {
    @Override
    public Boolean payment(SavePaymentDto deliveryDto) {
        Random random = new Random();
        int chance = random.nextInt(3);
        return chance != 0;
    }
}
