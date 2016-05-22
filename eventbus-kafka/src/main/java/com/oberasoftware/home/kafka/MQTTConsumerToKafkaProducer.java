package com.oberasoftware.home.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.base.event.EventHandler;
import com.oberasoftware.base.event.EventSubscribe;
import com.oberasoftware.home.api.model.ValueTransportMessage;
import com.oberasoftware.home.core.mqtt.*;
import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Renze de Vries
 */
@Component
public class MQTTConsumerToKafkaProducer implements EventHandler {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTConsumerToKafkaProducer.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Value("${kafka.producer.host}")
    private String kafkaHost;

    @Autowired
    private MQTTTopicEventBus eventBus;

    private KafkaProducer<String, String> kafkaProducer;

    public void start() {
        LOG.info("Starting connecting to kafka: {}", kafkaHost);
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHost);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer-test");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<>(props);

        LOG.info("Starting listening to state changes on MQTT");
        eventBus.connect();
        eventBus.registerHandler(this);
        eventBus.subscribe("/states/#");
    }

    @EventSubscribe
    public void receive(MQTTMessage message) {
        LOG.debug("Received a MQTT message: {}", message);
        ParsedPath parsedPath = MQTTPathParser.parsePath(message.getTopic());
        try {
            ValueTransportMessage parsedMessage = OBJECT_MAPPER.readValue(message.getMessage(), ValueTransportMessage.class);
            if(validateMessage(parsedPath, parsedMessage)) {
                LOG.debug("Message is valid, forwarding to Kafka: {}", message.getMessage());
                kafkaProducer.send(new ProducerRecord<>("states", message.getMessage()));
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
