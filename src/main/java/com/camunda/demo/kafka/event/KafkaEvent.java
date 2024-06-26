package com.camunda.demo.kafka.event;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "event"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OrderCheckBusinessKeyEventData.class, name = "CHECK_ORDER_BUSINESS_KEY"),
        @JsonSubTypes.Type(value = OrderComplianceResultEventData.class, name = "RESULT_COMPLIANCE"),
})
public interface KafkaEvent {
    Event getEvent();
}