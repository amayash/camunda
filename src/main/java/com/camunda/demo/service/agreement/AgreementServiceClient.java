package com.camunda.demo.service.agreement;

import com.camunda.demo.dto.SaveAgreementDto;

import java.util.UUID;

public interface AgreementServiceClient {
    UUID registrationAgreement(SaveAgreementDto saveAgreementDto);

    void deleteAgreement(UUID agreementId);
}
