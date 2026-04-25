package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public int insertOrder(User user, List<CartItem> cart, int addressId, String paymentMethod, double totalAmount, double discountAmount) {
        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateProduct = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);

            String sqlOrder = "INSERT INTO orders (UserID, ShippingAddressID, OrderDate, TotalAmount, DiscountAmount, PaymentMethod, PaymentStatus, Status) "
                    + "VALUES (?, ?, NOW(), ?, ?, ?, 'Unpaid', 'Processing')";

            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, user.getId());
            psOrder.setInt(2, addressId);
            psOrder.setDouble(3, totalAmount);
            psOrder.setDouble(4, discountAmount);
            psOrder.setString(5, paymentMethod);
            psOrder.executeUpdate();

            rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }
            String sqlDetail = "INSERT INTO order_details (OrderID, ProductID, Quantity, PriceAtPurchase) VALUES (?, ?, ?, ?)";
            String sqlUpdateProduct = "UPDATE products SET StockQuantity = StockQuantity - ?, SoldQuantity = SoldQuantity + ? WHERE ProductID = ? AND StockQuantity >= ?";

            psDetail = conn.prepareStatement(sqlDetail);
            psUpdateProduct = conn.prepareStatement(sqlUpdateProduct);

            for (CartItem item : cart) {
                psDetail.setInt(1, orderId);
                psDetail.setInt(2, item.getProduct().getId());
                psDetail.setInt(3, item.getQuantity());
                psDetail.setDouble(4, item.getProduct().getCurrentPrice());
                psDetail.addBatch();

                psUpdateProduct.setInt(1, item.getQuantity());
                psUpdateProduct.setInt(2, item.getQuantity());
                psUpdateProduct.setInt(3, item.getProduct().getId());
                psUpdateProduct.setInt(4, item.getQuantity());
                psUpdateProduct.addBatch();
            }

            psDetail.executeBatch();
            int[] stockResults = psUpdateProduct.executeBatch();

            for (int res : stockResults) {
                if (res == 0) {
                    throw new Exception("Sản phẩm trong kho không đủ để thực hiện giao dịch!");
                }
            }
            conn.commit();
            return orderId;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return 0;
        } finally {
            try {
                if (rs != null) rs.close();
                if (psOrder != null) psOrder.close();
                if (psDetail != null) psDetail.close();
                if (psUpdateProduct != null) psUpdateProduct.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public boolean confirmPaid(int orderId, String transactionId) throws SQLException {
        String sql = "UPDATE orders SET PaymentStatus = 'Paid', TransactionID = ?, PaymentDate = NOW(), Status = 'Processing' WHERE OrderID = ?";
        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, transactionId);
                ps.setInt(2, orderId);
                int result = ps.executeUpdate();
                updateOrderStatusWithConn(conn, orderId, "Paid", "Thanh toán thành công qua VNPAY.");
                conn.commit();
                return result > 0;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new SQLException("Lỗi khi xác nhận thanh toán đơn hàng #" + orderId);
        }
    }
    private void updateOrderStatusWithConn(Connection conn, int orderId, String status, String note) throws SQLException {
        String sql = "INSERT INTO order_logs (OrderID, Status, Note, CreatedAt) VALUES (?, ?, ?, NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setString(2, status);
            ps.setString(3, note);
            ps.executeUpdate();
        }
    }

    public boolean updatePaymentStatus(int orderId, String paymentStatus, String transactionId) {
        String sql = "UPDATE orders SET PaymentStatus = ?, TransactionID = ? WHERE OrderID = ?";

        try (Connection conn = new DBContext().getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, paymentStatus);
                ps.setString(2, transactionId);
                ps.setInt(3, orderId);
                int row = ps.executeUpdate();

                if ("Failed".equalsIgnoreCase(paymentStatus) || "Cancelled".equalsIgnoreCase(paymentStatus)) {
                    rollbackStock(orderId);
                }
                return row > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void rollbackStock(int orderId) {
        String sqlGetDetails = "SELECT ProductID, Quantity FROM order_details WHERE OrderID = ?";
        String sqlUpdateStock = "UPDATE products SET StockQuantity = StockQuantity + ?, SoldQuantity = SoldQuantity - ? WHERE ProductID = ?";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement psGet = conn.prepareStatement(sqlGetDetails)) {
                psGet.setInt(1, orderId);
                ResultSet rs = psGet.executeQuery();
                while (rs.next()) {
                    try (PreparedStatement psUp = conn.prepareStatement(sqlUpdateStock)) {
                        int qty = rs.getInt("Quantity");
                        psUp.setInt(1, qty);
                        psUp.setInt(2, qty);
                        psUp.setInt(3, rs.getInt("ProductID"));
                        psUp.executeUpdate();
                    }
                }
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT * FROM Orders WHERE UserID = ? ORDER BY OrderDate DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("OrderID"));
                o.setUserId(rs.getInt("UserID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getString("Status")); // Quan trọng: Trạng thái đơn
                o.setPaymentMethod(rs.getString("PaymentMethod"));
                o.setPaymentStatus(rs.getString("PaymentStatus"));
                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }


    public int countProductsInOrder(int orderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = "SELECT COUNT(*) FROM OrderDetails WHERE OrderID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> list = new ArrayList<>();
        String query = "SELECT od.*, p.Name, " +
                "(SELECT ImageURL FROM ProductImages WHERE ProductID = p.ProductID LIMIT 1) AS ImageURL " +
                "FROM OrderDetails od " +
                "JOIN Products p ON od.ProductID = p.ProductID " +
                "WHERE od.OrderID = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            while (rs.next()) {
                OrderDetail od = new OrderDetail();
                od.setOrderDetailId(rs.getInt("OrderDetailID"));
                od.setOrderId(rs.getInt("OrderID"));
                od.setProductId(rs.getInt("ProductID"));
                od.setQuantity(rs.getInt("Quantity"));
                od.setPriceAtPurchase(rs.getDouble("PriceAtPurchase"));

                Product p = new Product();
                p.setId(rs.getInt("ProductID"));

                p.setName(rs.getString("Name"));

                String img = rs.getString("ImageURL");
                if (img == null) img = "https://via.placeholder.com/150";
                p.setImageUrl(img);

                od.setProduct(p);
                list.add(od);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list;
    }


    public Order getOrderById(int orderId) {
        String query = "SELECT * FROM orders WHERE OrderID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, orderId);
            rs = ps.executeQuery();
            if (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("OrderID"));
                o.setUserId(rs.getInt("UserID"));
                o.setShippingAddressId(rs.getInt("ShippingAddressID"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getString("Status"));
                o.setPaymentMethod(rs.getString("PaymentMethod"));
                o.setPaymentStatus(rs.getString("PaymentStatus"));
                o.setOrderDate(Timestamp.valueOf(rs.getTimestamp("OrderDate").toLocalDateTime()));
                return o;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public void updateOrderStatus(int orderId, String newStatus, String note) {
        String updateOrder = "UPDATE Orders SET Status = ? WHERE OrderID = ?";
        String insertLog = "INSERT INTO OrderLogs (OrderID, Status, Note) VALUES (?, ?, ?)";

        try (Connection conn = new DBContext().getConnection()) {
            conn.setAutoCommit(false);


            try (PreparedStatement psUpdate = conn.prepareStatement(updateOrder)) {
                psUpdate.setString(1, newStatus);
                psUpdate.setInt(2, orderId);
                psUpdate.executeUpdate();
            }


            try (PreparedStatement psLog = conn.prepareStatement(insertLog)) {
                psLog.setInt(1, orderId);
                psLog.setString(2, newStatus);
                psLog.setString(3, note);
                psLog.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean requestCancelOrderSafe(int orderId, int userId) {
        String sql = "UPDATE Orders SET Status = 'Request Cancel' " +
                "WHERE OrderID = ? AND UserID = ? AND Status IN ('Pending', 'Processing')";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int countOrdersByStatus(int userId, String status) {
        String sql = "SELECT COUNT(*) FROM Orders WHERE UserID = ?";
        if (status != null && !status.equals("all")) {
            sql += " AND Status = ?";
        }
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            if (status != null && !status.equals("all")) {
                ps.setString(2, status);
            }
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Order> getOrdersWithPagination(int userId, String status, int offset, int limit) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE UserID = ?";

        if (status != null && !status.equals("all")) {
            sql += " AND Status = ?";
        }
        sql += " ORDER BY OrderDate DESC LIMIT ? OFFSET ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int paramIndex = 1;
            ps.setInt(paramIndex++, userId);

            if (status != null && !status.equals("all")) {
                ps.setString(paramIndex++, status);
            }

            ps.setInt(paramIndex++, limit);
            ps.setInt(paramIndex, offset);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order o = new Order();
                    o.setOrderId(rs.getInt("OrderID"));
                    o.setUserId(rs.getInt("UserID"));
                    o.setOrderDate(rs.getTimestamp("OrderDate"));
                    o.setTotalAmount(rs.getDouble("TotalAmount"));
                    o.setStatus(rs.getString("Status"));
                    o.setPaymentMethod(rs.getString("PaymentMethod"));
                    o.setPaymentStatus(rs.getString("PaymentStatus"));
                    list.add(o);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<OrderLog> getOrderLog(int orderId){
        List<OrderLog> list = new ArrayList<>();
        String sql = "SELECT * FROM OrderLogs WHERE OrderID = ? ORDER BY CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection()){
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, orderId);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                while(resultSet.next()){
                    OrderLog orderLog = new OrderLog();
                    orderLog.setOrderId(resultSet.getInt("OrderID"));
                    orderLog.setStatus(resultSet.getString("Status"));
                    orderLog.setNote(resultSet.getString("Note"));
                    orderLog.setCreatedAt(resultSet.getTimestamp("CreatedAt"));
                    list.add(orderLog);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }

// hàm kiểm tra user đã mua và nhận thành công chưa
    public boolean checkUserBoughtProduct(int userId, int productId) {
        String query = "SELECT COUNT(*) FROM OrderDetails od " +
                "JOIN Orders o ON od.OrderID = o.OrderID " +
                "WHERE o.UserID = ? AND od.ProductID = ? AND o.Status = 'Completed'";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setInt(2, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;

    }

}