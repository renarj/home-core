package com.oberasoftware.home.command;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.commands.BasicCommand;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.oberasoftware.home.util.ConverterHelper.mapToJson;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author renarj
 */
@Component
public class BasicCommandHandler implements EventHandler {
    private static final Logger LOG = getLogger(BasicCommandHandler.class);

    @Autowired
    private KafkaTopicSender topicSender;

    @EventSubscribe
    public void receive(BasicCommand basicCommand) {
        LOG.info("Received a basic command: {}", basicCommand);

        topicSender.publish(mapToJson(basicCommand));
    }
}
