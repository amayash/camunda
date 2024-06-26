package com.camunda.demo.delegate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Return implements JavaDelegate {
    private final RuntimeService runtimeService;

    @Override
    public void execute(DelegateExecution delegateExecution) {
        runtimeService.createProcessInstanceModification(delegateExecution.getProcessInstanceId())
                .startAfterActivity("PaymentId")
                .setVariable("orderCancelled", true)
                .execute();
    }
}
