package com.camunda.demo.service.notification;

import com.camunda.demo.dto.OrderCancellationDto;
import com.camunda.demo.dto.RequestCancellationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@ConditionalOnProperty(name = "app.rest.notification-service.mock.enabled")
@Component
@Slf4j
public class NotificationServiceClientMock implements NotificationServiceClient {
    @Override
    public void sendOrderCancellationNotification(OrderCancellationDto order) {
        log.info("sendOrderCancellationNotification send notification ...");
    }

    @Override
    public void sendRequestCancellationNotification(RequestCancellationDto dto) {
        log.info("sendRequestCancellationNotification send notification ...");
    }
}
