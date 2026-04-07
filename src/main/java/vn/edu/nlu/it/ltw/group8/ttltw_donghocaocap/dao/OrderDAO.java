package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    public boolean insertOrder(User user, List<CartItem> cart, int addressId, String paymentMethod, double totalAmount, double discountAmount) {
        Connection conn = null;
        PreparedStatement psOrder = null;
        PreparedStatement psDetail = null;
        PreparedStatement psUpdateProduct = null;
        ResultSet rs = null;

        try {
            conn = new DBContext().getConnection();

            conn.setAutoCommit(false);

            String sqlOrder = "INSERT INTO Orders (UserID, ShippingAddressID, OrderDate, TotalAmount, DiscountAmount, PaymentMethod, PaymentStatus, Status) "
                    + "VALUES (?, ?, NOW(), ?, ?, ?, ?, ?)";

            psOrder = conn.prepareStatement(sqlOrder, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, user.getId());
            psOrder.setInt(2, addressId);
            psOrder.setDouble(3, totalAmount);
            psOrder.setDouble(4, discountAmount);
            psOrder.setString(5, paymentMethod);
            psOrder.setString(6, "Pending");
            psOrder.setString(7, "Processing");
            psOrder.executeUpdate();

            rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            String sqlDetail = "INSERT INTO OrderDetails (OrderID, ProductID, Quantity, PriceAtPurchase) VALUES (?, ?, ?, ?)";
            String sqlUpdateProduct = "UPDATE Products SET StockQuantity = StockQuantity - ?, SoldQuantity = SoldQuantity + ? WHERE ProductID = ?";

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
                psUpdateProduct.addBatch();
            }
            psDetail.executeBatch();
            psUpdateProduct.executeBatch();

            conn.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
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

    public void updateOrderStatus(int orderId, String status) {
        String query = "UPDATE orders SET Status = ? WHERE OrderID = ?";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            ps.setInt(2, orderId);
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

}