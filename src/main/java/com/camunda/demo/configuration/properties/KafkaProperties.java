package com.camunda.demo.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "app.kafka")
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
@Getter
@Setter
public class KafkaProperties {
    private String bootstrapAddress;
    private String groupId;
}
