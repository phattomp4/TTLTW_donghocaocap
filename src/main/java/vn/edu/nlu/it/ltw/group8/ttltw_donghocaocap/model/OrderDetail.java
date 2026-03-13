package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class OrderDetail {
    private int orderDetailId;
    private int orderId;
    private int productId;
    private int quantity;
    private double priceAtPurchase;
    private Product product;
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }

    public OrderDetail() {
    }

    public OrderDetail(int orderDetailId, int orderId, int productId, int quantity, double priceAtPurchase) {
        this.orderDetailId = orderDetailId;
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
    }

    public int getOrderDetailId() { return orderDetailId; }
    public void setOrderDetailId(int orderDetailId) { this.orderDetailId = orderDetailId; }
    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
}