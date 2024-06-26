package com.camunda.demo.service.agreement;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.SaveAgreementDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class AgreementServiceClientImpl implements AgreementServiceClient {
    private final WebClient agreementServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public UUID registrationAgreement(SaveAgreementDto saveAgreementDto) {
        log.info("Impl contract service is used");
        return agreementServiceWebClient
                .post()
                .uri(properties.agreementServiceProperties().getMethods().getRegistrationAgreement())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(saveAgreementDto)
                .retrieve()
                .bodyToMono(UUID.class)
                .block();
    }

    @Override
    public void deleteAgreement(UUID agreementId) {
        agreementServiceWebClient
                .delete()
                .uri(properties.agreementServiceProperties().getMethods().getDeleteAgreement() + "/" + agreementId)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
