package com.oberasoftware.home.kafka;

import com.oberasoftware.home.core.mqtt.MQTTConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@ComponentScan
@Import(MQTTConfiguration.class)
public class EdgeProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(EdgeProcessor.class);

    public static void main(String[] args) {
        LOG.info("Starting edge processor");

        SpringApplication.run(EdgeProcessor.class).getBean(MQTTConsumerToKafkaProducer.class).start();
        LOG.info("Edge Processor started");


    }
}
