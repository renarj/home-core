package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.*;
import com.oberasoftware.base.event.impl.LocalEventBus;
import com.oberasoftware.home.api.converters.ConvertManager;
import com.oberasoftware.home.api.exceptions.ConversionException;
import com.oberasoftware.home.api.exceptions.HomeAutomationException;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTTopicEventBus implements DistributedTopicEventBus {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTTopicEventBus.class);

    @Value("${mqttHost}")
    private String mqttHost;

    @Autowired
    private ConvertManager convertManager;

    private MQTTBroker broker;

    @Autowired
    private LocalEventBus localEventBus;

    @PostConstruct
    public void initialize() throws HomeAutomationException {
        broker = new MQTTBroker(mqttHost);
        localEventBus.registerFilter(new MQTTPathFilter());
    }

    @Override
    public void subscribe(String topic) {
        broker.subscribeTopic(topic, (receivedTopic, payload) -> {
            LOG.info("Received a message on topic: {} with payload: {}", receivedTopic, payload);

            localEventBus.publish(new MQTTMessageImpl(receivedTopic, payload));
        });
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
        try {
            broker.connect();
            LOG.info("Connected to MQTT Broker: {}", mqttHost);
        } catch (HomeAutomationException e) {
            throw new RuntimeHomeAutomationException("Unable to connect to MQTT Broker", e);
        }
    }

    @Override
    public void disconnect() {
        broker.disconnect();
    }

    @Override
    public void publish(Event event, Object... objects) {
        LOG.info("Incoming event: {}", event);
        MQTTMessage message = convertManager.convert(event, MQTTMessage.class);
        if(message != null) {
            LOG.info("Converted to MQTT message: {}", message);
            broker.publish(message);
        } else {
            throw new ConversionException("Unable to convert event: " + event);
        }
    }

    @Override
    public void registerHandler(EventHandler eventHandler) {
        localEventBus.registerHandler(eventHandler);
    }

    @Override
    public void registerFilter(EventFilter eventFilter) {

    }
}
