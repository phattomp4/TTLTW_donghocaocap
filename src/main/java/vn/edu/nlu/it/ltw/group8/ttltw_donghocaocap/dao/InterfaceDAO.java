package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.ShopInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

public class InterfaceDAO {

    public void updateShopInfo(ShopInfo info) {
        String query = "UPDATE ShopInfo SET BrandName=?, Subtitle=?, MainImageURL=?, History1=?, History2=?, History3=?, FooterDesc=?, Address=?, Hotline=?, Email=?, Copyright=? WHERE ID=1";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBanner(String imgUrl, String linkUrl, int sortOrder, Timestamp startDate, Timestamp endDate) {
        String query = "INSERT INTO Banners (ImageURL, LinkURL, SortOrder, StartDate, EndDate, IsActive) VALUES (?, ?, ?, ?, ?, 1)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, imgUrl);
            ps.setString(2, linkUrl);
            ps.setInt(3, sortOrder);
            ps.setTimestamp(4, startDate);
            ps.setTimestamp(5, endDate);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateBannerOrder(int id, int sortOrder) {
        String query = "UPDATE Banners SET SortOrder = ? WHERE BannerID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, sortOrder);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBanner(int id) {
        String query = "DELETE FROM Banners WHERE BannerID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addSmallBanner(String imgUrl, int sortOrder) {
        String query = "INSERT INTO SmallBanners (ImageURL, SortOrder, IsActive) VALUES (?, ?, 1)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, imgUrl);
            ps.setInt(2, sortOrder);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteSmallBanner(int id) {
        String query = "DELETE FROM SmallBanners WHERE ID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addBrand(String name, String logoUrl, int sortOrder) {
        String query = "INSERT INTO Brands (Name, LogoURL, SortOrder, IsActive) VALUES (?, ?, ?, 1)";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, name);
            ps.setString(2, logoUrl);
            ps.setInt(3, sortOrder);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteBrand(int id) {
        String query = "DELETE FROM Brands WHERE BrandID=?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}