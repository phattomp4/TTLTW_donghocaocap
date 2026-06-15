package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Banner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BannerDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<Banner> getAllBanners() {
        List<Banner> list = new ArrayList<>();
        String query = "SELECT * FROM banners ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
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

    public void insertBanner(Banner b) {
        String query = "INSERT INTO banners (ImageURL, SortOrder, IsActive, LinkURL, StartDate, EndDate) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, b.getImageUrl());
            ps.setInt(2, b.getSortOrder());
            ps.setBoolean(3, b.isActive());
            ps.setString(4, b.getLinkUrl());
            ps.setTimestamp(5, b.getStartDate() != null ? new Timestamp(b.getStartDate().getTime()) : null);
            ps.setTimestamp(6, b.getEndDate() != null ? new Timestamp(b.getEndDate().getTime()) : null);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void updateBannerOrder(int id, int order) {
        String query = "UPDATE banners SET SortOrder = ? WHERE BannerID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, order);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteBanner(int id) {
        String query = "DELETE FROM banners WHERE BannerID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
    public List<Banner> getActiveBannersForClient() {
        List<Banner> list = new ArrayList<>();
        // Câu lệnh SQL lọc: Chỉ lấy Banner đang Active VÀ (Chưa đến ngày bắt đầu HOẶC đã bắt đầu) VÀ (Chưa đến ngày kết thúc HOẶC không hẹn ngày kết thúc)
        String query = "SELECT * FROM banners WHERE IsActive = 1 "
                + "AND (StartDate IS NULL OR StartDate <= NOW()) "
                + "AND (EndDate IS NULL OR EndDate >= NOW()) "
                + "ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
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
}