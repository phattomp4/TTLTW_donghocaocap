package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class SmallBanner {
    private int id;
    private String imageUrl;
    private int sortOrder;
    private boolean isActive;

    public SmallBanner() {}

    public SmallBanner(int id, String imageUrl, int sortOrder, boolean isActive) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
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
}