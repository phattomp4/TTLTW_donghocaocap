package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StatisticDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    private String getDateCondition(String period) {
        switch (period.toLowerCase()) {
            case "today":
                return "DATE(OrderDate) = CURDATE()";
            case "week":
                return "YEARWEEK(OrderDate, 1) = YEARWEEK(CURDATE(), 1)";
            case "year":
                return "YEAR(OrderDate) = YEAR(CURDATE())";
            case "month":
            default:
                return "MONTH(OrderDate) = MONTH(CURDATE()) AND YEAR(OrderDate) = YEAR(CURDATE())";
        }
    }

    public double getRevenueByPeriod(String period) {
        double revenue = 0;
        String query = "SELECT SUM(TotalAmount) FROM orders WHERE Status = 'Completed' AND " + getDateCondition(period);
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                revenue = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return revenue;
    }

    public int countOrdersByPeriod(String period) {
        int total = 0;
        String query = "SELECT COUNT(*) FROM orders WHERE Status = 'Completed' AND " + getDateCondition(period);
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                total = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return total;
    }

    public int countOrderStatus(String status) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM orders WHERE Status = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, status);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public int countPendingAndProcessingOrders() {
        int count = 0;
        String query = "SELECT COUNT(*) FROM orders WHERE Status IN ('Pending', 'Processing')";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public int countLowStockProducts(int limitValue) {
        int count = 0;
        String query = "SELECT COUNT(*) FROM products WHERE StockQuantity < ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, limitValue);
            rs = ps.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return count;
    }

    public Map<String, Integer> getOrderStatusStats() {
        Map<String, Integer> stats = new HashMap<>();
        String query = "SELECT Status, COUNT(*) as Total FROM orders GROUP BY Status";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                stats.put(rs.getString("Status"), rs.getInt("Total"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return stats;
    }

    public List<Product> getLowStockProducts() {
        List<Product> list = new ArrayList<>();
        String query = "SELECT * FROM products WHERE StockQuantity < 10 ORDER BY StockQuantity ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                p.setImageUrl(rs.getString("ImageURL"));
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<Order> getRecentOrders(int limit) {
        List<Order> list = new ArrayList<>();
        String query = "SELECT o.*, u.FullName AS CustomerName FROM orders o " +
                "LEFT JOIN users u ON o.UserID = u.Id " +
                "ORDER BY o.OrderDate DESC LIMIT ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setOrderId(rs.getInt("OrderID"));
                o.setUserId(rs.getInt("UserID"));
                o.setOrderDate(rs.getTimestamp("OrderDate"));
                o.setTotalAmount(rs.getDouble("TotalAmount"));
                o.setStatus(rs.getString("Status"));
                o.setPaymentMethod(rs.getString("PaymentMethod"));
                String cName = rs.getString("CustomerName");
                o.setTransactionId(cName != null ? cName : "Khách vãng lai");

                list.add(o);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public List<Product> getTopSellingProducts(int limit) {
        List<Product> list = new ArrayList<>();
        String query = "SELECT p.*, b.Name AS BrandName FROM products p " +
                "LEFT JOIN brands b ON p.BrandID = b.BrandID " +
                "ORDER BY p.SoldQuantity DESC LIMIT ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, limit);
            rs = ps.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("ProductID"));
                p.setName(rs.getString("Name"));
                p.setStockQuantity(rs.getInt("StockQuantity"));
                p.setSoldQuantity(rs.getInt("SoldQuantity"));
                p.setCurrentPrice(rs.getDouble("CurrentPrice"));
                p.setImageUrl(rs.getString("ImageURL"));
                p.setDescription(rs.getString("BrandName"));

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public Map<String, Double> getRevenueTimelineData(String period) {
        Map<String, Double> map = new LinkedHashMap<>();
        String query = "";

        if ("year".equalsIgnoreCase(period)) {
            query = "SELECT DATE_FORMAT(OrderDate, 'Tháng %m') AS Label, SUM(TotalAmount) AS Revenue FROM orders WHERE Status='Completed' AND YEAR(OrderDate)=YEAR(CURDATE()) GROUP BY MONTH(OrderDate) ORDER BY MONTH(OrderDate)";
        } else if ("week".equalsIgnoreCase(period)) {
            query = "SELECT CASE DAYOFWEEK(OrderDate) " +
                    "WHEN 1 THEN 'Chủ Nhật' WHEN 2 THEN 'Thứ 2' WHEN 3 THEN 'Thứ 3' " +
                    "WHEN 4 THEN 'Thứ 4' WHEN 5 THEN 'Thứ 5' WHEN 6 THEN 'Thứ 6' WHEN 7 THEN 'Thứ 7' END AS Label, " +
                    "SUM(TotalAmount) AS Revenue FROM orders WHERE Status='Completed' AND YEARWEEK(OrderDate,1)=YEARWEEK(CURDATE(),1) GROUP BY DAYOFWEEK(OrderDate) ORDER BY DAYOFWEEK(OrderDate)";
        } else if ("today".equalsIgnoreCase(period)) {
            query = "SELECT CONCAT(HOUR(OrderDate), 'h') AS Label, SUM(TotalAmount) AS Revenue FROM orders WHERE Status='Completed' AND DATE(OrderDate)=CURDATE() GROUP BY HOUR(OrderDate) ORDER BY HOUR(OrderDate)";
        } else {
            query = "SELECT DATE_FORMAT(OrderDate, '%d/%m') AS Label, SUM(TotalAmount) AS Revenue FROM orders WHERE Status='Completed' AND MONTH(OrderDate)=MONTH(CURDATE()) AND YEAR(OrderDate)=YEAR(CURDATE()) GROUP BY DATE(OrderDate) ORDER BY DATE(OrderDate)";
        }

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("Label"), rs.getDouble("Revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }

        if (map.isEmpty()) {
            map.put("Chưa có dữ liệu", 0.0);
        }
        return map;
    }

    public Map<String, Double> getBrandRevenueShare() {
        Map<String, Double> map = new LinkedHashMap<>();
        String query = "SELECT b.Name AS BrandName, SUM(od.Quantity * od.PriceAtPurchase) AS Revenue " +
                "FROM orderdetails od " +
                "JOIN products p ON od.ProductID = p.ProductID " +
                "JOIN brands b ON p.BrandID = b.BrandID " +
                "JOIN orders o ON od.OrderID = o.OrderID " +
                "WHERE o.Status = 'Completed' " +
                "GROUP BY b.BrandID, b.Name " +
                "ORDER BY Revenue DESC LIMIT 5";
        try {
            query = "SELECT b.Name AS BrandName, SUM(od.Quantity * od.PriceAtPurchase) AS Revenue " +
                    "FROM orderdetails od " +
                    "JOIN products p ON od.ProductID = p.ProductID " +
                    "JOIN brands b ON p.BrandID = b.BrandID " +
                    "JOIN orders o ON od.OrderID = o.OrderID " +
                    "WHERE o.Status = 'Completed' " +
                    "GROUP BY b.BrandID, b.Name " +
                    "ORDER BY Revenue DESC LIMIT 5";
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("BrandName"), rs.getDouble("Revenue"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return map;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}