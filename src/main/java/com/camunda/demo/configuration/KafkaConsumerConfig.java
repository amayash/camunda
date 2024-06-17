package com.camunda.demo.configuration;

import com.camunda.demo.configuration.properties.KafkaProperties;
import javassist.bytecode.ByteArray;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@ConditionalOnProperty(prefix = "app", name = "kafka.enabled")
@RequiredArgsConstructor
public class KafkaConsumerConfig {
    private final KafkaProperties kafkaProperties;

    private ConsumerFactory<String, ByteArray> consumerFactoryString() {
        Map<String, Object> props = new HashMap<>();
        // Установка адресов серверов Kafka, к которым потребитель будет подключаться
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getBootstrapAddress());
        // Установка стратегии автоматического сброса смещения (начинать с самого начала, если смещение не найдено)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        // Установка идентификатора группы потребителей
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaProperties.getGroupId());
        // Установка десериализатора для ключей сообщений (здесь используется десериализатор для строк)
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // Установка десериализатора для значений сообщений (здесь используется десериализатор для массива байтов)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ByteArray> kafkaListenerContainerFactoryString() {
        ConcurrentKafkaListenerContainerFactory<String, ByteArray> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactoryString());
        return factory;
    }
}
