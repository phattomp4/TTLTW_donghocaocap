package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;
import java.util.Date;
public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private double priceAtPurchase;
    private Product product;
    private String ghnOrderCode;
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderDetail() {}


    public OrderDetail(int orderDetailId, int orderId, int productId, int quantity, double priceAtPurchase, Product product, String ghnOrderCode) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.product = product;
        this.ghnOrderCode = ghnOrderCode;
    }

    public int getOrderDetailId() {
        return orderDetailId;
    }public String getGhnOrderCode() {
        return ghnOrderCode;
    }

    public void setGhnOrderCode(String ghnOrderCode) {
        this.ghnOrderCode = ghnOrderCode;
    }

    public void setOrderDetailId(int orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPriceAtPurchase() {
        return priceAtPurchase;
    }

    public void setPriceAtPurchase(double priceAtPurchase) {
        this.priceAtPurchase = priceAtPurchase;
    }
}