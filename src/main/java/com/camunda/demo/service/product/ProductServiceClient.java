package com.camunda.demo.service.product;

import com.camunda.demo.dto.SaveOrderStatusDto;

import java.util.UUID;

public interface ProductServiceClient {
    void changeStatus(UUID orderId, SaveOrderStatusDto dto);
}
