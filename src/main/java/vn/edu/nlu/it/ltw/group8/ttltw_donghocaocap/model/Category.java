package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class Category {
    private int id;
    private String name;
    private int parentId;
    private int sortOrder;
    private boolean isActive;

    public Category() {
    }

    public Category(int id, String name, int parentId, int sortOrder, boolean isActive) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
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

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
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