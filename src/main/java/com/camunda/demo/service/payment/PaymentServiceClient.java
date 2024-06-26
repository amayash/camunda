package com.camunda.demo.service.payment;

import com.camunda.demo.dto.SavePaymentDto;

public interface PaymentServiceClient {
    Boolean payment(SavePaymentDto dto);
}
