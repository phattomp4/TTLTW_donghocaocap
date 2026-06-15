package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminDAO {

    public double getTotalRevenue() {
        String query = "SELECT SUM(TotalAmount) FROM Orders WHERE Status != 'Cancelled'";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countOrders() {
        String query = "SELECT COUNT(*) FROM Orders";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countUsers() {
        String query = "SELECT COUNT(*) FROM Users WHERE Role != 'Admin'";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public Product getProductById(int pid) {
        String query = "SELECT p.*, (SELECT ImageURL FROM ProductImages WHERE ProductID = p.ProductID LIMIT 1) AS ImageURL FROM Products p WHERE p.ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, pid);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products ORDER BY ProductID DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapResultSetToProduct(rs));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Product> getProductsByPage(int index, int pageSize) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM Products ORDER BY ProductID DESC LIMIT ? OFFSET ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, (index - 1) * pageSize);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public int getTotalProducts() {
        String sql = "SELECT COUNT(*) FROM Products";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public List<Product> getProductsWithFilter(String keyword, String gender, String brandId, String priceRange, int page, int pageSize) {
        List<Product> list = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM products WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty() && !keyword.equalsIgnoreCase("null")) {
            query.append(" AND Name LIKE ? ");
        }
        if (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("null") && !gender.equals("0")) {
            if ("Men".equalsIgnoreCase(gender)) {
                query.append(" AND (Name LIKE '%Nam%' OR Description LIKE '%Nam%') ");
            } else if ("Women".equalsIgnoreCase(gender)) {
                query.append(" AND (Name LIKE '%Nữ%' OR Description LIKE '%Nữ%') ");
            }
        }
        if (brandId != null && !brandId.trim().isEmpty() && !brandId.equalsIgnoreCase("null") && !brandId.equals("0")) {
            query.append(" AND BrandID = ? ");
        }
        if (priceRange != null && !priceRange.trim().isEmpty() && !priceRange.equalsIgnoreCase("null") && !priceRange.equals("0")) {
            String[] prices = priceRange.split("-");
            if (prices.length == 2) {
                query.append(" AND CurrentPrice BETWEEN ? AND ? ");
            }
        }

        query.append(" ORDER BY ProductID DESC LIMIT ? OFFSET ? ");

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty() && !keyword.equalsIgnoreCase("null")) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            if (brandId != null && !brandId.trim().isEmpty() && !brandId.equalsIgnoreCase("null") && !brandId.equals("0")) {
                ps.setInt(paramIndex++, Integer.parseInt(brandId));
            }
            if (priceRange != null && !priceRange.trim().isEmpty() && !priceRange.equalsIgnoreCase("null") && !priceRange.equals("0")) {
                String[] prices = priceRange.split("-");
                if (prices.length == 2) {
                    ps.setDouble(paramIndex++, Double.parseDouble(prices[0]));
                    ps.setDouble(paramIndex++, Double.parseDouble(prices[1]));
                }
            }

            int offset = (page - 1) * pageSize;
            if (offset < 0) offset = 0;

            ps.setInt(paramIndex++, pageSize);
            ps.setInt(paramIndex, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Product p = new Product();

                    p.setId(rs.getInt("ProductID"));
                    p.setBrandId(rs.getInt("BrandID"));

                    String name = rs.getString("Name");
                    p.setName(name != null ? name : "Sản phẩm không tên");

                    String sku = rs.getString("SKU");
                    p.setSku(sku != null ? sku : "N/A");

                    String desc = rs.getString("Description");
                    p.setDescription(desc != null ? desc : "");

                    p.setOriginalPrice(rs.getDouble("OriginalPrice"));
                    p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                    p.setStockQuantity(rs.getInt("StockQuantity"));
                    p.setSoldQuantity(rs.getInt("SoldQuantity"));

                    String img = rs.getString("ImageURL");
                    if (img == null || img.trim().isEmpty()) {
                        img = "https://via.placeholder.com/60";
                    }
                    p.setImageUrl(img);

                    p.setIsActive(rs.getBoolean("is_active") ? 1 : 0);

                    p.setLuxury(rs.getInt("IsFeatured") == 1);

                    list.add(p);
                }
            }
        } catch (Exception e) {
            System.out.println("🚨 LỖI SQL TRONG ADMIN_DAO: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== [ADMIN_DAO LOG] Thực tế đã bốc được: " + list.size() + " sản phẩm gửi về Servlet ===");
        return list;
    }

    public int getTotalProductsWithFilter(String keyword, String gender, String brandId, String priceRange) {
        int total = 0;
        StringBuilder query = new StringBuilder("SELECT COUNT(*) FROM products WHERE 1=1 ");

        if (keyword != null && !keyword.trim().isEmpty() && !keyword.equalsIgnoreCase("null")) {
            query.append(" AND Name LIKE ? ");
        }
        if (gender != null && !gender.trim().isEmpty() && !gender.equalsIgnoreCase("null") && !gender.equals("0")) {
            if ("Men".equalsIgnoreCase(gender)) {
                query.append(" AND (Name LIKE '%Nam%' OR Description LIKE '%Nam%') ");
            } else if ("Women".equalsIgnoreCase(gender)) {
                query.append(" AND (Name LIKE '%Nữ%' OR Description LIKE '%Nữ%') ");
            }
        }
        if (brandId != null && !brandId.trim().isEmpty() && !brandId.equalsIgnoreCase("null") && !brandId.equals("0")) {
            query.append(" AND BrandID = ? ");
        }
        if (priceRange != null && !priceRange.trim().isEmpty() && !priceRange.equalsIgnoreCase("null") && !priceRange.equals("0")) {
            String[] prices = priceRange.split("-");
            if (prices.length == 2) {
                query.append(" AND CurrentPrice BETWEEN ? AND ? ");
            }
        }

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            int paramIndex = 1;
            if (keyword != null && !keyword.trim().isEmpty() && !keyword.equalsIgnoreCase("null")) {
                ps.setString(paramIndex++, "%" + keyword + "%");
            }
            if (brandId != null && !brandId.trim().isEmpty() && !brandId.equalsIgnoreCase("null") && !brandId.equals("0")) {
                ps.setInt(paramIndex++, Integer.parseInt(brandId));
            }
            if (priceRange != null && !priceRange.trim().isEmpty() && !priceRange.equalsIgnoreCase("null") && !priceRange.equals("0")) {
                String[] prices = priceRange.split("-");
                if (prices.length == 2) {
                    ps.setDouble(paramIndex++, Double.parseDouble(prices[0]));
                    ps.setDouble(paramIndex++, Double.parseDouble(prices[1]));
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("🚨 LỖI ĐẾM SẢN PHẨM TRONG ADMIN_DAO: " + e.getMessage());
            e.printStackTrace();
        }
        return total;
    }
    public List<Product> searchProductsByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Name LIKE ? ORDER BY ProductID DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToProduct(rs));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean insertProduct(Product p) {
        String query = "INSERT INTO Products (BrandID, Name, SKU, Description, OriginalPrice, CurrentPrice, ImageURL, StockQuantity, SoldQuantity, IsLuxury, IsActive) VALUES (?, ?, ?, ?, ?, ?, ?, ?, 0, ?, 1)";
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
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void insertProduct(Product p, String imagePath) {
        String sqlProduct = "INSERT INTO Products (Name, SKU, Description, OriginalPrice, CurrentPrice, StockQuantity, SoldQuantity, BrandID, CategoryID, IsActive) VALUES (?, ?, ?, ?, ?, ?, 0, 1, 1, 1)";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setString(3, p.getDescription());
                ps.setDouble(4, p.getOriginalPrice());
                ps.setDouble(5, p.getCurrentPrice());
                ps.setInt(6, p.getStockQuantity());
                ps.executeUpdate();

                int productId = 0;
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) productId = generatedKeys.getInt(1);
                }

                if (imagePath != null && !imagePath.isEmpty()) {
                    String sqlImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
                    try (PreparedStatement psImg = conn.prepareStatement(sqlImg)) {
                        psImg.setInt(1, productId);
                        psImg.setString(2, imagePath);
                        psImg.executeUpdate();
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean updateProduct(Product p) {
        String query = "UPDATE Products SET BrandID=?, Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, ImageURL=?, StockQuantity=?, IsLuxury=?, is_active=? WHERE ProductID=?";
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
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void updateProduct(Product p, String newImagePath) {
        String sql = "UPDATE Products SET Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, StockQuantity=? WHERE ProductID=?";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setString(3, p.getDescription());
                ps.setDouble(4, p.getOriginalPrice());
                ps.setDouble(5, p.getCurrentPrice());
                ps.setInt(6, p.getStockQuantity());
                ps.setInt(7, p.getId());
                ps.executeUpdate();

                if (newImagePath != null && !newImagePath.isEmpty()) {
                    try (PreparedStatement psDel = conn.prepareStatement("DELETE FROM ProductImages WHERE ProductID=?")) {
                        psDel.setInt(1, p.getId());
                        psDel.executeUpdate();
                    }
                    try (PreparedStatement psImg = conn.prepareStatement("INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)")) {
                        psImg.setInt(1, p.getId());
                        psImg.setString(2, newImagePath);
                        psImg.executeUpdate();
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean toggleProductStatus(int productId, int status) {
        String query = "UPDATE Products SET is_active = ? WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, status);
            ps.setInt(2, productId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void deleteProduct(int pid) {
        String sqlImg = "DELETE FROM ProductImages WHERE ProductID = ?";
        String sqlProd = "DELETE FROM Products WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlImg);
                 PreparedStatement ps2 = conn.prepareStatement(sqlProd)) {
                ps1.setInt(1, pid);
                ps1.executeUpdate();
                ps2.setInt(1, pid);
                ps2.executeUpdate();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }


    public void insertFullProduct(Product p, List<String> detailImages, List<ProductSpecification> listSpecs) {
        String sqlProduct = "INSERT INTO Products (Name, SKU, Description, OriginalPrice, CurrentPrice, StockQuantity, SoldQuantity, BrandID, ImageURL, IsLuxury, IsActive) VALUES (?, ?, ?, ?, ?, ?, 0, 1, ?, ?, 1)";
        String sqlImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
        String sqlSpec = "INSERT INTO ProductSpecifications (ProductID, SpecName, SpecValue) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setString(3, p.getDescription());
                ps.setDouble(4, p.getOriginalPrice());
                ps.setDouble(5, p.getCurrentPrice());
                ps.setInt(6, p.getStockQuantity());
                ps.setString(7, p.getImageUrl());
                ps.setBoolean(8, p.isLuxury());
                ps.executeUpdate();

                int productId = 0;
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) productId = keys.getInt(1);
                }

                if (detailImages != null && !detailImages.isEmpty()) {
                    try (PreparedStatement psImg = conn.prepareStatement(sqlImg)) {
                        for (String imgPath : detailImages) {
                            psImg.setInt(1, productId);
                            psImg.setString(2, imgPath);
                            psImg.addBatch();
                        }
                        psImg.executeBatch();
                    }
                }
                if (listSpecs != null && !listSpecs.isEmpty()) {
                    try (PreparedStatement psSpec = conn.prepareStatement(sqlSpec)) {
                        for (ProductSpecification spec : listSpecs) {
                            psSpec.setInt(1, productId);
                            psSpec.setString(2, spec.getName());
                            psSpec.setString(3, spec.getValue());
                            psSpec.addBatch();
                        }
                        psSpec.executeBatch();
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateFullProduct(Product p, List<String> detailImages, List<ProductSpecification> listSpecs) {
        String sqlUpdate = "UPDATE Products SET Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, StockQuantity=?, IsLuxury=?, IsActive=? WHERE ProductID=?";
        String sqlUpdateImg = "UPDATE Products SET ImageURL=? WHERE ProductID=?";
        String sqlDelImg = "DELETE FROM ProductImages WHERE ProductID=?";
        String sqlInsImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
        String sqlDelSpec = "DELETE FROM ProductSpecifications WHERE ProductID=?";
        String sqlInsSpec = "INSERT INTO ProductSpecifications (ProductID, SpecName, SpecValue) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sqlUpdate)) {
                ps.setString(1, p.getName());
                ps.setString(2, p.getSku());
                ps.setString(3, p.getDescription());
                ps.setDouble(4, p.getOriginalPrice());
                ps.setDouble(5, p.getCurrentPrice());
                ps.setInt(6, p.getStockQuantity());
                ps.setBoolean(7, p.isLuxury());
                ps.setInt(8, p.getIsActive());
                ps.setInt(9, p.getId());
                ps.executeUpdate();

                if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
                    try (PreparedStatement psImgMain = conn.prepareStatement(sqlUpdateImg)) {
                        psImgMain.setString(1, p.getImageUrl());
                        psImgMain.setInt(2, p.getId());
                        psImgMain.executeUpdate();
                    }
                }
                try (PreparedStatement psDelI = conn.prepareStatement(sqlDelImg)) {
                    psDelI.setInt(1, p.getId());
                    psDelI.executeUpdate();
                }
                if (detailImages != null && !detailImages.isEmpty()) {
                    try (PreparedStatement psInsI = conn.prepareStatement(sqlInsImg)) {
                        for (String img : detailImages) {
                            psInsI.setInt(1, p.getId());
                            psInsI.setString(2, img);
                            psInsI.addBatch();
                        }
                        psInsI.executeBatch();
                    }
                }
                try (PreparedStatement psDelS = conn.prepareStatement(sqlDelSpec)) {
                    psDelS.setInt(1, p.getId());
                    psDelS.executeUpdate();
                }
                if (listSpecs != null && !listSpecs.isEmpty()) {
                    try (PreparedStatement psInsS = conn.prepareStatement(sqlInsSpec)) {
                        for (ProductSpecification spec : listSpecs) {
                            psInsS.setInt(1, p.getId());
                            psInsS.setString(2, spec.getName());
                            psInsS.setString(3, spec.getValue());
                            psInsS.addBatch();
                        }
                        psInsS.executeBatch();
                    }
                }
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<String> getDetailImages(int productId) {
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
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Map<String, String> getProductSpecsMap(int productId) {
        Map<String, String> map = new HashMap<>();
        String query = "SELECT SpecName, SpecValue FROM ProductSpecifications WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("SpecName"), rs.getString("SpecValue"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM Orders ORDER BY OrderDate DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("OrderID"));
                o.setUserId(rs.getInt("UserID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getString("Status"));
                o.setPaymentMethod(rs.getString("PaymentMethod"));
                list.add(o);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void updateOrderStatus(int orderId, String status) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean updateOrderStatusWithLog(int OrderID, String nextStatus){
        String queryCheck = "SELECT Status FROM Orders WHERE OrderID = ?";
        String queryUpdate = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            String currentStatus = "";
            try (PreparedStatement psCheck = conn.prepareStatement(queryCheck)) {
                psCheck.setInt(1, OrderID);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) currentStatus = rs.getString("Status");
                }
            }
            if ("Completed".equals(currentStatus) || "Cancelled".equals(currentStatus)) {
                return false;
            }
            try (PreparedStatement psUpdate = conn.prepareStatement(queryUpdate)) {
                psUpdate.setString(1, nextStatus);
                psUpdate.setInt(2, OrderID);
                psUpdate.executeUpdate();
            }
            if ("Cancelled".equals(nextStatus)) {
                String queryReturn = "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = ?";
                String queryUpdateStock = "UPDATE Products SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
                try (PreparedStatement psReturn = conn.prepareStatement(queryReturn);
                     PreparedStatement psStock = conn.prepareStatement(queryUpdateStock)) {
                    psReturn.setInt(1, OrderID);
                    try (ResultSet rsReturn = psReturn.executeQuery()) {
                        while (rsReturn.next()) {
                            psStock.setInt(1, rsReturn.getInt("Quantity"));
                            psStock.setInt(2, rsReturn.getInt("ProductID"));
                            psStock.addBatch();
                        }
                    }
                    psStock.executeBatch();
                }
            }
            conn.commit();
            return true;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String query = "SELECT u.*, " +
                "(SELECT Street FROM addresses WHERE UserID = u.UserID AND IsDefault = 1 LIMIT 1) AS DefaultStreet " +
                "FROM Users u WHERE Role != 'Admin' ORDER BY UserID DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                String defAddr = rs.getString("DefaultStreet");
                u.setAddress(defAddr != null ? defAddr : "Chưa thiết lập");
                u.setActive(rs.getBoolean("is_active"));
                list.add(u);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<User> searchUsers(String keyword) {
        List<User> list = new ArrayList<>();
        String query = "SELECT u.*, " +
                "(SELECT Street FROM addresses WHERE UserID = u.UserID AND IsDefault = 1 LIMIT 1) AS DefaultStreet " +
                "FROM Users u " +
                "WHERE Role != 'Admin' AND (FullName LIKE ? OR Email LIKE ? OR Phone LIKE ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("UserID"));
                    u.setUsername(rs.getString("Username"));
                    u.setFullName(rs.getString("FullName"));
                    u.setEmail(rs.getString("Email"));
                    u.setPhone(rs.getString("Phone"));
                    String defAddr = rs.getString("DefaultStreet");
                    u.setAddress(defAddr != null ? defAddr : "Chưa thiết lập");
                    u.setActive(rs.getBoolean("is_active")); // LẤY TRẠNG THÁI KHÓA/MỞ
                    list.add(u);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<User> getUsersWithPagination(int offset, int limit, String keyword) {
        List<User> list = new ArrayList<>();
        String query = "SELECT u.*, " +
                "(SELECT StreetDetail FROM addresses WHERE UserID = u.UserID AND IsDefault = 1 LIMIT 1) AS DefaultStreet " +
                "FROM users u WHERE 1=1";

        if (keyword != null && !keyword.isEmpty()) {
            query += " AND (u.Username LIKE ? OR u.FullName LIKE ? OR u.Email LIKE ? OR u.Phone LIKE ?)";
        }
        query += " ORDER BY u.UserID DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            int paramIdx = 1;
            if (keyword != null && !keyword.isEmpty()) {
                String key = "%" + keyword + "%";
                ps.setString(paramIdx++, key);
                ps.setString(paramIdx++, key);
                ps.setString(paramIdx++, key);
                ps.setString(paramIdx++, key);
            }
            ps.setInt(paramIdx++, limit);
            ps.setInt(paramIdx, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("UserID"));
                    u.setUsername(rs.getString("Username"));
                    u.setFullName(rs.getString("FullName"));
                    u.setEmail(rs.getString("Email"));
                    u.setPhone(rs.getString("Phone"));
                    u.setRole(rs.getString("Role"));
                    u.setStatus(rs.getString("Status"));
                    u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    u.setActive(rs.getBoolean("is_active"));
                    u.setAddress(rs.getString("DefaultStreet") != null ? rs.getString("DefaultStreet") : "Chưa thiết lập");
                    list.add(u);
                }
            }
        } catch (Exception e) {
            System.out.println(">>> LỖI TẠI GET USERS PAGINATION:");
            e.printStackTrace();
        }
        return list;
    }

    public int getTotalUsersCount(String keyword) {
        String query = "SELECT COUNT(*) FROM users WHERE 1=1";
        if (keyword != null && !keyword.isEmpty()) {
            query += " AND (Username LIKE ? OR FullName LIKE ? OR Email LIKE ? OR Phone LIKE ?)";
        }
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            if (keyword != null && !keyword.isEmpty()) {
                String key = "%" + keyword + "%";
                ps.setString(1, key);
                ps.setString(2, key);
                ps.setString(3, key);
                ps.setString(4, key);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public User getUserById(int id) {
        String query = "SELECT u.*, (SELECT Street FROM addresses WHERE UserID = u.UserID AND IsDefault = 1 LIMIT 1) AS DefaultStreet FROM Users u WHERE UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setId(rs.getInt("UserID"));
                    u.setUsername(rs.getString("Username"));
                    u.setFullName(rs.getString("FullName"));
                    u.setEmail(rs.getString("Email"));
                    u.setPhone(rs.getString("Phone"));
                    u.setStatus(rs.getString("Status"));
                    u.setCreatedAt(rs.getTimestamp("CreatedAt"));
                    u.setAddress(rs.getString("DefaultStreet"));
                    u.setActive(rs.getBoolean("is_active"));
                    return u;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public boolean updateUserRole(int userId, String newRole) {
        String sql = "UPDATE Users SET Role = ? WHERE UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newRole);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // HÀM MỚI: CẬP NHẬT TRẠNG THÁI KHÓA/MỞ KHÓA (Cho Admin sử dụng)
    public void toggleUserActiveStatus(int userId, boolean isActive) {
        String sql = "UPDATE users SET is_active = ? WHERE UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, isActive);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public UserShoppingStats getCustomerStats(int userId) {
        UserShoppingStats stats = new UserShoppingStats();
        String query = "SELECT " +
                "COUNT(*) AS TotalOrders, " +
                "SUM(CASE WHEN Status != 'Cancelled' THEN TotalAmount ELSE 0 END) AS TotalSpent, " +
                "COUNT(CASE WHEN Status = 'Completed' THEN 1 END) AS CompletedOrders, " +
                "COUNT(CASE WHEN Status = 'Cancelled' THEN 1 END) AS CancelledOrders " +
                "FROM Orders WHERE UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    stats.setTotalOrders(rs.getInt("TotalOrders"));
                    stats.setTotalSpent(rs.getDouble("TotalSpent"));
                    stats.setCompletedCount(rs.getInt("CompletedOrders"));
                    stats.setCancelledCount(rs.getInt("CancelledOrders"));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return stats;
    }

    public boolean addVoucher(Voucher voucher) {
        String sql = "INSERT INTO vouchers (Code, DiscountType, DiscountValue, MaxDiscount, UsageLimit, StartDate, EndDate) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, voucher.getCode());
            ps.setString(2, voucher.getDiscountType());
            ps.setDouble(3, voucher.getDiscountValue());
            ps.setDouble(4, voucher.getMaxDiscount());
            ps.setInt(5, voucher.getUsageLimit());
            ps.setTimestamp(6, voucher.getStartDate());
            ps.setTimestamp(7, voucher.getEndDate());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void saveVoucherHistory(int voucherId, int userId, int orderId) {
        String sqlInsert = "INSERT INTO voucher_history (VoucherID, UserID, OrderID) VALUES (?, ?, ?)";
        String sqlUpdate = "UPDATE vouchers SET UsedCount = UsedCount + 1 WHERE VoucherID = ?";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert);
                 PreparedStatement psUpdate = conn.prepareStatement(sqlUpdate)) {
                psInsert.setInt(1, voucherId);
                psInsert.setInt(2, userId);
                psInsert.setInt(3, orderId);
                psInsert.executeUpdate();

                psUpdate.setInt(1, voucherId);
                psUpdate.executeUpdate();
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    public List<VoucherUsageDTO> getUsageHistory() {
        List<VoucherUsageDTO> list = new ArrayList<>();
        String sql = "SELECT v.Code, u.Username, h.UsedAt, o.TotalAmount " +
                "FROM voucher_history h " +
                "JOIN vouchers v ON h.VoucherID = v.VoucherID " +
                "JOIN Users u ON h.UserID = u.UserID " +
                "JOIN Orders o ON h.OrderID = o.OrderID " +
                "ORDER BY h.UsedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                VoucherUsageDTO dto = new VoucherUsageDTO();
                dto.setCode(rs.getString("Code"));
                dto.setUsername(rs.getString("Username"));
                dto.setUsedAt(rs.getTimestamp("UsedAt"));
                dto.setOrderTotal(rs.getDouble("TotalAmount"));
                list.add(dto);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public Voucher getVoucherByCode(String code) {
        String sql = "SELECT * FROM vouchers WHERE Code = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Voucher v = new Voucher();
                    v.setVoucherID(rs.getInt("VoucherID"));
                    v.setCode(rs.getString("Code"));
                    v.setDiscountType(rs.getString("DiscountType"));
                    v.setDiscountValue(rs.getDouble("DiscountValue"));
                    v.setMaxDiscount(rs.getDouble("MaxDiscount"));
                    v.setUsageLimit(rs.getInt("UsageLimit"));
                    v.setUsedCount(rs.getInt("UsedCount"));
                    v.setStartDate(rs.getTimestamp("StartDate"));
                    v.setEndDate(rs.getTimestamp("EndDate"));
                    v.setMinOrderValue(rs.getDouble("MinOrderValue"));
                    return v;
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<VoucherUsageDTO> getVoucherHistoryByUserId(int userId) {
        List<VoucherUsageDTO> list = new ArrayList<>();
        String sql = "SELECT v.Code, h.UsedAt, o.TotalAmount " +
                "FROM voucher_history h " +
                "JOIN vouchers v ON h.VoucherID = v.VoucherID " +
                "JOIN Orders o ON h.OrderID = o.OrderID " +
                "WHERE h.UserID = ? " +
                "ORDER BY h.UsedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VoucherUsageDTO dto = new VoucherUsageDTO();
                    dto.setCode(rs.getString("Code"));
                    dto.setUsedAt(rs.getTimestamp("UsedAt"));
                    dto.setOrderTotal(rs.getDouble("TotalAmount"));
                    list.add(dto);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean hasUserUsedVoucher(int userId, String code) {
        String sql = "SELECT COUNT(*) FROM voucher_history h " +
                "JOIN vouchers v ON h.VoucherID = v.VoucherID " +
                "WHERE h.UserID = ? AND v.Code = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, code);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void processVoucherAfterOrder(int userId, String code, int orderId, double orderTotal) {
        String updateVoucherSql = "UPDATE vouchers SET UsedCount = UsedCount + 1 WHERE Code = ?";
        String insertHistorySql = "INSERT INTO voucher_history (VoucherID, UserID, OrderID, UsedAt) " +
                "VALUES ((SELECT VoucherID FROM vouchers WHERE Code = ? LIMIT 1), ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(updateVoucherSql);
                 PreparedStatement ps2 = conn.prepareStatement(insertHistorySql)) {

                ps1.setString(1, code);
                ps1.executeUpdate();

                ps2.setString(1, code);
                ps2.setInt(2, userId);
                ps2.setInt(3, orderId);
                ps2.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
                ps2.executeUpdate();

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) { e.printStackTrace(); }
    }


    public List<Banner> getAdminAllBanners() {
        List<Banner> list = new ArrayList<>();
        String query = "SELECT * FROM banners ORDER BY SortOrder ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Banner b = new Banner();
                b.setId(rs.getInt("BannerID"));
                b.setImageUrl(rs.getString("ImageURL"));
                b.setSortOrder(rs.getInt("SortOrder"));
                b.setActive(rs.getBoolean("IsActive"));
                b.setLinkUrl(rs.getString("LinkURL"));
                b.setStartDate(rs.getTimestamp("StartDate"));
                b.setEndDate(rs.getTimestamp("EndDate"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean insertBanner(Banner b) {
        String query = "INSERT INTO banners (ImageURL, SortOrder, IsActive, LinkURL, StartDate, EndDate) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, b.getImageUrl());
            ps.setInt(2, b.getSortOrder());
            ps.setBoolean(3, b.isActive());
            ps.setString(4, b.getLinkUrl());
            ps.setTimestamp(5, b.getStartDate() != null ? new Timestamp(b.getStartDate().getTime()) : null);
            ps.setTimestamp(6, b.getEndDate() != null ? new Timestamp(b.getEndDate().getTime()) : null);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateBanner(Banner b) {
        String query = "UPDATE banners SET ImageURL=?, IsActive=?, LinkURL=?, StartDate=?, EndDate=? WHERE BannerID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, b.getImageUrl());
            ps.setBoolean(2, b.isActive());
            ps.setString(3, b.getLinkUrl());
            ps.setTimestamp(4, b.getStartDate() != null ? new Timestamp(b.getStartDate().getTime()) : null);
            ps.setTimestamp(5, b.getEndDate() != null ? new Timestamp(b.getEndDate().getTime()) : null);
            ps.setInt(6, b.getId());
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public void updateBannerOrder(int bannerId, int newOrder) {
        String query = "UPDATE banners SET SortOrder = ? WHERE BannerID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, newOrder);
            ps.setInt(2, bannerId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public boolean deleteBanner(int bannerId) {
        String query = "DELETE FROM banners WHERE BannerID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, bannerId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateCategoryStatus(int catId, boolean isActive) {
        String query = "UPDATE categories SET IsActive = ? WHERE CategoryID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setBoolean(1, isActive);
            ps.setInt(2, catId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateCategoryOrder(int catId, int newOrder) {
        String query = "UPDATE categories SET SortOrder = ? WHERE CategoryID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, newOrder);
            ps.setInt(2, catId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }


    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setId(rs.getInt("ProductID"));
        p.setBrandId(rs.getInt("BrandID"));
        p.setName(rs.getString("Name"));
        p.setSku(rs.getString("SKU"));
        p.setDescription(rs.getString("Description"));
        p.setOriginalPrice(rs.getDouble("OriginalPrice"));
        p.setCurrentPrice(rs.getDouble("CurrentPrice"));
        p.setStockQuantity(rs.getInt("StockQuantity"));
        p.setSoldQuantity(rs.getInt("SoldQuantity"));
        p.setLuxury(rs.getBoolean("IsLuxury"));
        p.setIsActive(rs.getInt("is_active"));

        String img = rs.getString("ImageURL");
        if (img == null || img.trim().isEmpty()) {
            img = "https://via.placeholder.com/150";
        }
        p.setImageUrl(img);
        return p;
    }
}