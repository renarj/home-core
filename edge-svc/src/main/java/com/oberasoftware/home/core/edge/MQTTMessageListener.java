package com.oberasoftware.home.core.edge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.model.ValueTransportMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTPathParser;
import com.oberasoftware.home.core.mqtt.ParsedPath;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTMessageListener implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTMessageListener.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Autowired
    private KafkaTopicSender topicSender;

    @EventSubscribe
    public void receive(MQTTMessage message) {
        LOG.debug("Received a MQTT message: {}", message);
        ParsedPath parsedPath = MQTTPathParser.parsePath(message.getTopic());
        try {
            ValueTransportMessage parsedMessage = OBJECT_MAPPER.readValue(message.getMessage(), ValueTransportMessage.class);
            if(validateMessage(parsedPath, parsedMessage)) {
                LOG.debug("Message is valid, forwarding to Kafka: {}", message.getMessage());

                topicSender.publish(message.getMessage());
            }
        } catch (IOException e) {
            LOG.error("", e);
        }

    }

    private boolean validateMessage(ParsedPath path, ValueTransportMessage value) {
        //TODO: Implement proper validation to prevent illegal messages etc.
        return true;
    }
}
