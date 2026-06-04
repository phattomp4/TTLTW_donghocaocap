package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    // thêm sản phẩm vào giỏ (cộng dồn số lượng nếu đã tồn tại)
    public void addToCart(int userId, int productId, int quantity) {
        String sql = "INSERT INTO CartItems (UserID, ProductID, Quantity) VALUES (?, ?, ?) " +
                "ON DUPLICATE KEY UPDATE Quantity = Quantity + VALUES(Quantity)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // lấy toàn bộ giỏ hàng của 1 user
    public List<CartItem> getCartByUserId(int userId) {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.CartID, c.UserID, c.ProductID, c.Quantity, " +
                "p.Name, p.ImageURL, p.CurrentPrice " +
                "FROM CartItems c " +
                "JOIN Products p ON c.ProductID = p.ProductID " +
                "WHERE c.UserID = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CartItem item = new CartItem();
                item.setCartId(rs.getInt("CartID"));
                item.setUserId(rs.getInt("UserID"));
                item.setProductId(rs.getInt("ProductID"));
                item.setQuantity(rs.getInt("Quantity"));
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setImageUrl(rs.getString("ImageURL"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                item.setProduct(p);

                list.add(item);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // cập nhật số lượng
    public void updateQuantity(int cartId, int newQuantity) {
        String sql = "UPDATE CartItems SET Quantity = ? WHERE CartID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, cartId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // xóa 1 sản phẩm khỏi giỏ
    public void removeCartItem(int cartId) {
        String sql = "DELETE FROM CartItems WHERE CartID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, cartId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // xóa sạch giỏ hàng sau khi thanh toán thành công
    public void clearCart(int userId) {
        String sql = "DELETE FROM CartItems WHERE UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // cập nhật số lượng dựa trên UserID và ProductID
    public void updateCartQuantityDirect(int userId, int productId, int quantity) {
        String sql = "UPDATE CartItems SET Quantity = ? WHERE UserID = ? AND ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, quantity);
            ps.setInt(2, userId);
            ps.setInt(3, productId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // xóa sản phẩm dựa trên UserID và ProductID
    public void removeCartItemDirect(int userId, int productId) {
        String sql = "DELETE FROM CartItems WHERE UserID = ? AND ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}

