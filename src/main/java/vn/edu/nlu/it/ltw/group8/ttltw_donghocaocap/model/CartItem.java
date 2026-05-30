package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class CartItem {
    private int cartId;
    private int userId;
    private int productId;
    private int quantity;

    private Product product;

    public CartItem() {}

    public CartItem(int cartId, int userId, int productId, int quantity) {
        this.cartId = cartId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getCartId() { return cartId; }
    public void setCartId(int cartId) { this.cartId = cartId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public double getTotalPrice() {
        if (product != null) {
            return product.getCurrentPrice() * quantity;
        }
        return 0;
    }
}