package com.oberasoftware.home.core.edge;

import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@ComponentScan
@Import(MQTTConfiguration.class)
public class EdgeProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(EdgeProcessor.class);

    public static void main(String[] args) {
        LOG.info("Starting edge processor");

        ApplicationContext context = SpringApplication.run(EdgeProcessor.class);
        MQTTTopicEventBus topicEventBus = context.getBean(MQTTTopicEventBus.class);
        MQTTMessageListener messageListener = context.getBean(MQTTMessageListener.class);
        KafkaTopicSender topicSender = context.getBean(KafkaTopicSender.class);

        LOG.info("Connecting to topic sender");
        topicSender.connect();

        LOG.info("Starting listening to state changes on MQTT");
        topicEventBus.connect();
        topicEventBus.registerHandler(messageListener);
        topicEventBus.subscribe("/states/#");


        LOG.info("Edge Processor started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the kafka gracefully on shutdown");
            topicSender.close();
        }));
    }
}
