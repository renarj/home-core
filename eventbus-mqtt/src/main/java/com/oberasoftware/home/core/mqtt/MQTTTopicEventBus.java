package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.DistributedTopicEventBus;
import com.oberasoftware.base.event.Event;
import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.EventHandler;
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
import java.util.List;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTTopicEventBus implements DistributedTopicEventBus {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTTopicEventBus.class);

    @Value("${mqtt.host}")
    private String mqttHost;

    @Value("${mqtt.username:}")
    private String mqttUsername;

    @Value("${mqtt.password:}")
    private String mqttPassword;

    @Autowired
    private ConvertManager convertManager;

    private MQTTBroker broker;

    @Autowired
    private LocalEventBus localEventBus;

    @PostConstruct
    public void initialize() throws HomeAutomationException {
        broker = new MQTTBroker(mqttHost, mqttUsername, mqttPassword);
        localEventBus.registerFilter(new MQTTPathFilter());
    }

    @Override
    public void subscribe(String topic) {
        LOG.info("Subscribing to topic: {}", topic);
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
            LOG.info("Connected to MQTT Broker: {} with user: {}", mqttHost, mqttUsername);
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
