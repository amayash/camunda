package com.camunda.demo.controller;

import com.camunda.demo.dto.ConfirmOrderDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ProcessController {
    private final RuntimeService runtimeService;
    private static final String PROCESS_KEY = "DemoCamundaProcessKey";

    @PostMapping("/confirm-order")
    public String confirmOrder(@RequestBody ConfirmOrderDto dto) {
        log.info("ConfirmOrderDto: {}", dto);
        return runtimeService
                .createProcessInstanceByKey(PROCESS_KEY)
                .businessKey(UUID.randomUUID().toString())
                .setVariable("confirmOrderDto", dto)
                .execute()
                .getBusinessKey();
    }
}
