package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Category;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.PriceRange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {


    public List<Category> getCategoriesByParent(int parentId) {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories WHERE ParentCategoryID = ? AND IsActive = 1 ORDER BY SortOrder ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, parentId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getInt("ParentCategoryID"),
                        rs.getInt("SortOrder"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public List<Category> getAllCategoriesForAdmin() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT * FROM Categories ORDER BY ParentCategoryID ASC, SortOrder ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getInt("ParentCategoryID"),
                        rs.getInt("SortOrder"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void addCategory(String name, int parentId, int sortOrder, boolean isActive) {
        String sql = "INSERT INTO Categories (Name, ParentCategoryID, SortOrder, IsActive) VALUES (?, ?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, parentId);
            ps.setInt(3, sortOrder);
            ps.setBoolean(4, isActive);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateCategory(int id, String name, int parentId, int sortOrder, boolean isActive) {
        String sql = "UPDATE Categories SET Name=?, ParentCategoryID=?, SortOrder=?, IsActive=? WHERE CategoryID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, parentId);
            ps.setInt(3, sortOrder);
            ps.setBoolean(4, isActive);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteCategory(int id) {
        String sql = "DELETE FROM Categories WHERE CategoryID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateCategorySortOrder(int id, int order) {
        String sql = "UPDATE Categories SET SortOrder = ? WHERE CategoryID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, order);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void toggleCategoryStatus(int id, boolean status) {
        String sql = "UPDATE Categories SET IsActive = ? WHERE CategoryID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBoolean(1, status);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }


    public List<PriceRange> getAllPriceRanges() {
        List<PriceRange> list = new ArrayList<>();
        String sql = "SELECT * FROM PriceRanges ORDER BY minPrice ASC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new PriceRange(
                        rs.getInt("id"),
                        rs.getString("label"),
                        rs.getDouble("minPrice"),
                        rs.getDouble("maxPrice")
                ));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    public void addPriceRange(String label, double min, double max) {
        String sql = "INSERT INTO PriceRanges (label, minPrice, maxPrice) VALUES (?, ?, ?)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, label);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updatePriceRange(int id, String label, double min, double max) {
        String sql = "UPDATE PriceRanges SET label=?, minPrice=?, maxPrice=? WHERE id=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, label);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deletePriceRange(int id) {
        String sql = "DELETE FROM PriceRanges WHERE id = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}