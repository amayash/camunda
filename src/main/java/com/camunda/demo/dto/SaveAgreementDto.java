package com.camunda.demo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaveAgreementDto {
    @NotBlank(message = "CRM is required")
    private String CRM;

    @NotBlank(message = "Account number is required")
    private String accountNumber;
}
