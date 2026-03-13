package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.ShopInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InterfaceDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public void updateShopInfo(ShopInfo info) {
        String query = "UPDATE ShopInfo SET BrandName=?, Subtitle=?, MainImageURL=?, History1=?, History2=?, History3=?, FooterDesc=?, Address=?, Hotline=?, Email=?, Copyright=? WHERE ID=1";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, info.getBrandName());
            ps.setString(2, info.getSubtitle());
            ps.setString(3, info.getMainImageUrl());
            ps.setString(4, info.getHistory1());
            ps.setString(5, info.getHistory2());
            ps.setString(6, info.getHistory3());
            ps.setString(7, info.getFooterDesc());
            ps.setString(8, info.getAddress());
            ps.setString(9, info.getHotline());
            ps.setString(10, info.getEmail());
            ps.setString(11, info.getCopyright());
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void addBanner(String imgUrl, int sortOrder) {
        String query = "INSERT INTO Banners (ImageURL, SortOrder, IsActive) VALUES (?, ?, 1)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, imgUrl);
            ps.setInt(2, sortOrder);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteBanner(int id) {
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("DELETE FROM Banners WHERE BannerID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void addSmallBanner(String imgUrl, int sortOrder) {
        String query = "INSERT INTO SmallBanners (ImageURL, SortOrder, IsActive) VALUES (?, ?, 1)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, imgUrl);
            ps.setInt(2, sortOrder);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteSmallBanner(int id) {
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("DELETE FROM SmallBanners WHERE ID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void addBrand(String name, String logoUrl, int sortOrder) {
        String query = "INSERT INTO Brands (Name, LogoURL, SortOrder, IsActive) VALUES (?, ?, ?, 1)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, logoUrl);
            ps.setInt(3, sortOrder);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void deleteBrand(int id) {
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement("DELETE FROM Brands WHERE BrandID=?");
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
    }
}