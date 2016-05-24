package com.oberasoftware.home.core.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.ValueTransportMessage;
import com.oberasoftware.home.kafka.KafkaConfiguration;
import com.oberasoftware.home.kafka.KafkaTopicListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@Import({StateConfiguration.class, BaseConfiguration.class, KafkaConfiguration.class})
public class StateContainer {
    private static final Logger LOG = LoggerFactory.getLogger(StateContainer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    public static void main(String[] args) {
        LOG.info("Starting state service");
        ApplicationContext context = SpringApplication.run(StateContainer.class);
        KafkaTopicListener topicListener = context.getBean(KafkaTopicListener.class);
        topicListener.connect();
        StateManager stateManager = context.getBean(StateManager.class);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the kafka gracefully on shutdown");
            topicListener.close();
        }));


        topicListener.register(received -> {
            try {
                ValueTransportMessage message = OBJECT_MAPPER.readValue(received, ValueTransportMessage.class);
                LOG.debug("Received value: {}", message);
                stateManager.setState(message.getControllerId(),
                        message.getChannelId(), message.getLabel(), message.getValue());
            } catch (IOException e) {
                LOG.error("Could not read message", e);
            } catch(Exception ex) {
                LOG.error("Something happened", ex);
            }
        });

    }
}
