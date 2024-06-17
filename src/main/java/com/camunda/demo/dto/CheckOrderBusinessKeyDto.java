package com.camunda.demo.dto;

import com.camunda.demo.kafka.event.Event;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CheckOrderBusinessKeyDto {
    private Event event;
    private String processId;
    private ClaimDto data;

    @JsonCreator
    public CheckOrderBusinessKeyDto(@JsonProperty("event") Event event,
                                    @JsonProperty("processId") String processId,
                                    @JsonProperty("login") String login,
                                    @JsonProperty("businessKey") String businessKey,
                                    @JsonProperty("CRM") String CRM) {
        this.event = event;
        this.processId = processId;
        this.data = ClaimDto.builder()
                .login(login)
                .businessKey(businessKey)
                .CRM(CRM)
                .build();
    }
}
