package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.SaveOrderStatusDto;
import com.camunda.demo.enums.OrderStatus;
import com.camunda.demo.service.product.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RejectOrder implements JavaDelegate {
    private final ProductServiceClient productServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        productServiceClient.changeStatus(dto.getOrderId(), new SaveOrderStatusDto(OrderStatus.REJECTED));
    }
}
