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

    // List chứa danh sách đường dẫn ảnh (cho bảng ProductImages)
    private List<String> imageList = new ArrayList<>();

    // Map chứa thông số kỹ thuật (cho bảng ProductSpecifications)
    // Key = Tên thông số (vd: Chống nước), Value = Giá trị (vd: 5 ATM)
    private Map<String, String> specifications = new HashMap<>();

    public Product() {}

    public Product(int id, int brandId, String name, String sku, String description, double originalPrice, double currentPrice, String imageUrl, int stockQuantity, int soldQuantity, boolean isLuxury) {
        this.id = id;
        this.brandId = brandId;
        this.name = name;
        this.sku = sku;
        this.description = description; // 3. Gán giá trị description
        this.originalPrice = originalPrice;
        this.currentPrice = currentPrice;
        this.imageUrl = imageUrl;
        this.stockQuantity = stockQuantity; // 4. Gán giá trị stockQuantity
        this.soldQuantity = soldQuantity;
        this.isLuxury = isLuxury;
    }


    public int getId() { return id; }
    public int getBrandId() { return brandId; }
    public String getName() { return name; }
    public String getSku() { return sku; }
    public String getDescription() { return description; } // 5. Thêm getter
    public double getOriginalPrice() { return originalPrice; }
    public double getCurrentPrice() { return currentPrice; }
    public String getImageUrl() { return imageUrl; }
    public int getStockQuantity() { return stockQuantity; } // 6. Thêm getter
    public int getSoldQuantity() { return soldQuantity; }
    public boolean isLuxury() { return isLuxury; }
    public Map<String, String> getSpecifications() {
        return specifications;
    }

    public void setId(int id) { this.id = id; }
    public void setBrandId(int brandId) { this.brandId = brandId; }
    public void setName(String name) { this.name = name; }
    public void setSku(String sku) { this.sku = sku; }
    public void setDescription(String description) { this.description = description; }
    public void setOriginalPrice(double originalPrice) { this.originalPrice = originalPrice; }
    public void setCurrentPrice(double currentPrice) { this.currentPrice = currentPrice; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }
    public void setLuxury(boolean luxury) { isLuxury = luxury; }
    public List<String> getImageList() {
        return imageList;
    }
    public void setImageList(List<String> imageList) {
        this.imageList = imageList;
    }
    public void setSpecifications(Map<String, String> specifications) {
        this.specifications = specifications;
    }
}