package com.camunda.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveDeliveryDto {
    @NotBlank(message = "address is required")
    private String address;

    @NotNull(message = "order id is required")
    private UUID orderId;
}
