package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.HandlerEntry;

import java.lang.reflect.Method;

/**
 * @author Renze de Vries
 */
public class MQTTPathFilter implements EventFilter {

    private static final String PATH_REGEX = "/(.*)/(.*)/(.*)/(.*)";

    @Override
    public boolean isFiltered(Object o, HandlerEntry handlerEntry) {
        if(o instanceof MQTTMessageImpl) {
            MQTTMessageImpl mqttMessage = (MQTTMessageImpl) o;
            Method eventMethod = handlerEntry.getEventMethod();
            MQTTPath mqttPath = eventMethod.getAnnotation(MQTTPath.class);
            if(mqttPath != null) {
                String actualPath = mqttMessage.getTopic();

                return !isPathSupported(mqttPath, actualPath);
            }
        }
        return false;
    }

    /**
     * actualpath could be /states/robomax/sonar/distance -> value {"value":10.5}
     * supportedPath could be /states/{controllerId}/{device}/{label}
     *
     * actualpath could be /commands/robomax/motion/stand
     *
     * @param supportedPath
     * @param actualPath
     * @return
     */
    private boolean isPathSupported(MQTTPath supportedPath, String actualPath) {


        return false;
    }
}
