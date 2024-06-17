package com.camunda.demo.dto;

import com.camunda.demo.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveOrderStatusDto {
    private OrderStatus status;
}
