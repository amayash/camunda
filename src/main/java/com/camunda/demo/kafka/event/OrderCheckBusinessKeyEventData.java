package com.camunda.demo.kafka.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
public class OrderCheckBusinessKeyEventData implements KafkaEvent {
    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "Business key is required")
    private String businessKey;

    @NotBlank(message = "CRM is required")
    private String CRM;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Event getEvent() {
        return Event.CHECK_ORDER_BUSINESS_KEY;
    }
}
