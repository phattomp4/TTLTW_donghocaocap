package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MenuDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    // 1. Lấy menu ĐANG BẬT cho khách hàng xem ở trang chủ (HomeServlet/MenuFilter)
    public List<Category> getActiveMenus() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE IsActive = 1 ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("CategoryID")); // Đổi lại đúng tên cột ID của bạn
                c.setName(rs.getString("Name"));
                c.setSortOrder(rs.getInt("SortOrder"));
                c.setActive(rs.getBoolean("IsActive"));
                list.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
        return list;
    }

    // 2. Lấy TẤT CẢ menu cho Admin quản lý
    public List<Category> getAllMenus() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM categories ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("CategoryID"));
                c.setName(rs.getString("Name"));
                c.setSortOrder(rs.getInt("SortOrder"));
                c.setActive(rs.getBoolean("IsActive"));
                list.add(c);
            }
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
        return list;
    }

    // 3. Cập nhật trạng thái Bật/Tắt (IsActive)
    public void updateStatus(int id, boolean isActive) {
        String query = "UPDATE categories SET IsActive = ? WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setBoolean(1, isActive);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
    }

    // 4. Cập nhật thứ tự vị trí khi kéo thả
    public void updateMenuOrder(int id, int sortOrder) {
        String query = "UPDATE categories SET SortOrder = ? WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, sortOrder);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (Exception e) {}
    }
}
