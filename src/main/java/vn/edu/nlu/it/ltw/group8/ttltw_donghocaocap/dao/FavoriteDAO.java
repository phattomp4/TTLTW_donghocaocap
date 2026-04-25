package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {

    public boolean isFavorite(int userId, int productId) {
        String query = "SELECT 1 FROM Favorites WHERE UserID = ? AND ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            return ps.executeQuery().next();
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean toggleFavorite(int userId, int productId) {
        if (isFavorite(userId, productId)) {
            String query = "DELETE FROM Favorites WHERE UserID = ? AND ProductID = ?";
            try (Connection conn = new DBContext().getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.executeUpdate();
            } catch (Exception e) { e.printStackTrace(); }
            return false;
        } else {

            String query = "INSERT INTO Favorites (UserID, ProductID) VALUES (?, ?)";
            try (Connection conn = new DBContext().getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);
                ps.setInt(2, productId);
                ps.executeUpdate();
            } catch (Exception e) { e.printStackTrace(); }
            return true;
        }
    }


    public List<Product> getFavoriteProducts(int userId) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.* FROM Products p JOIN Favorites f ON p.id = f.ProductID WHERE f.UserID = ? ORDER BY f.CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setCurrentPrice(rs.getDouble("currentPrice"));
                p.setOriginalPrice(rs.getDouble("originalPrice"));
                p.setImageUrl(rs.getString("imageUrl"));
                p.setStockQuantity(rs.getInt("stockQuantity"));
                list.add(p);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}