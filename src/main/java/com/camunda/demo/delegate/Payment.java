package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.SavePaymentDto;
import com.camunda.demo.service.payment.PaymentServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Payment implements JavaDelegate {
    private final PaymentServiceClient paymentServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        log.info("ConfirmOrderDto from payment: {}", dto);
        Boolean result = paymentServiceClient.payment(new SavePaymentDto(dto.getOrderId(), dto.getOrderPrice(),
                dto.getCustomerAccountNumber()));
        log.info("Payment result: {}", result);
        delegateExecution.setVariable("orderCancelled", !result);
    }
}
