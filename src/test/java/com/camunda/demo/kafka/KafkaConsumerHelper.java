package com.camunda.demo.kafka;

import com.camunda.demo.kafka.event.OrderCheckBusinessKeyEventData;
import com.camunda.demo.kafka.event.OrderComplianceResultEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerHelper {
    private final KafkaProducerHelper kafkaProducerHelper;

    @KafkaListener(topics = Producer.PRODUCER_TOPIC, containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupTopic(byte[] message) throws IOException {
        log.info("Receive message: {}", new String(message));

        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final OrderCheckBusinessKeyEventData eventSource = objectMapper.readValue(message, OrderCheckBusinessKeyEventData.class);
            log.info("EventSource: {}", eventSource);
            String businessKey = eventSource.getBusinessKey();
            kafkaProducerHelper.sendEvent(Consumer.CONSUMER_TOPIC, businessKey,
                    new OrderComplianceResultEventData(businessKey, true));
        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message: {}; exception: ", new String(message), e);
        }
    }
}
