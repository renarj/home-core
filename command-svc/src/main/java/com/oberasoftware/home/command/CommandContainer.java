package com.oberasoftware.home.command;

import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.home.kafka.KafkaConfiguration;
import com.oberasoftware.home.kafka.KafkaTopicSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@ComponentScan
@Import({KafkaConfiguration.class, BaseConfiguration.class})
public class CommandContainer {
    private static final Logger LOG = LoggerFactory.getLogger(CommandContainer.class);

    public static void main(String[] args) {
        LOG.info("Starting command channel service");
        ApplicationContext context = new SpringApplication(CommandContainer.class).run(args);
        KafkaTopicSender sender = context.getBean(KafkaTopicSender.class);
        sender.connect();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Killing the kafka connection gracefully on shutdown");
            sender.close();
        }));
    }
}
