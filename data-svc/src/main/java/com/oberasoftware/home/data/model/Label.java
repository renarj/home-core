package com.oberasoftware.home.data.model;

/**
 * @author Renze de Vries
 */
public class Label implements UserItem {
    private String userId;
    private String channelId;
    private String controllerId;

    private String label;

    public Label(String userId, String channelId, String controllerId, String label) {
        this.userId = userId;
        this.channelId = channelId;
        this.controllerId = controllerId;
        this.label = label;
    }

    @Override
    public String getItemId() {
        return label;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String toString() {
        return "Label{" +
                ", userId='" + userId + '\'' +
                ", channelId='" + channelId + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
