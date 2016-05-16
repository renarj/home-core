package com.oberasoftware.home.core.mqtt;

import com.oberasoftware.home.api.types.Value;

/**
 * @author Renze de Vries
 */
public class MessageValue {
    private Value value;
    private String controllerId;
    private String channelId;
    private String label;

    public MessageValue(Value value, String controllerId, String channelId, String label) {
        this.value = value;
        this.controllerId = controllerId;
        this.channelId = channelId;
        this.label = label;
    }

    public MessageValue() {
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "MessageValue{" +
                "value=" + value +
                ", controllerId='" + controllerId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
