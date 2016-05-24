package com.oberasoftware.home.command;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class BasicCommandHandler implements EventHandler {
    private static final Logger LOG = getLogger(BasicCommandHandler.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Autowired
    private KafkaTopicSender topicSender;

    @EventSubscribe
    public void receive(BasicCommand basicCommand) {
        LOG.info("Received a basic command: {}", basicCommand);

        try {
            topicSender.publish(OBJECT_MAPPER.writeValueAsString(basicCommand));
        } catch (JsonProcessingException e) {
            LOG.error("Could not write command", e);
        }
    }
}
