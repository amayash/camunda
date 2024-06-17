package com.camunda.demo.delegate;

import com.camunda.demo.service.agreement.AgreementServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class DeleteAgreement implements JavaDelegate {
    private final AgreementServiceClient agreementServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        agreementServiceClient.deleteAgreement((UUID) delegateExecution.getVariable("agreementId"));
    }
}
