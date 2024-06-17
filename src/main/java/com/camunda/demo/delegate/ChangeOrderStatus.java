package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.SaveOrderStatusDto;
import com.camunda.demo.enums.OrderStatus;
import com.camunda.demo.service.product.ProductServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChangeOrderStatus implements JavaDelegate {
    private final ProductServiceClient productServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        if ((Boolean) delegateExecution.getVariable("orderCancelled"))
            productServiceClient.changeStatus(dto.getOrderId(), new SaveOrderStatusDto(OrderStatus.CANCELLED));
        else {
            productServiceClient.changeStatus(dto.getOrderId(), new SaveOrderStatusDto(OrderStatus.CONFIRMED));
            log.info("ConfirmOrderDto from change order status to confirmed: {}", dto);
        }
    }
}
