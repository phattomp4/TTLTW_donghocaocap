package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;



import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // Hàm hỗ trợ map dữ liệu từ ResultSet sang Object (để đỡ viết lại nhiều lần)
    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        return new Product(
                rs.getInt("ProductID"),
                rs.getInt("BrandID"),
                rs.getString("Name"),
                rs.getString("SKU"),
                rs.getString("Description"),
                rs.getDouble("OriginalPrice"),
                rs.getDouble("CurrentPrice"),
                rs.getString("ImageURL"),
                rs.getInt("StockQuantity"),
                rs.getInt("SoldQuantity"),
                rs.getBoolean("IsLuxury")
        );
    }

    public List<Product> getFeaturedProducts() {
        List<Product> list = new ArrayList<>();

        String query = "SELECT * FROM Products ORDER BY CreatedAt DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getMenProducts() {
        List<Product> list = new ArrayList<>();
        // Dùng LIKE '%Nam%' để tìm sản phẩm có chứa chữ Nam
        String query = "SELECT * FROM Products WHERE Name LIKE '%Nam%' ORDER BY CreatedAt DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Product> getWomenProducts() {
        List<Product> list = new ArrayList<>();

        String query = "SELECT * FROM Products WHERE Name LIKE '%Nữ%' ORDER BY CreatedAt DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public List<Product> getLuxuryProducts() {
        List<Product> list = new ArrayList<>();

        String query = "SELECT * FROM Products WHERE IsLuxury = 1 ORDER BY CreatedAt DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getLuxuryProducts1() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE IsLuxury = 1";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Product getProductById(int id) {
        String query = "SELECT * FROM Products WHERE ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product p = mapResultSetToProduct(rs);
                p.setImageList(getProductImages(id));
                p.setSpecifications(getProductSpecs(id));

                return p;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsByPriceRange(double min, double max) {
        List<Product> list = new ArrayList<>();
        String query;
        if (max == -1) {
            query = "SELECT * FROM Products WHERE CurrentPrice >= ?";
        } else {
            query = "SELECT * FROM Products WHERE CurrentPrice BETWEEN ? AND ?";
        }

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setDouble(1, min);
            if (max != -1) {
                ps.setDouble(2, max);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getProductImages(int productId) {
        List<String> list = new ArrayList<>();

        String query = "SELECT ImageURL FROM ProductImages WHERE ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("ImageURL"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }


    public Map<String, String> getProductSpecs(int productId) {
        Map<String, String> specs = new HashMap<>();
        String query = "SELECT SpecName, SpecValue FROM ProductSpecifications WHERE ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                specs.put(rs.getString("SpecName"), rs.getString("SpecValue"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return specs;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Name LIKE ? OR SKU LIKE ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            String searchPattern = "%" + keyword + "%"; // Thêm % để tìm kiếm gần đúng
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByBrand(String brandName) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.*, b.Name as BrandName FROM Products p " +
                "JOIN Brands b ON p.BrandID = b.BrandID " +
                "WHERE b.Name LIKE ? AND p.StockQuantity > 0";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + brandName + "%"); // Tìm gần đúng
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs)); // Giả sử bạn có hàm mapProduct để đỡ viết lại code set thuộc tính
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Product> getProductsByOrigin(String origin) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.* FROM Products p " +
                "JOIN ProductSpecifications ps ON p.ProductID = ps.ProductID " +
                "WHERE ps.SpecName = N'Xuất xứ' AND ps.SpecValue LIKE ? AND p.StockQuantity > 0";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + origin + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    private Product mapProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("ProductID"));
        p.setName(rs.getString("Name"));
        p.setCurrentPrice(rs.getDouble("CurrentPrice"));
        p.setOriginalPrice(rs.getDouble("OriginalPrice"));
        p.setImageUrl(rs.getString("ImageURL"));
        p.setSoldQuantity(rs.getInt("SoldQuantity"));
        return p;
    }

    public void updateStock(int productId, int quantity) {
        String query = "UPDATE products SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}


