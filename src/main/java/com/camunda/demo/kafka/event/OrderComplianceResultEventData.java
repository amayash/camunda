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
public class OrderComplianceResultEventData implements KafkaEvent {
    @NotBlank(message = "Business key is required")
    private String businessKey;

    @NotBlank(message = "Compliance result is required")
    private Boolean complianceResult;

    @Override
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public Event getEvent() {
        return Event.RESULT_COMPLIANCE;
    }
}
