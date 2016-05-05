package com.oberasoftware.home.core.mqtt;

/**
 * @author Renze de Vries
 */
public enum MessageGroup {
    STATES("states"),
    COMMANDS("commands"),
    UNKNOWN("unknown");

    private String group;

    MessageGroup(String group) {
        this.group = group;
    }

    public static MessageGroup fromGroup(String groupText) {
        for(MessageGroup group : values()) {
            if(group.group.equalsIgnoreCase(groupText)) {
                return group;
            }
        }

        return UNKNOWN;
    }
}
