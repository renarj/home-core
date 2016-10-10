package com.oberasoftware.home.data.model;

import java.util.Map;

/**
 * @author Renze de Vries
 */
public class Widget implements UserItem {

    private String widgetId;
    private String userId;

    private Label label;
    private String name;
    private String widgetType;

    private String organisationalId;

    private Map<String, String> properties;

    public Widget(String widgetId, String organisationalId, Label label, String name, String widgetType, Map<String, String> properties) {
        this.widgetId = widgetId;
        this.organisationalId = organisationalId;
        this.label = label;
        this.name = name;
        this.widgetType = widgetType;
        this.properties = properties;
    }

    @Override
    public String getItemId() {
        return widgetId;
    }

    public String getOrganisationalId() {
        return organisationalId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public Label getLabel() {
        return label;
    }

    public String getName() {
        return name;
    }

    public String getWidgetType() {
        return widgetType;
    }

    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public String toString() {
        return "Widget{" +
                "widgetId='" + widgetId + '\'' +
                "organisationalId='" + organisationalId + '\'' +
                ", label=" + label +
                ", name='" + name + '\'' +
                ", widgetType='" + widgetType + '\'' +
                ", properties=" + properties +
                '}';
    }
}
