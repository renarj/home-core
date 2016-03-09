package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.BaseConfiguration;
import com.oberasoftware.home.api.impl.events.devices.DeviceValueEventImpl;
import com.oberasoftware.home.api.impl.types.ValueImpl;
import com.oberasoftware.home.api.types.VALUE_TYPE;
import com.oberasoftware.mqtt.broker.MQTTContainer;
import com.oberasoftware.mqtt.broker.server.Server;
import com.oberasoftware.mqtt.broker.server.config.MemoryConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Renze de Vries
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MQTTConfiguration.class, TestConfiguration.class, BaseConfiguration.class, MQTTContainer.class} )
@DirtiesContext
public class MQTTTest {
    private static final Logger LOG = LoggerFactory.getLogger(MQTTTest.class);

    @Autowired
    private MQTTTopicEventBus topicEventBus;

    @Autowired
    private TestListener testListener;

    @Autowired
    private Server server;

    @Before
    public void setUp() throws IOException {
        MemoryConfig config = new MemoryConfig();
        config.setWebsocketPort(9080);
        config.setPort(9009);

        server.startServer(config);
        LOG.info("Started Moquitto MQTT server");
    }

    @After
    public void tearDown() {
        topicEventBus.disconnect();
        LOG.info("Disconnected from MQTT server");

        server.stopServer();
        LOG.info("Stopped Moquitto MQTT server");
    }

    @Test
    public void testPublishAndReceive() {
        topicEventBus.connect();
        LOG.info("Connected to MQTT server");


        topicEventBus.subscribe("/states/#");

        topicEventBus.publish(new DeviceValueEventImpl("testController", "plugin1", "device1",
                new ValueImpl(VALUE_TYPE.DECIMAL, 100.5), "power"));

        LOG.info("Awaiting message");
        testListener.await();

        LOG.info("Received a message, asserting");
        assertThat(testListener.getLastMessage(), notNullValue());
        assertThat(testListener.getLastTopic(), notNullValue());
    }
}
