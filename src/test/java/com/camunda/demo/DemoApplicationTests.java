package com.camunda.demo;

import com.camunda.demo.configuration.properties.RestConfigurationProperties;
import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.kafka.KafkaConsumerHelper;
import com.camunda.demo.kafka.KafkaProducerHelper;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.patch;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
@WireMockTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, topics = {"producer_topic", "consumer_topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9091", "port=9091"})
public class DemoApplicationTests {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private KafkaConsumerHelper kafkaConsumerHelper;

    @Autowired
    private KafkaProducerHelper kafkaProducerHelper;

    @RegisterExtension
    static WireMockExtension wireMockProductServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8080))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockAgreementServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8092))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockPaymentServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8094))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockDeliveryServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(8095))
            .build();

    @Autowired
    private RestConfigurationProperties properties;

    private static final String PROCESS_KEY = "DemoCamundaProcessKey";

    private static final ConfirmOrderDto dto = new ConfirmOrderDto(
            UUID.randomUUID(),
            "address123",
            "CRM123",
            "accountNumber123",
            BigDecimal.valueOf(100),
            "login123");

    @BeforeEach
    void setupWireMockServers() {
        // регистрируем договор
        wireMockAgreementServer.stubFor(post(urlEqualTo(properties.agreementServiceProperties().getMethods().getRegistrationAgreement()))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)));

        // получаем дату доставки
        wireMockDeliveryServer.stubFor(
                post(urlEqualTo(properties.deliveryServiceProperties().getMethods().getGetDate()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)));

        // производим оплату
        wireMockPaymentServer.stubFor(
                post(urlEqualTo(properties.paymentServiceProperties().getMethods().getPayment()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)
                                .withBody("true")));

        // изменяем статус
        wireMockProductServer.stubFor(
                patch(urlEqualTo("/orders/" + dto.getOrderId() + properties.productServiceProperties().getMethods().getChangeStatus()))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", "application/json")
                                .withStatus(200)));
    }

    @Test
    public void test() throws InterruptedException {
        // Запускаем процесс
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("confirmOrderDto", dto)
                .execute();

        String businessKey = processInstance.getBusinessKey();
        assertNotNull(businessKey);
        waitForProcessCompletion(processInstance, processInstance.getId());
    }

    private void waitForProcessCompletion(ProcessInstance instance, String processInstanceId) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60000) {
            runtimeService.createProcessInstanceQuery()
                    .processInstanceId(processInstanceId)
                    .singleResult();
            if (instance == null || instance.isEnded()) {
                return;
            }
            Thread.sleep(1000);
        }
    }
}
