package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.DistributedTopicEventBus;
import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.EventHandler;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTTopicEventBus implements DistributedTopicEventBus {
    @Override
    public void subscribe(String s) {

    }

    @Override
    public List<String> getSubscriptions() {
        return null;
    }

    @Override
    public void unsubscribe(String s) {

    }

    @Override
    public void connect() {

    }

    @Override
    public void disconnect() {

    }

    @Override
    public void publish(Event event, Object... objects) {

    }

    @Override
    public void registerHandler(EventHandler eventHandler) {

    }

    @Override
    public void registerFilter(EventFilter eventFilter) {

    }
}
