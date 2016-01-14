package com.oberasoftware.home.core.mqtt.converters;

import com.oberasoftware.home.api.converters.Converter;
import com.oberasoftware.home.api.converters.TypeConverter;
import com.oberasoftware.home.api.events.DeviceValueEvent;
import com.oberasoftware.home.api.types.Value;
import com.oberasoftware.home.core.mqtt.MQTTMessage;
import com.oberasoftware.home.core.mqtt.MQTTMessageImpl;
import org.springframework.stereotype.Component;

/**
 * @author Renze de Vries
 */
@Component
public class DeviceValueToMQTTConverter implements Converter {

    //controller/plugin/device/label
    private static final String TOPIC_FORMAT = "/states/%s/%s/%s/%s";

    @TypeConverter
    public MQTTMessage convert(DeviceValueEvent deviceValueEvent) {
        Value value = deviceValueEvent.getValue();
        String topic = String.format(TOPIC_FORMAT, deviceValueEvent.getControllerId(),
                deviceValueEvent.getPluginId(), deviceValueEvent.getDeviceId(), deviceValueEvent.getLabel());
        String message = value.toString();

        return new MQTTMessageImpl(topic, message);
    }
}
