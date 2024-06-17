package com.camunda.demo.dto;

import com.camunda.demo.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrderStatusDto {
    @NotNull(message = "order status is required")
    private OrderStatus status;
}
