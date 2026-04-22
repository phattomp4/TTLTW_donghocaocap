package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;



import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.ProductSpecification;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    public double getTotalRevenue() {
        String query = "SELECT SUM(TotalAmount) FROM Orders WHERE Status != 'Cancelled'";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public int countOrders() {
        String query = "SELECT COUNT(*) FROM Orders";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }


    public int countUsers() {
        String query = "SELECT COUNT(*) FROM Users WHERE Role != 'Admin'";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    public List<Order> getAllOrders() {
        List<Order> list = new ArrayList<>();
        String query = "SELECT * FROM Orders ORDER BY OrderDate DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
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

    // 5. CẬP NHẬT TRẠNG THÁI ĐƠN HÀNG
    public void updateOrderStatus(int orderId, String status) {
        String query = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void insertProduct(Product p, String imagePath) {
        String sqlProduct = "INSERT INTO Products (Name, SKU, Description, OriginalPrice, CurrentPrice, StockQuantity, SoldQuantity, BrandID, CategoryID) VALUES (?, ?, ?, ?, ?, ?, 0, 1, 1)";

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getSku());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getOriginalPrice());
            ps.setDouble(5, p.getCurrentPrice());
            ps.setInt(6, p.getStockQuantity());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int productId = 0;
            if (rs.next()) productId = rs.getInt(1);

            if (imagePath != null && !imagePath.isEmpty()) {
                String sqlImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
                PreparedStatement psImg = conn.prepareStatement(sqlImg);
                psImg.setInt(1, productId);
                psImg.setString(2, imagePath);
                psImg.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

    public void updateProduct(Product p, String newImagePath) {
        String sql = "UPDATE Products SET Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, StockQuantity=? WHERE ProductID=?";
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sql);
            ps.setString(1, p.getName());
            ps.setString(2, p.getSku());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getOriginalPrice());
            ps.setDouble(5, p.getCurrentPrice());
            ps.setInt(6, p.getStockQuantity());
            ps.setInt(7, p.getId());
            ps.executeUpdate();

            if (newImagePath != null && !newImagePath.isEmpty()) {

                PreparedStatement psDel = conn.prepareStatement("DELETE FROM ProductImages WHERE ProductID=?");
                psDel.setInt(1, p.getId());
                psDel.executeUpdate();

                PreparedStatement psImg = conn.prepareStatement("INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)");
                psImg.setInt(1, p.getId());
                psImg.setString(2, newImagePath);
                psImg.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        }
    }

    public Product getProductById(int pid) {

        String query = "SELECT p.*, (SELECT ImageURL FROM ProductImages WHERE ProductID = p.ProductID LIMIT 1) AS ImageURL FROM Products p WHERE p.ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, pid);
            rs = ps.executeQuery();
            if (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setSku(rs.getString("SKU"));
                p.setDescription(rs.getString("Description"));
                p.setOriginalPrice(rs.getDouble("OriginalPrice"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setImageUrl(rs.getString("ImageURL"));
                p.setLuxury(rs.getBoolean("IsLuxury"));
                return p;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products ORDER BY ProductID DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setSku(rs.getString("SKU"));
                p.setOriginalPrice(rs.getDouble("OriginalPrice"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setSoldQuantity(rs.getInt("SoldQuantity"));

                String img = rs.getString("ImageURL");
                if (img == null || img.trim().isEmpty()) {
                    img = "https://via.placeholder.com/150";
                }
                p.setImageUrl(img);
                p.setLuxury(rs.getBoolean("IsLuxury"));

                list.add(p);
            }
        } catch (Exception e) {
            // In lỗi ra console để debug (Xem Output cửa sổ bên dưới nếu vẫn lỗi)
            System.out.println("Lỗi getAllProducts: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return list;
    }

    public void deleteProduct(int pid) {
        try {
            conn = new DBContext().getConnection();

            String sqlImg = "DELETE FROM ProductImages WHERE ProductID = ?";
            PreparedStatement ps1 = conn.prepareStatement(sqlImg);
            ps1.setInt(1, pid);
            ps1.executeUpdate();

            String sqlProd = "DELETE FROM Products WHERE ProductID = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlProd);
            ps2.setInt(1, pid);
            ps2.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {}
        }
    }

    public void insertFullProduct(Product p, List<String> detailImages, List<ProductSpecification> listSpecs) {
        String sqlProduct = "INSERT INTO Products (Name, SKU, Description, OriginalPrice, CurrentPrice, StockQuantity, SoldQuantity, BrandID, ImageURL, IsLuxury) VALUES (?, ?, ?, ?, ?, ?, 0, 1, ?, ?)";

        String sqlImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
        String sqlSpec = "INSERT INTO ProductSpecifications (ProductID, SpecName, SpecValue) VALUES (?, ?, ?)";

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            ps = conn.prepareStatement(sqlProduct, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, p.getName());
            ps.setString(2, p.getSku());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getOriginalPrice());
            ps.setDouble(5, p.getCurrentPrice());
            ps.setInt(6, p.getStockQuantity());
            ps.setString(7, p.getImageUrl());


            ps.setBoolean(8, p.isLuxury());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            int productId = 0;
            if (rs.next()) productId = rs.getInt(1);
            if (detailImages != null && !detailImages.isEmpty()) {
                PreparedStatement psImg = conn.prepareStatement(sqlImg);
                for (String imgPath : detailImages) {
                    psImg.setInt(1, productId);
                    psImg.setString(2, imgPath);
                    psImg.addBatch();
                }
                psImg.executeBatch();
            }

            if (listSpecs != null && !listSpecs.isEmpty()) {
                PreparedStatement psSpec = conn.prepareStatement(sqlSpec);
                for (ProductSpecification spec : listSpecs) {
                    psSpec.setInt(1, productId);
                    psSpec.setString(2, spec.getName());
                    psSpec.setString(3, spec.getValue());
                    psSpec.addBatch();
                }
                psSpec.executeBatch();
            }

            conn.commit();
        } catch (Exception e) {
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) {}
        }
    }

    public void updateFullProduct(Product p, List<String> detailImages, List<ProductSpecification> listSpecs) {

        String sqlUpdate = "UPDATE Products SET Name=?, SKU=?, Description=?, OriginalPrice=?, CurrentPrice=?, StockQuantity=?, IsLuxury=? WHERE ProductID=?";
        String sqlUpdateImg = "UPDATE Products SET ImageURL=? WHERE ProductID=?";
        String sqlDelImg = "DELETE FROM ProductImages WHERE ProductID=?";
        String sqlInsImg = "INSERT INTO ProductImages (ProductID, ImageURL) VALUES (?, ?)";
        String sqlDelSpec = "DELETE FROM ProductSpecifications WHERE ProductID=?";
        String sqlInsSpec = "INSERT INTO ProductSpecifications (ProductID, SpecName, SpecValue) VALUES (?, ?, ?)";

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sqlUpdate);
            ps.setString(1, p.getName());
            ps.setString(2, p.getSku());
            ps.setString(3, p.getDescription());
            ps.setDouble(4, p.getOriginalPrice());
            ps.setDouble(5, p.getCurrentPrice());
            ps.setInt(6, p.getStockQuantity());
            ps.setBoolean(7, p.isLuxury());
            ps.setInt(8, p.getId()); // ID để WHERE
            ps.executeUpdate();

            if (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) {
                PreparedStatement psImgMain = conn.prepareStatement(sqlUpdateImg);
                psImgMain.setString(1, p.getImageUrl());
                psImgMain.setInt(2, p.getId());
                psImgMain.executeUpdate();
            }

            if (detailImages != null && !detailImages.isEmpty()) {

                PreparedStatement psDelI = conn.prepareStatement(sqlDelImg);
                psDelI.setInt(1, p.getId());
                psDelI.executeUpdate();

                PreparedStatement psInsI = conn.prepareStatement(sqlInsImg);
                for (String img : detailImages) {
                    psInsI.setInt(1, p.getId());
                    psInsI.setString(2, img);
                    psInsI.addBatch();
                }
                psInsI.executeBatch();
            }
            if (listSpecs != null) {

                PreparedStatement psDelS = conn.prepareStatement(sqlDelSpec);
                psDelS.setInt(1, p.getId());
                psDelS.executeUpdate();

                if (!listSpecs.isEmpty()) {
                    PreparedStatement psInsS = conn.prepareStatement(sqlInsSpec);
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
            try { if (conn != null) conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace();
        } finally {
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) {}
        }
    }

    public List<String> getDetailImages(int productId) {
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

    public java.util.Map<String, String> getProductSpecsMap(int productId) {
        java.util.Map<String, String> map = new java.util.HashMap<>();
        String query = "SELECT SpecName, SpecValue FROM ProductSpecifications WHERE ProductID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, productId);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("SpecName"), rs.getString("SpecValue"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return map;
    }

    public List<Product> searchProductsByName(String keyword) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM Products WHERE Name LIKE ? ORDER BY ProductID DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, "%" + keyword + "%");
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name")); // Cột tên là Name
                p.setSku(rs.getString("SKU"));
                p.setOriginalPrice(rs.getDouble("OriginalPrice"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setSoldQuantity(rs.getInt("SoldQuantity"));

                String img = rs.getString("ImageURL");
                if (img == null || img.trim().isEmpty()) img = "https://via.placeholder.com/150";
                p.setImageUrl(img);

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (conn != null) conn.close(); } catch (SQLException e) {}
        }
        return list;
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();

        String query = "SELECT u.*, " +
                "(SELECT Street FROM addresses WHERE UserID = u.UserID AND IsDefault = 1 LIMIT 1) AS DefaultStreet " +
                "FROM Users u WHERE Role != 'Admin' ORDER BY UserID DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                String defAddr = rs.getString("DefaultStreet");
                u.setAddress(defAddr != null ? defAddr : "Chưa thiết lập");
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

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            String key = "%" + keyword + "%";
            ps.setString(1, key);
            ps.setString(2, key);
            ps.setString(3, key);
            rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));

                String defAddr = rs.getString("DefaultStreet");
                u.setAddress(defAddr != null ? defAddr : "Chưa thiết lập");

                list.add(u);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public boolean updateOrderStatusWithLog(int OrderID, String nextStatus){
        String queryCheck = "SELECT Status FROM Orders WHERE OrderID = ?";
        String queryUpdate = "Update Orders SET Status = ? WHERE OrderID = ?";
        try{
            String currentStatus = "";
            Connection conn = new DBContext().getConnection();
            PreparedStatement preparedStatementCheck = conn.prepareStatement(queryCheck);
            preparedStatementCheck.setInt(1, OrderID);
            ResultSet resultSet = preparedStatementCheck.executeQuery();
            if(resultSet.next()){
                currentStatus = resultSet.getString("Status");
            }

            if("Completed".equals(currentStatus) || "Cancelled".equals(currentStatus)){
                return false;
            }

            PreparedStatement preparedStatementUpdate = conn.prepareStatement(queryUpdate);
            preparedStatementUpdate.setString(1, nextStatus);
            preparedStatementUpdate.setInt(2, OrderID);
            preparedStatementUpdate.executeQuery();

            if("Cancelled".equals(nextStatus)){
                String queryReturn = "SELECT ProductID, Quantity FROM OrderDetails WHERE OrderID = ?";
                PreparedStatement preparedStatementReturn = conn.prepareStatement(queryReturn);
                preparedStatementReturn.setInt(1, OrderID);
                ResultSet resultSetReturn = preparedStatementReturn.executeQuery();

                String queryUpdateStock = "UPDATE Products SET StockQuantity = StockQuantity + ? WHERE ProductID = ?";
                PreparedStatement preparedStatementStock = conn.prepareStatement(queryUpdateStock);

                while(resultSetReturn.next()){
                    preparedStatementStock.setInt(1, resultSetReturn.getInt("Quantity"));
                    preparedStatementStock.setInt(2, resultSetReturn.getInt("ProductID"));
                    preparedStatementStock.addBatch();
                }
                preparedStatementStock.executeBatch();
            }
            conn.commit();
            return true;
        }
        catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    }


