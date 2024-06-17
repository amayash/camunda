package com.camunda.demo;

import com.camunda.demo.dto.ConfirmOrderDto;
import com.camunda.demo.kafka.KafkaConsumerHelper;
import com.camunda.demo.kafka.KafkaProducerHelper;
import com.camunda.demo.kafka.event.Event;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
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
@SpringBootTest
@WireMockTest
@DirtiesContext
@EnableKafka
public class DemoApplicationTests {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    KafkaConsumerHelper kafkaConsumerHelper;
    @Autowired
    KafkaProducerHelper kafkaProducerHelper;

    @RegisterExtension
    static WireMockExtension wireMockAgreementServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9099))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockPaymentServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9098))
            .build();

    @RegisterExtension
    static WireMockExtension wireMockDeliveryServer = WireMockExtension.newInstance()
            .options(wireMockConfig().port(9097))
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

    @Test
    public void test() throws IOException, InterruptedException {
        // Запускаем процесс
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("confirmOrderDto", dto)
                .execute();

        String businessKey = processInstance.getBusinessKey();
        assertNotNull(businessKey);
        setupWireMockServers();
        String topic = "producer_topic";
        byte[] testMessageGet = ("{\"login:\"" + dto.getCustomerLogin() +
                ",\"CRM:\"" + dto.getCustomerCRM() +
                ",\"businessKey:\"" + businessKey +
                "}").getBytes(StandardCharsets.UTF_8);

        byte[] testMessageSend = ("{\"businessKey:\"" + businessKey +
                ",\"complianceResult:\"" + dto.getCustomerCRM() +
                ",\"event:\"" + Event.RESULT_COMPLIANCE +
                "}").getBytes(StandardCharsets.UTF_8);

        // Отправка сообщения
        // kafkaProducerHelper.sendMessage(topic, testMessage);

        //assertNotNull(receivedMessage);
        //assertArrayEquals(testMessage, receivedMessage);
        //kafkaConsumerConfig();
    }

    /*
        private void kafkaConsumerConfig() throws InterruptedException, IOException {
            // Настройка Consumer для тестирования
            Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("group1", "true", embeddedKafkaBroker);
            consumerProps.put("key.deserializer", StringDeserializer.class);
            consumerProps.put("value.deserializer", ByteArrayDeserializer.class);
            ConsumerFactory<String, byte[]> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
            ContainerProperties containerProperties = new ContainerProperties(Producer.PRODUCER_TOPIC);
            KafkaMessageListenerContainer<String, byte[]> messageListenerContainer = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

            final ObjectMapper objectMapper = new ObjectMapper();
            BlockingQueue<ConsumerRecord<String, byte[]>> records = new LinkedBlockingQueue<>();
            messageListenerContainer.setupMessageListener((MessageListener<String, byte[]>) record -> {
                try {
                    System.out.println("Received message: " + objectMapper.readValue(record.value(), OrderCheckBusinessKeyEventData.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                records.add(record);
            });

            // Запуск Consumer
            messageListenerContainer.start();
            ContainerTestUtils.waitForAssignment(messageListenerContainer, embeddedKafkaBroker.getPartitionsPerTopic());

            // Проверка полученного сообщения
            ConsumerRecord<String, byte[]> received = records.poll(10, TimeUnit.SECONDS);
            assertThat(received).isNotNull();
            assert received != null;

            assertThat(objectMapper.readValue(received.value(), OrderCheckBusinessKeyEventData.class).getLogin()).isEqualTo(dto.getCustomerLogin());
        }
    */
    private void setupWireMockServers() {
        // регистрируем договор
        String agreementRegistrationRequest = "{\"crm\":" + dto.getCustomerCRM() +
                ", \"accountNumber\":" + dto.getCustomerAccountNumber() + "}";
        wireMockAgreementServer.stubFor(
                post(urlEqualTo("/agreements/registration"))
                        .withRequestBody(new EqualToPattern(agreementRegistrationRequest))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(agreementId.toString())));

        // получаем дату доставки
        String deliveryGetDateRequest = "{\"address\":" + dto.getOrderDeliveryAddress() +
                ",\"orderId\":" + dto.getOrderId() + "}";
        wireMockDeliveryServer.stubFor(
                post(urlEqualTo("/deliveries"))
                        .withRequestBody(new EqualToPattern(deliveryGetDateRequest))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(deliveryDate.toString())));

        // производим оплату
        String paymentPaymentRequest = "{\"orderId\":" + dto.getOrderId() +
                ", \"totalSum\":" + dto.getOrderPrice() +
                ", \"accountNumber\":" + dto.getCustomerAccountNumber() + "}";
        wireMockAgreementServer.stubFor(
                post(urlEqualTo("/payment"))
                        .withRequestBody(new EqualToPattern(paymentPaymentRequest))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withBody(Boolean.valueOf(true).toString())));
    }
}
