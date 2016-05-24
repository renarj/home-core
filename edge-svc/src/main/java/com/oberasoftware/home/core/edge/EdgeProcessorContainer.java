package com.oberasoftware.home.core.edge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.model.BasicCommandImpl;
import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.home.kafka.KafkaConfiguration;
import com.oberasoftware.home.kafka.KafkaTopicListener;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@ComponentScan
@Import({MQTTConfiguration.class, KafkaConfiguration.class})
public class EdgeProcessorContainer {
    private static final Logger LOG = LoggerFactory.getLogger(EdgeProcessorContainer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    public static void main(String[] args) {
        LOG.info("Starting edge processor");

        ApplicationContext context = SpringApplication.run(EdgeProcessorContainer.class);
        MQTTTopicEventBus topicEventBus = context.getBean(MQTTTopicEventBus.class);
        MQTTMessageListener messageListener = context.getBean(MQTTMessageListener.class);
        KafkaTopicSender topicSender = context.getBean(KafkaTopicSender.class);
        KafkaTopicListener topicListener = context.getBean(KafkaTopicListener.class);

        LOG.info("Connecting to command channel");
        topicEventBus.connect();
        topicListener.register(message -> {
            try {
                BasicCommand basicCommand = OBJECT_MAPPER.readValue(message, BasicCommandImpl.class);
                LOG.info("Received basic command: {}", basicCommand);
                topicEventBus.publish(basicCommand);
            } catch (IOException e) {
                LOG.error("", e);
            }
        });
        topicListener.connect();

        LOG.info("Connecting to topic sender for forwarding states");
        topicSender.connect();

        LOG.info("Starting listening to state changes on MQTT");
        topicEventBus.registerHandler(messageListener);
        topicEventBus.subscribe("/states/#");

        LOG.info("Edge Processor started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the kafka gracefully on shutdown");
            topicSender.close();
        }));
    }
}