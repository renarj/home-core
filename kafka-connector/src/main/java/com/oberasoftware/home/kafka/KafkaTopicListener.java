package com.oberasoftware.home.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.home.api.messaging.TopicConsumer;
import com.oberasoftware.home.api.messaging.TopicListener;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Renze de Vries
 */
@Component
public class KafkaTopicListener implements TopicListener<String> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaTopicListener.class);
    private static final int KAFKA_READ_TIMEOUT = 100;
    private static final int POLLER_SLEEP_INTERVAL = 1000;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Value("${kafka.consumer.host}")
    private String kafkaHost;

    private List<TopicConsumer<String>> topicConsumers = new CopyOnWriteArrayList<>();

    private volatile boolean running;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private org.apache.kafka.clients.consumer.KafkaConsumer<String, String> kafkaConsumer;

    @Override
    public void connect() {
        LOG.info("Connecting to Kafka host: {}", kafkaHost);
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaHost);
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        kafkaConsumer = new org.apache.kafka.clients.consumer.KafkaConsumer<>(props);
        kafkaConsumer.subscribe(Collections.singletonList("states"));

        running = true;
        executorService.submit((Runnable) this::runPoller);
        LOG.info("Finished connect to Kafka");
    }

    @Override
    public void close() {
        LOG.info("Closing kafka connection to: {}", kafkaHost);
        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            LOG.error("TopicListener thread not shutdown cleanly: {}", e.getMessage());
        }
        LOG.info("Closing consumer");
        kafkaConsumer.close();
    }

    @Override
    public void register(TopicConsumer<String> topicConsumer) {
        LOG.info("Registering topicConsumer: {}", topicConsumer);
        topicConsumers.add(topicConsumer);
    }

    private void runPoller() {
        LOG.info("Poller has started");
        while(running && !Thread.currentThread().isInterrupted()) {
            ConsumerRecords<String, String> r = kafkaConsumer.poll(KAFKA_READ_TIMEOUT);
            if(!r.isEmpty()) {
                r.forEach(cr -> notifyListeners(cr.value()));
            } else {
                sleepUninterruptibly(POLLER_SLEEP_INTERVAL, MILLISECONDS);
            }
        }
        LOG.info("Poller has stopped");
    }

    private void notifyListeners(String message) {
        topicConsumers.forEach(c -> {
            LOG.debug("Notifying consumer: {} with message: {}", c, message);
            c.receive(message);
        });
    }

}
