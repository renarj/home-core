package com.oberasoftware.home.core.edge;

import com.oberasoftware.home.activemq.ActiveMQConfiguration;
import com.oberasoftware.home.activemq.ActiveMQTopicListener;
import com.oberasoftware.home.activemq.ActiveMQTopicSender;
import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import com.oberasoftware.home.core.mqtt.MQTTTopicEventBus;
import com.oberasoftware.robo.api.commands.BasicCommand;
import com.oberasoftware.robo.core.model.BasicCommandImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

import static com.oberasoftware.home.util.ConverterHelper.mapFromJson;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@ComponentScan
@Import({MQTTConfiguration.class, ActiveMQConfiguration.class})
public class EdgeProcessorContainer {
    private static final Logger LOG = LoggerFactory.getLogger(EdgeProcessorContainer.class);

    public static void main(String[] args) {
        LOG.info("Starting edge processor");

        ApplicationContext context = SpringApplication.run(EdgeProcessorContainer.class);
        MQTTTopicEventBus topicEventBus = context.getBean(MQTTTopicEventBus.class);
        MQTTMessageListener messageListener = context.getBean(MQTTMessageListener.class);
        ActiveMQTopicSender topicSender = context.getBean(ActiveMQTopicSender.class);
        ActiveMQTopicListener topicListener = context.getBean(ActiveMQTopicListener.class);

        LOG.info("Connecting to command channel");
        topicEventBus.connect();
        topicListener.register(message -> {
            BasicCommand basicCommand = mapFromJson(message, BasicCommandImpl.class);
            LOG.info("Received basic command: {}", basicCommand);
            topicEventBus.publish(basicCommand);
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
