package com.camunda.demo.kafka.sender;

import com.camunda.demo.dto.ClaimDto;

public interface EventSender {
    void sendEvent(final ClaimDto data, final String processId);
}

