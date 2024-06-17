package com.camunda.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ConfirmOrderDto {
    private UUID orderId;
    private String orderDeliveryAddress;
    private String customerCRM;
    private String customerAccountNumber;
    private BigDecimal orderPrice;
    private String customerLogin;
}
