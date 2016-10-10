package com.oberasoftware.home.data.model;

/**
 * @author Renze de Vries
 */
public class OrganisationalItem implements UserItem {
    private String organisationalItemId;
    private String userId;

    private String title;

    private String layoutType;
    private String layoutData;

    public OrganisationalItem(String organisationalItemId, String userId, String title, String layoutType, String layoutData) {
        this.organisationalItemId = organisationalItemId;
        this.userId = userId;
        this.title = title;
        this.layoutType = layoutType;
        this.layoutData = layoutData;
    }

    @Override
    public String getItemId() {
        return organisationalItemId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getOrganisationalItemId() {
        return organisationalItemId;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public String getLayoutData() {
        return layoutData;
    }

    @Override
    public String toString() {
        return "OrganisationalItem{" +
                "organisationalItemId='" + organisationalItemId + '\'' +
                ", userId='" + userId + '\'' +
                ", title='" + title + '\'' +
                ", layoutType='" + layoutType + '\'' +
                ", layoutData='" + layoutData + '\'' +
                '}';
    }
}
