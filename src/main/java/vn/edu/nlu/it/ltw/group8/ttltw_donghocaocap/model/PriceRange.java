package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class PriceRange {
    private int id;
    private String label;
    private double minPrice;
    private double maxPrice;

    public PriceRange() {}
    public PriceRange(int id, String label, double minPrice, double maxPrice) {
        this.id = id;
        this.label = label;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}