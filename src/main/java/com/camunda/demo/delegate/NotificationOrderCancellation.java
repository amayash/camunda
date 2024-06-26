package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.OrderCancellationDto;
import com.camunda.demo.enums.OrderStatus;
import com.camunda.demo.service.notification.NotificationServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotificationOrderCancellation implements JavaDelegate {
    private final NotificationServiceClient notificationServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        notificationServiceClient.sendOrderCancellationNotification(new OrderCancellationDto(dto.getOrderId(), OrderStatus.CANCELLED));
    }
}
