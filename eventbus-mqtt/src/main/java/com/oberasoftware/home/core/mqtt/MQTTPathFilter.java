package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.base.event.EventFilter;
import com.oberasoftware.base.event.HandlerEntry;
import com.oberasoftware.home.api.exceptions.RuntimeHomeAutomationException;

import java.lang.reflect.Method;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Renze de Vries
 */
public class MQTTPathFilter implements EventFilter {

    private static final String PATH_REGEX = "/(.*)/(.*)/(.*)/(.*)";
    private static final Pattern PATH_PATTERN = Pattern.compile(PATH_REGEX);
    private static final int GROUP_COUNT = 4;

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
        ParsedPath parsedPath = parsePath(actualPath);


        return false;
    }

    private ParsedPath parsePath(String path) {
        Matcher matched = PATH_PATTERN.matcher(path);
        if(matched.find() && matched.groupCount() == GROUP_COUNT) {
            String group = matched.group(1);
            String controllerId = matched.group(2);
            String device = matched.group(3);
            String label = matched.group(4);

            return new ParsedPath(MessageGroup.fromGroup(group), controllerId, device, label);
        } else {
            throw new RuntimeHomeAutomationException("Could not parse message path: " + path);
        }
    }

}
