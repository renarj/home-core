package com.oberasoftware.home.kafka;

import com.oberasoftware.home.api.messaging.TopicSender;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Renze de Vries
 */
@Component
public class KafkaTopicSender implements TopicSender<String> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTopicSender.class);

    @Value("${kafka.producer.host}")
    private String kafkaHost;

    @Value("${kafka.topic}")
    private String kafkaTopic;


    private KafkaProducer<String, String> kafkaProducer;

    @Override
    public void connect() {
        LOG.info("Starting connecting to kafka: {} for topic: {}", kafkaHost, kafkaTopic);
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHost);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "producer");
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducer = new KafkaProducer<>(props);
    }

    @Override
    public void close() {
        LOG.info("Closing connection to Kafka");
        kafkaProducer.close();
    }

    @Override
    public void publish(String message) {
        LOG.debug("Publishing: {} to topic: {}", message, kafkaTopic);
        kafkaProducer.send(new ProducerRecord<>("states", message));
    }
}
