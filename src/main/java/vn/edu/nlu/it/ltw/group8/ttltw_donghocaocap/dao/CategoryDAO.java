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
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Category> getCategoriesByParent(int parentId) {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM categories WHERE ParentCategoryID = ? ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, parentId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getInt("ParentCategoryID"),
                        rs.getInt("SortOrder"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        String query = "SELECT * FROM categories ORDER BY ParentCategoryID, SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(
                        rs.getInt("CategoryID"),
                        rs.getString("Name"),
                        rs.getInt("ParentCategoryID"),
                        rs.getInt("SortOrder"),
                        rs.getBoolean("IsActive")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addCategory(String name, int parentId, int sortOrder, boolean isActive) {
        String query = "INSERT INTO categories (Name, ParentCategoryID, SortOrder, IsActive) VALUES (?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, parentId);
            ps.setInt(3, sortOrder);
            ps.setBoolean(4, isActive);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCategory(int id) {
        String query = "DELETE FROM categories WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategory(int id, String name, int parentId, int sortOrder, boolean isActive) {
        String query = "UPDATE categories SET Name = ?, ParentCategoryID = ?, SortOrder = ?, IsActive = ? WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setInt(2, parentId);
            ps.setInt(3, sortOrder);
            ps.setBoolean(4, isActive);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategoryStatus(int id, boolean isActive) {
        String query = "UPDATE categories SET IsActive = ? WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setBoolean(1, isActive);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCategoryOrder(int id, int sortOrder) {
        String query = "UPDATE categories SET SortOrder = ? WHERE CategoryID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, sortOrder);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<PriceRange> getAllPriceRanges() {
        List<PriceRange> list = new ArrayList<>();
        String query = "SELECT * FROM PriceRanges ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new PriceRange(
                        rs.getInt("ID"),
                        rs.getString("Label"),
                        rs.getDouble("MinPrice"),
                        rs.getDouble("MaxPrice")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public void addPriceRange(String label, double min, double max) {
        String query = "INSERT INTO PriceRanges (Label, MinPrice, MaxPrice) VALUES (?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, label);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deletePriceRange(int id) {
        String query = "DELETE FROM PriceRanges WHERE ID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updatePriceRange(int id, String label, double min, double max) {
        String query = "UPDATE PriceRanges SET Label = ?, MinPrice = ?, MaxPrice = ? WHERE ID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, label);
            ps.setDouble(2, min);
            ps.setDouble(3, max);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}