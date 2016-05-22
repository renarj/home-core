package com.oberasoftware.home.core.state;

import com.oberasoftware.base.BaseConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

/**
 * @author Renze de Vries
 */
@SpringBootApplication
@Import({StateConfiguration.class, BaseConfiguration.class})
public class StateContainer {
    private static final Logger LOG = LoggerFactory.getLogger(StateContainer.class);

    public static void main(String[] args) {
        LOG.info("Starting state service");
        ApplicationContext context = SpringApplication.run(StateContainer.class);
        context.getBean(StateConsumer.class).connect();
    }
}
