package com.camunda.demo.kafka.sender;

import com.camunda.demo.dto.ClaimDto;
import com.camunda.demo.kafka.Producer;
import com.camunda.demo.kafka.event.OrderCheckBusinessKeyEventData;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCheckBusinessKeyEventSender implements EventSender {
    private final Producer producer;

    @Override
    public void sendEvent(final ClaimDto data, final String processId) {
        final OrderCheckBusinessKeyEventData orderCheckBusinessKeyEventData =
                new OrderCheckBusinessKeyEventData(data.getLogin(), data.getBusinessKey(), data.getCRM());
        try {
            producer.sendEvent(Producer.PRODUCER_TOPIC, processId, orderCheckBusinessKeyEventData);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
