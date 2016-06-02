package com.oberasoftware.home.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author Renze de Vries
 */
@Component
public class RabbitMQConnector {
    private static final Logger LOG = LoggerFactory.getLogger(RabbitMQConnector.class);

    @Value("${rmq.host:}")
    private String host;

    @Value("${rmq.port:}")
    private int port;

    @Value("${rmq.user:}")
    private String user;

    @Value("${rmq.password:}")
    private String password;

    @Value("${rmq.virtualHost:}")
    private String virtualHost;


    public void connect(String topic) {
        LOG.info("Connecting to rabbitmq on host: {} and port: {}", host, port);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(user);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(port);
        try {
            Connection conn = factory.newConnection();

            Channel channel = conn.createChannel();
            channel.exchangeDeclare("topic", "fanout");
        } catch (IOException e) {
            LOG.error("", e);
        } catch (TimeoutException e) {
            LOG.error("", e);
        }

    }
}
