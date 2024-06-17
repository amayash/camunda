package com.camunda.demo.kafka;

import com.camunda.demo.kafka.event.OrderComplianceResultEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class KafkaProducerHelper {
    @Autowired
    private KafkaTemplate<String, byte[]> kafkaTemplate;

    public void sendEvent(final String topic, final String key, final OrderComplianceResultEventData event) throws JsonProcessingException {
        final ObjectMapper objectMapper = new ObjectMapper();
        final byte[] data = objectMapper.writeValueAsBytes(event);

        CompletableFuture<SendResult<String, byte[]>> future = kafkaTemplate.send(topic, key, data);

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Kafka send complete: {}", result);
            } else {
                log.error("Kafka fail send", ex);
            }
        });
    }
}
