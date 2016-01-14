package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class TestListener implements EventHandler {

    private String lastTopic = null;
    private String lastMessage = null;

    @EventSubscribe
    public void receive(MQTTMessage mqttMessage) {
        this.lastTopic = mqttMessage.getTopic();
        this.lastMessage = mqttMessage.getMessage();
    }

    public String getLastTopic() {
        return lastTopic;
    }

    public String getLastMessage() {
        return lastMessage;
    }
}
