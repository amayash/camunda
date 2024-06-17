package com.camunda.demo.service.delivery;

import com.camunda.demo.dto.SaveDeliveryDto;

import java.time.LocalDate;
import java.util.UUID;

public interface DeliveryServiceClient {
    LocalDate getDate(SaveDeliveryDto deliveryDto);

    void cancellation(UUID orderId);
}
