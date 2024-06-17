package com.camunda.demo;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.kafka.KafkaConsumerHelper;
import com.camunda.demo.kafka.KafkaProducerHelper;
import com.camunda.demo.kafka.event.Event;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest()
@EmbeddedKafka(partitions = 1, topics = {"producer_topic", "consumer_topic"}, brokerProperties = {"listeners=PLAINTEXT://localhost:9091", "port=9091"})
@WireMockTest
@DirtiesContext
@Slf4j
public class DemoApplicationTests {
    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private KafkaConsumerHelper kafkaConsumerHelper;

    @Autowired
    private KafkaProducerHelper kafkaProducerHelper;

    @RegisterExtension
    static WireMockExtension wireMockAgreementServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(4321))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockPaymentServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(4322))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockDeliveryServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(4323))
            .build();

    private static final String PROCESS_KEY = "DemoCamundaProcessKey";

    private static final ConfirmOrderDto dto = new ConfirmOrderDto(
            UUID.randomUUID(),
            "address123",
            "CRM123",
            "accountNumber123",
            BigDecimal.valueOf(100),
            "login123");

    private static final UUID agreementId = UUID.randomUUID();
    private static final LocalDate deliveryDate = LocalDate.now().plusDays(7);

    @BeforeEach
    private void setupWireMockServers() {
        // регистрируем договор
        String agreementRegistrationRequest = "{\"crm\":" + dto.getCustomerCRM() +
                ", \"accountNumber\":" + dto.getCustomerAccountNumber() + "}";
//        wireMockAgreementServer.stubFor(
//                post(urlEqualTo("/agreements/registration"))
//                        .withRequestBody(new EqualToPattern(agreementRegistrationRequest))
//                        .willReturn(aResponse()
//                                .withStatus(200)
//                                .withBody(agreementId.toString())));
        wireMockAgreementServer.stubFor(post(urlEqualTo("/agreements/registration"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withStatus(200)
                        .withBody(UUID.randomUUID().toString())));

        // получаем дату доставки
        String deliveryGetDateRequest = "{\"address\":" + dto.getOrderDeliveryAddress() +
                ",\"orderId\":" + dto.getOrderId() + "}";
        wireMockDeliveryServer.stubFor(
                post(urlEqualTo("/deliveries"))
                        //.withRequestBody(new EqualToPattern(deliveryGetDateRequest))
                        .willReturn(aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                //.withStatus(200)
                                .withBody(deliveryDate.toString())));

        // производим оплату
        String paymentPaymentRequest = "{\"orderId\":" + dto.getOrderId() +
                ", \"totalSum\":" + dto.getOrderPrice() +
                ", \"accountNumber\":" + dto.getCustomerAccountNumber() + "}";
        wireMockPaymentServer.stubFor(
                post(urlEqualTo("/payment"))
                        .withRequestBody(new EqualToPattern(paymentPaymentRequest))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(Boolean.valueOf(true).toString())));
    }

    @Test
    public void test() {
        // Запускаем процесс
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("confirmOrderDto", dto)
                .execute();

        String businessKey = processInstance.getBusinessKey();
        assertNotNull(businessKey);
//        String topic = "producer_topic";
//        byte[] testMessageGet = ("{\"login:\"" + dto.getCustomerLogin() +
//                ",\"CRM:\"" + dto.getCustomerCRM() +
//                ",\"businessKey:\"" + businessKey +
//                "}").getBytes(StandardCharsets.UTF_8);
//
//        byte[] testMessageSend = ("{\"businessKey:\"" + businessKey +
//                ",\"complianceResult:\"" + dto.getCustomerCRM() +
//                ",\"event:\"" + Event.RESULT_COMPLIANCE +
//                "}").getBytes(StandardCharsets.UTF_8);

        // Отправка сообщения
        // kafkaProducerHelper.sendMessage(topic, testMessage);

        //assertNotNull(receivedMessage);
        //assertArrayEquals(testMessage, receivedMessage);
        //kafkaConsumerConfig();
    }
}
