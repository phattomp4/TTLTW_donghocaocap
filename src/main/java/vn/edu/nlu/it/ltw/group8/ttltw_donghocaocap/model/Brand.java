package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class Brand {
    private int id;
    private String name;
    private String logoUrl;
    private int sortOrder;
    private boolean isActive;

    public Brand() {}

    public Brand(int id, String name, String logoUrl, int sortOrder, boolean isActive) {
        this.id = id;
        this.name = name;
        this.logoUrl = logoUrl;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
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