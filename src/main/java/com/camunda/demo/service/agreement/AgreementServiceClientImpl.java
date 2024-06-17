package com.camunda.demo.service.agreement;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.SaveAgreementDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AgreementServiceClientImpl implements AgreementServiceClient {
    private final WebClient agreementServiceWebClient;
    private final RestConfigurationProperties properties;

    @Override
    public UUID registrationAgreement(SaveAgreementDto saveAgreementDto) {
        return agreementServiceWebClient
                .post()
                .uri(properties.agreementServiceProperties().getHost() + properties.agreementServiceProperties().getMethods().getRegistrationAgreement())
                .bodyValue(saveAgreementDto)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<UUID>() {})
                .block();
    }

    @Override
    public void deleteAgreement(UUID agreementId) {
        agreementServiceWebClient
                .delete()
                .uri(properties.agreementServiceProperties().getHost() + properties.agreementServiceProperties().getMethods().getDeleteAgreement() + "/" + agreementId)
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe();
    }
}
