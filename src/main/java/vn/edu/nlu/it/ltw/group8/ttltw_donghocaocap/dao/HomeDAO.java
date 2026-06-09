package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Banner;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Brand;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.SmallBanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class HomeDAO {

    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public List<SmallBanner> getSmallBanners() {
        List<SmallBanner> list = new ArrayList<>();

        String query = "SELECT * FROM SmallBanners WHERE IsActive = 1 ORDER BY SortOrder ASC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                SmallBanner b = new SmallBanner();
                b.setId(rs.getInt("ID"));
                b.setImageUrl(rs.getString("ImageURL"));
                b.setSortOrder(rs.getInt("SortOrder"));
                b.setActive(rs.getBoolean("IsActive"));

                list.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception e) {}
        }
        return list;
    }

    public List<Banner> getSlideshowBanners() {
        List<Banner> list = new ArrayList<>();
        String query = "SELECT BannerID, ImageURL, link_url, start_date, end_date " +
                "FROM banners " +
                "WHERE IsActive = 1 " +
                "AND (start_date IS NULL OR start_date <= NOW()) " +
                "AND (end_date IS NULL OR end_date >= NOW()) " +
                "ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Banner banner = new Banner();
                banner.setId(rs.getInt("BannerID"));
                banner.setImageUrl(rs.getString("ImageURL"));
                banner.setLinkUrl(rs.getString("link_url"));

                banner.setStartDate(rs.getTimestamp("start_date"));
                banner.setEndDate(rs.getTimestamp("end_date"));
                list.add(banner);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Brand> getFeaturedBrands() {
        List<Brand> list = new ArrayList<>();

        String query = "SELECT * FROM brands WHERE IsActive = 1 ORDER BY SortOrder ASC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Brand b = new Brand();
                b.setId(rs.getInt("BrandID"));
                b.setName(rs.getString("Name"));
                b.setLogoUrl(rs.getString("LogoURL"));
                b.setSortOrder(rs.getInt("SortOrder"));
                b.setActive(rs.getBoolean("IsActive"));
                list.add(b);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
}