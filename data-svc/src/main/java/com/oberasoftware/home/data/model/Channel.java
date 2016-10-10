package com.oberasoftware.home.data.model;

/**
 * @author Renze de Vries
 */
public class Channel implements UserItem {
    private String userId;
    private String controllerId;
    private String channelId;

    public Channel(String userId, String controllerId, String channelId) {
        this.userId = userId;
        this.controllerId = controllerId;
        this.channelId = channelId;
    }

    @Override
    public String getItemId() {
        return channelId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getControllerId() {
        return controllerId;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "userId='" + userId + '\'' +
                ", controllerId='" + controllerId + '\'' +
                ", channelId='" + channelId + '\'' +
                '}';
    }
}
