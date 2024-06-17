package com.camunda.demo.kafka.sender;

import com.camunda.demo.dto.ClaimDto;
import com.camunda.demo.kafka.event.Event;

public interface EventSender {
    boolean canSend(final Event orderEventType);

    void sendEvent(final ClaimDto data, final String processId);
}

