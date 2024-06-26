package com.camunda.demo.kafka.handler;

import com.camunda.demo.kafka.event.KafkaEvent;

public interface EventHandler<T extends KafkaEvent> {
    boolean canHandle(KafkaEvent eventSource);

    void handleEvent(T eventSource);
}
