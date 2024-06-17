package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.SaveDeliveryDto;
import com.camunda.demo.service.delivery.DeliveryServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Delivery implements JavaDelegate {
    private final DeliveryServiceClient deliveryServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        try {
            ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
            log.info("ConfirmOrderDto from delivery: {}", dto);
            delegateExecution.setVariable("orderDeliveryDate",
                    deliveryServiceClient.getDate(new SaveDeliveryDto(dto.getOrderDeliveryAddress(), dto.getOrderId())));
        } catch (Exception e) {
            throw new BpmnError("errorEventId");
        }
    }
}
