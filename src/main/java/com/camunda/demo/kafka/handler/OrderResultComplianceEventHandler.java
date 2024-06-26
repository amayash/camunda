package com.camunda.demo.kafka.handler;

import com.camunda.demo.kafka.event.Event;
import com.camunda.demo.kafka.event.KafkaEvent;
import com.camunda.demo.kafka.event.OrderComplianceResultEventData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderResultComplianceEventHandler implements EventHandler<OrderComplianceResultEventData> {
    private final RuntimeService runtimeService;

    @Override
    public boolean canHandle(KafkaEvent eventSource) {
        return Event.RESULT_COMPLIANCE.equals(eventSource.getEvent());
    }

    @Override
    public void handleEvent(OrderComplianceResultEventData eventSource) {
        log.info("Order result compliance event: {}", eventSource);
        runtimeService.createMessageCorrelation("continueProcessMsg")
                .processInstanceBusinessKey(eventSource.getBusinessKey())
                .setVariable("complianceResultFailed", !eventSource.getComplianceResult())
                .correlate();
    }
}
