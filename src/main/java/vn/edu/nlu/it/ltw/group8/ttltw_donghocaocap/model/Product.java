package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Product {
    private int id;
    private int brandId;
    private String name;
    private String sku;
    private String description;
    private double originalPrice;
    private double currentPrice;
    private String imageUrl;
    private int stockQuantity;
    private int soldQuantity;
    private boolean isLuxury;

    private List<String> imageList = new ArrayList<>();

    private Map<String, String> specifications = new HashMap<>();

    public Product() {}

    public Product(int id, int brandId, String name, String sku, String description, double originalPrice, double currentPrice, String imageUrl, int stockQuantity, int soldQuantity, boolean isLuxury) {
        this.id = id;
        this.brandId = brandId;
        this.name = name;
        this.sku = sku;
        this.description = description;
        this.originalPrice = originalPrice;
        this.currentPrice = currentPrice;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity;
        this.soldQuantity = soldQuantity;
        this.isLuxury = isLuxury;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public boolean isLuxury() {
        return isLuxury;
    }

    public void setLuxury(boolean luxury) {
        isLuxury = luxury;
    }

    public List<String> getImageList() {
        return imageList;
    }
    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }

    public Map<String, String> getSpecifications() {
        return specifications;
    }
    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }
}