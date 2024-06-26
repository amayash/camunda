package com.camunda.demo.delegate;

import com.camunda.demo.dto.ClaimDto;
import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.kafka.sender.OrderCheckBusinessKeyEventSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Compliance implements JavaDelegate {
    private final OrderCheckBusinessKeyEventSender sender;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        log.info("Executing delegateExecution");
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        String businessKey = delegateExecution.getBusinessKey();

        sender.sendEvent(ClaimDto.builder()
                .login(dto.getCustomerLogin())
                .CRM(dto.getCustomerCRM())
                .businessKey(businessKey)
                .build(), businessKey);
    }
}
