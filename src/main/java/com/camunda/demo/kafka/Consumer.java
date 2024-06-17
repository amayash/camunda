package com.camunda.demo.kafka;

import com.camunda.demo.kafka.event.KafkaEvent;
import com.camunda.demo.kafka.handler.EventHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
public class Consumer {
    private final Set<EventHandler<KafkaEvent>> eventHandlers;
    public static final String CONSUMER_TOPIC = "consumer_topic";

    @KafkaListener(topics = CONSUMER_TOPIC, containerFactory = "kafkaListenerContainerFactoryString")
    public void listenGroupTopic(byte[] message) throws IOException {
        log.info("Receive message: {}", new String(message));

        final ObjectMapper objectMapper = new ObjectMapper();

        try {
            final KafkaEvent eventSource = objectMapper.readValue(message, KafkaEvent.class);
            log.info("EventSource: {}", eventSource);

            eventHandlers.stream()
                    .filter(eventSourceEventHandler -> eventSourceEventHandler.canHandle(eventSource))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Handler for eventsource not found"))
                    .handleEvent(eventSource);

        } catch (JsonProcessingException e) {
            log.error("Couldn't parse message: {}; exception: ", new String(message), e);
        }
    }
}
