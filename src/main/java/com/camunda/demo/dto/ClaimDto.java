package com.camunda.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaimDto {
    @NotBlank(message = "login is required")
    private String login;

    @NotBlank(message = "business key is required")
    private String businessKey;

    @NotBlank(message = "CRM is required")
    private String CRM;
}
