package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseDAO {

    public void importProduct(int productId, int quantity, double importPrice) {
        try (Connection conn = new DBContext().getConnection()) {
            String sqlImport = "INSERT INTO ProductImports (productId, quantity, importPrice) VALUES (?, ?, ?)";
            PreparedStatement ps1 = conn.prepareStatement(sqlImport, PreparedStatement.RETURN_GENERATED_KEYS);
            ps1.setInt(1, productId);
            ps1.setInt(2, quantity);
            ps1.setDouble(3, importPrice);
            ps1.executeUpdate();

            ResultSet rsKeys = ps1.getGeneratedKeys();
            int importId = 0;
            if (rsKeys.next()) importId = rsKeys.getInt(1);

            String sqlUpdateStock = "UPDATE Products SET stockQuantity = stockQuantity + ? WHERE ProductID = ?";
            PreparedStatement ps2 = conn.prepareStatement(sqlUpdateStock);
            ps2.setInt(1, quantity);
            ps2.setInt(2, productId);
            ps2.executeUpdate();

            logTransaction(conn, productId, quantity, "IMPORT", "PNK-" + importId);

        } catch (Exception e) { e.printStackTrace(); }
    }

    public void logTransaction(Connection conn, int productId, int qtyChange, String type, String refCode) throws Exception {
        String sqlLog = "INSERT INTO InventoryTransactions (productId, quantityChange, transactionType, referenceCode) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(sqlLog);
        ps.setInt(1, productId);
        ps.setInt(2, qtyChange);
        ps.setString(3, type);
        ps.setString(4, refCode);
        ps.executeUpdate();
    }

    public List<Map<String, Object>> getInventoryHistory() {
        List<Map<String, Object>> list = new ArrayList<>();
        String sql = "SELECT t.*, p.name FROM InventoryTransactions t JOIN Products p ON t.productId = p.ProductID ORDER BY t.createdAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                map.put("productName", rs.getString("name"));
                map.put("qtyChange", rs.getInt("quantityChange"));
                map.put("type", rs.getString("transactionType"));
                map.put("refCode", rs.getString("referenceCode"));
                map.put("createdAt", rs.getTimestamp("createdAt"));
                list.add(map);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}