package com.camunda.demo.service.notification;

import com.camunda.demo.dto.OrderCancellationDto;
import com.camunda.demo.dto.RequestCancellationDto;

public interface NotificationServiceClient {
    void sendOrderCancellationNotification(OrderCancellationDto dto);

    void sendRequestCancellationNotification(RequestCancellationDto dto);
}
