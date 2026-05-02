package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

import java.util.Date;

public class Banner {
    private int id;
    private String imageUrl;
    private int sortOrder;
    private boolean isActive;
    private String linkUrl;
    private Date startDate;
    private Date endDate;

    public Banner() {
    }

    public Banner(int id, String imageUrl, int sortOrder, boolean isActive, String linkUrl, Date startDate, Date endDate) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
        this.linkUrl = linkUrl;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}