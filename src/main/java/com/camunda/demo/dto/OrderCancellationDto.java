package com.camunda.demo.dto;

import com.camunda.demo.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCancellationDto {
    private UUID orderId;
    private OrderStatus status;
}
