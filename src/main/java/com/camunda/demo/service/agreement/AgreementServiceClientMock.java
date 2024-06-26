package com.camunda.demo.service.agreement;

import com.camunda.demo.dto.SaveAgreementDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.UUID;

@Primary
@ConditionalOnProperty(name = "app.rest.agreement-service.mock.enabled")
@Component
@Slf4j
public class AgreementServiceClientMock implements AgreementServiceClient {
    @Override
    public UUID registrationAgreement(SaveAgreementDto saveAgreementDto) {
        Random random = new Random();
        int chance = random.nextInt(3);
        if (chance == 0) {
            throw new RuntimeException("Ошибка при создании договора");
        }
        return UUID.randomUUID();
    }

    @Override
    public void deleteAgreement(UUID agreementId) {
        log.info("Deleting agreement ...");
    }
}
