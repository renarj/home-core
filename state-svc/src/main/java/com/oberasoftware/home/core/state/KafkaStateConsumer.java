package com.oberasoftware.home.core.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oberasoftware.home.api.managers.StateManager;
import com.oberasoftware.home.api.model.ValueTransportMessage;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author Renze de Vries
 */
@Component
public class KafkaStateConsumer implements StateConsumer {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStateConsumer.class);
    private static final int KAFKA_READ_TIMEOUT = 100;
    private static final int POLLER_SLEEP_INTERVAL = 1000;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.enableDefaultTyping();
    }

    @Value("${kafka.consumer.host}")
    private String kafkaHost;

    @Autowired
    private StateManager stateManager;

    private volatile boolean running;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    private KafkaConsumer<String, String> kafkaConsumer;

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
        kafkaConsumer = new KafkaConsumer<>(props);
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
            LOG.error("Consumer thread not shutdown cleanly: {}", e.getMessage());
        }
        LOG.info("Closing consumer");
        kafkaConsumer.close();
    }

    private void runPoller() {
        LOG.info("Poller has started");
        while(running && !Thread.currentThread().isInterrupted()) {
            ConsumerRecords<String, String> r = kafkaConsumer.poll(KAFKA_READ_TIMEOUT);
            if(!r.isEmpty()) {
                r.forEach(cr -> {
                    try {
                        ValueTransportMessage message = OBJECT_MAPPER.readValue(cr.value(), ValueTransportMessage.class);
                        LOG.debug("Received value: {}", message);
                        stateManager.setState(message.getControllerId(),
                                message.getChannelId(), message.getLabel(), message.getValue());
                    } catch (IOException e) {
                        LOG.error("Could not read message", e);
                    } catch(Exception ex) {
                        LOG.error("Something happened", ex);
                    }
                });
            } else {
                sleepUninterruptibly(POLLER_SLEEP_INTERVAL, MILLISECONDS);
            }
        }
        LOG.info("Poller has stopped");
    }

}
