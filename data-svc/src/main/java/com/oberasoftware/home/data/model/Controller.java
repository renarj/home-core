package com.oberasoftware.home.data.model;

/**
 * @author Renze de Vries
 */
public class Controller implements UserItem {
    private String controllerId;
    private String userId;

    public Controller(String controllerId, String userId) {
        this.controllerId = controllerId;
        this.userId = userId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getItemId() {
        return controllerId;
    }

    @Override
    public String toString() {
        return "Controller{" +
                "controllerId='" + controllerId + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }
}
