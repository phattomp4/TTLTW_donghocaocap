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
import java.sql.*;
import java.util.LinkedHashMap;

public class ProductDAO {

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
                rs.getBoolean("IsLuxury"),
                rs.getInt("is_active")
        );
    }

    private Product mapProductShort(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("ProductID"));
        p.setName(rs.getString("Name"));
        p.setCurrentPrice(rs.getDouble("CurrentPrice"));
        p.setOriginalPrice(rs.getDouble("OriginalPrice"));
        p.setImageUrl(rs.getString("ImageURL"));
        p.setSoldQuantity(rs.getInt("SoldQuantity"));
        return p;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products ORDER BY CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) {
            System.out.println("Lỗi tại hàm getAllProducts: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getMenProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Name LIKE '%Nam%' AND Name NOT LIKE '%Dây%' AND Name NOT LIKE '%Hộp%' ORDER BY CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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
        String query = "SELECT * FROM Products WHERE Name LIKE '%Nữ%' AND Name NOT LIKE '%Dây%' AND Name NOT LIKE '%Hộp%' ORDER BY CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
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
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapProductShort(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Product getProductById(int id) {
        String query = "SELECT * FROM Products WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product p = mapResultSetToProduct(rs);
                    p.setImageList(getProductImages(id));
                    p.setSpecifications(getProductSpecs(id));
                    return p;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Product> getProductsByPriceRange(double min, double max) {
        List<Product> list = new ArrayList<>();
        String query = (max == -1) ? "SELECT * FROM Products WHERE CurrentPrice >= ?"
                : "SELECT * FROM Products WHERE CurrentPrice BETWEEN ? AND ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setDouble(1, min);
            if (max != -1) {
                ps.setDouble(2, max);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getProductImages(int productId) {
        List<String> list = new ArrayList<>();
        String query = "SELECT ImageURL FROM ProductImages WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rs.getString("ImageURL"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public Map<String, String> getProductSpecs(int productId) {
        Map<String, String> specs = new HashMap<>();
        String query = "SELECT SpecName, SpecValue FROM ProductSpecifications WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    specs.put(rs.getString("SpecName"), rs.getString("SpecValue"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return specs;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Name LIKE ? OR SKU LIKE ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByBrand(String brandName) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.* FROM Products p " +
                "JOIN Brands b ON p.BrandID = b.BrandID " +
                "WHERE b.Name LIKE ? AND p.StockQuantity > 0";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + brandName + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProductShort(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Product> getProductsByOrigin(String origin) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.* FROM Products p " +
                "JOIN ProductSpecifications ps ON p.ProductID = ps.ProductID " +
                "WHERE ps.SpecName = N'Xuất xứ' AND ps.SpecValue LIKE ? AND p.StockQuantity > 0";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + origin + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapProductShort(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateStock(int productId, int quantity) {
        String query = "UPDATE products SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, quantity);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getAllBrandsWithLogo() {
        Map<String, String> brandMap = new LinkedHashMap<>();
        String query = "SELECT Name, LogoURL FROM Brands ORDER BY Name ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                brandMap.put(rs.getString("Name"), rs.getString("LogoURL"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return brandMap;
    }

    public List<Product> filterProducts(String[] types, String priceRange, String[] brands, String[] genders, String collection, int limit, int offset) {
        List<Product> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT DISTINCT p.* FROM Products p ");

        if (brands != null && brands.length > 0) {
            query.append("JOIN Brands b ON p.BrandID = b.BrandID ");
        }
        query.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (types != null && types.length > 0) {
            query.append("AND (");
            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                if ("Đồng hồ".equalsIgnoreCase(type)) {
                    query.append("(p.Name LIKE '%Đồng hồ%' AND p.Name NOT LIKE '%Dây%' AND p.Name NOT LIKE '%Hộp%')");
                } else if ("Phụ kiện".equalsIgnoreCase(type)) {
                    query.append("(p.Name LIKE '%Dây%' OR p.Name LIKE '%Hộp%')");
                } else {
                    query.append("p.Name LIKE ?");
                    params.add("%" + type + "%");
                }
                if (i < types.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1": query.append("AND p.CurrentPrice < 1000000 "); break;
                case "1to3": query.append("AND p.CurrentPrice BETWEEN 1000000 AND 3000000 "); break;
                case "3to6": query.append("AND p.CurrentPrice BETWEEN 3000000 AND 6000000 "); break;
                case "6to9": query.append("AND p.CurrentPrice BETWEEN 6000000 AND 9000000 "); break;
                case "9to15": query.append("AND p.CurrentPrice BETWEEN 9000000 AND 15000000 "); break;
                case "over15": query.append("AND p.CurrentPrice > 15000000 "); break;
            }
        }

        if (brands != null && brands.length > 0) {
            query.append("AND (");
            for (int i = 0; i < brands.length; i++) {
                query.append("b.Name LIKE ?");
                params.add("%" + brands[i] + "%");
                if (i < brands.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        if (genders != null && genders.length > 0) {
            query.append("AND (");
            for (int i = 0; i < genders.length; i++) {
                if ("Unisex".equalsIgnoreCase(genders[i])) {
                    query.append("(p.Name LIKE '%Nam%' OR p.Name LIKE '%Nữ%')");
                } else {
                    query.append("p.Name LIKE ?");
                    params.add("%" + genders[i] + "%");
                }
                if (i < genders.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        String orderBy = "ORDER BY p.CreatedAt DESC ";
        if (collection != null && !collection.isEmpty()) {
            if ("luxury".equals(collection)) {
                query.append("AND p.IsLuxury = 1 ");
            } else if ("banchay".equals(collection)) {
                orderBy = "ORDER BY p.SoldQuantity DESC ";
            }
        }
        query.append(orderBy).append("LIMIT ? OFFSET ? ");
        params.add(limit);
        params.add(offset);

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalFilteredProducts(String[] types, String priceRange, String[] brands, String[] genders, String collection) {
        StringBuilder query = new StringBuilder("SELECT COUNT(DISTINCT p.ProductID) FROM Products p ");
        if (brands != null && brands.length > 0) {
            query.append("JOIN Brands b ON p.BrandID = b.BrandID ");
        }
        query.append("WHERE 1=1 ");
        List<Object> params = new ArrayList<>();

        if (types != null && types.length > 0) {
            query.append("AND (");
            for (int i = 0; i < types.length; i++) {
                String type = types[i];
                if ("Đồng hồ".equalsIgnoreCase(type)) {
                    query.append("(p.Name LIKE '%Đồng hồ%' AND p.Name NOT LIKE '%Dây%' AND p.Name NOT LIKE '%Hộp%')");
                } else if ("Phụ kiện".equalsIgnoreCase(type)) {
                    query.append("(p.Name LIKE '%Dây%' OR p.Name LIKE '%Hộp%')");
                } else {
                    query.append("p.Name LIKE ?");
                    params.add("%" + type + "%");
                }
                if (i < types.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        if (priceRange != null && !priceRange.isEmpty()) {
            switch (priceRange) {
                case "under1": query.append("AND p.CurrentPrice < 1000000 "); break;
                case "1to3": query.append("AND p.CurrentPrice BETWEEN 1000000 AND 3000000 "); break;
                case "3to6": query.append("AND p.CurrentPrice BETWEEN 3000000 AND 6000000 "); break;
                case "6to9": query.append("AND p.CurrentPrice BETWEEN 6000000 AND 9000000 "); break;
                case "9to15": query.append("AND p.CurrentPrice BETWEEN 9000000 AND 15000000 "); break;
                case "over15": query.append("AND p.CurrentPrice > 15000000 "); break;
            }
        }

        if (brands != null && brands.length > 0) {
            query.append("AND (");
            for (int i = 0; i < brands.length; i++) {
                query.append("b.Name LIKE ?");
                params.add("%" + brands[i] + "%");
                if (i < brands.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        if (genders != null && genders.length > 0) {
            query.append("AND (");
            for (int i = 0; i < genders.length; i++) {
                if ("Unisex".equalsIgnoreCase(genders[i])) {
                    query.append("(p.Name LIKE '%Nam%' OR p.Name LIKE '%Nữ%')");
                } else {
                    query.append("p.Name LIKE ?");
                    params.add("%" + genders[i] + "%");
                }
                if (i < genders.length - 1) query.append(" OR ");
            }
            query.append(") ");
        }

        if (collection != null && !collection.isEmpty() && "luxury".equals(collection)) {
            query.append("AND p.IsLuxury = 1 ");
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Product> getFeaturedProducts(int limit) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products ORDER BY IsFeatured DESC, Score DESC LIMIT ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateIsFeatured(int productId, int isFeatured) {
        String query = "UPDATE Products SET IsFeatured = ? WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, isFeatured);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Product> getBestSellersLast3Months(int limit) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.*, SUM(od.Quantity) as TotalSold " +
                "FROM Products p " +
                "JOIN OrderDetails od ON p.ProductID = od.ProductID " +
                "JOIN Orders o ON od.OrderID = o.OrderID " +
                "WHERE o.OrderDate >= DATE_SUB(NOW(), INTERVAL 3 MONTH) " +
                "AND o.Status = 'Delivered' " +
                "GROUP BY p.ProductID " +
                "ORDER BY TotalSold DESC " +
                "LIMIT ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, limit);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void incrementProductScore(int productId, int scoreValue) {
        String query = "UPDATE Products SET Score = Score + ? WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, scoreValue);
            ps.setInt(2, productId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean insertProduct(Product p) {
        String query = "INSERT INTO Products (BrandID, Name, SKU, Description, OriginalPrice, CurrentPrice, ImageURL, StockQuantity, SoldQuantity, IsLuxury, is_active) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, ?, 1)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, p.getBrandId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getSku());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getOriginalPrice());
            ps.setDouble(6, p.getCurrentPrice());
            ps.setString(7, p.getImageUrl());
            ps.setInt(8, p.getStockQuantity());
            ps.setBoolean(9, p.isLuxury());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateProduct(Product p) {
        String query = "UPDATE Products SET BrandID=?, Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, " +
                "ImageURL=?, StockQuantity=?, IsLuxury=?, is_active=? WHERE ProductID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, p.getBrandId());
            ps.setString(2, p.getName());
            ps.setString(3, p.getSku());
            ps.setString(4, p.getDescription());
            ps.setDouble(5, p.getOriginalPrice());
            ps.setDouble(6, p.getCurrentPrice());
            ps.setString(7, p.getImageUrl());
            ps.setInt(8, p.getStockQuantity());
            ps.setBoolean(9, p.isLuxury());
            ps.setInt(10, p.getIsActive());
            ps.setInt(11, p.getId());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int productId) {
        String query = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean toggleProductStatus(int productId, int status) {
        String query = "UPDATE Products SET is_active = ? WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, status);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}


