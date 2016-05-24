package com.oberasoftware.home.core.edge;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class BasicCommandConverter implements Converter {
    private static final Logger LOG = LoggerFactory.getLogger(BasicCommandConverter.class);

    private static final String TOPIC_PATH = "/commands/%s/%s/%s";

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @TypeConverter
    public MQTTMessage convert(BasicCommand basicCommand) {
        String topic = String.format(TOPIC_PATH, basicCommand.getControllerId(), basicCommand.getItemId(), basicCommand.getCommandType());
        LOG.info("Forward basic command: {} to topic: {}", basicCommand, topic);
        try {
            return new MQTTMessageImpl(topic, OBJECT_MAPPER.writeValueAsString(basicCommand));
        } catch (JsonProcessingException e) {
            LOG.error("", e);
            throw new RuntimeHomeAutomationException("Could not convert", e);
        }
    }
}
