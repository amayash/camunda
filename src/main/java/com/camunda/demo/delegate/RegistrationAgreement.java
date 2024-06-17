package com.camunda.demo.delegate;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.dto.SaveAgreementDto;
import com.camunda.demo.service.agreement.AgreementServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.BpmnError;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegistrationAgreement implements JavaDelegate {
    private final AgreementServiceClient agreementServiceClient;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        ConfirmOrderDto dto = (ConfirmOrderDto) delegateExecution.getVariable("confirmOrderDto");
        log.info("ConfirmOrderDto from registration agreement: {}", dto);
        try {
            delegateExecution.setVariable("agreementId",
                    agreementServiceClient.registrationAgreement(
                            new SaveAgreementDto(
                                    dto.getCustomerAccountNumber(),
                                    dto.getCustomerCRM()
                            )));
        } catch (Exception e) {
            log.error("Error while saving agreement: {}", e.getMessage());
            throw new BpmnError("errorEventId");
        }
    }
}
