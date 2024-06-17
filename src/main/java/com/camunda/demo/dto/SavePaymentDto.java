package com.camunda.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SavePaymentDto {
    @NotNull(message = "order id is required")
    private UUID orderId;

    @NotNull(message = "total sum is required")
    @Positive
    private BigDecimal totalSum;

    @NotBlank(message = "account number is required")
    private String accountNumber;
}
