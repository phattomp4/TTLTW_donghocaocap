package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    // hàm lưu đánh giá vào db
    public boolean insertReview(int productId, int userId, int rating, String comment, String imageUrl) {
        String query = "INSERT INTO Reviews (ProductID, UserID, Rating, Comment, ImageUrl, CreatedAt) VALUES (?, ?, ?, ?, ?, NOW())";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.setString(5, imageUrl);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    //hàm lấy danh sách đánh giá
    public List<Review> getReviewsByProductId(int productId) {
        List<Review> list = new ArrayList<>();
        String query = "SELECT r.*, u.Username, u.Avatar " +
                "FROM Reviews r " +
                "JOIN Users u ON r.UserID = u.UserID " +
                "WHERE r.ProductID = ? " +
                "ORDER BY r.CreatedAt DESC";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("ReviewID"));
                review.setProductId(rs.getInt("ProductID"));
                review.setUserId(rs.getInt("UserID"));
                review.setRating(rs.getInt("Rating"));
                review.setComment(rs.getString("Comment"));
                review.setImageUrl(rs.getString("ImageUrl"));
                review.setCreatedAt(rs.getTimestamp("CreatedAt"));
                review.setUsername(rs.getString("Username"));
                review.setUserAvatar(rs.getString("Avatar"));
                list.add(review);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // xóa đánh giá (phải kiểm tra đúng UserID)
    public boolean deleteReview(int reviewId, int userId) {
        String query = "DELETE FROM Reviews WHERE ReviewID = ? AND UserID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, reviewId);
            ps.setInt(2, userId);
            return ps.executeUpdate() > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    // lấy danh sách đánh giá có giới hạn
    public List<Review> getReviewsWithPagination(int productId, int offset, int limit) {
        List<Review> list = new ArrayList<>();
        String query = "SELECT r.*, u.Username, u.Avatar " +
                "FROM Reviews r " +
                "JOIN Users u ON r.UserID = u.id " +
                "WHERE r.ProductID = ? " +
                "ORDER BY r.CreatedAt DESC " +
                "LIMIT ? OFFSET ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ps.setInt(2, limit);
            ps.setInt(3, offset);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Review review = new Review();
                review.setId(rs.getInt("ReviewID"));
                review.setProductId(rs.getInt("ProductID"));
                review.setUserId(rs.getInt("UserID"));
                review.setRating(rs.getInt("Rating"));
                review.setComment(rs.getString("Comment"));
                review.setImageUrl(rs.getString("ImageUrl"));
                review.setCreatedAt(rs.getTimestamp("CreatedAt"));
                review.setUsername(rs.getString("Username"));
                review.setUserAvatar(rs.getString("Avatar"));
                list.add(review);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // đếm tổng số đánh giá của sản phẩm để ẩn nút "Xem thêm" khi hết
    public int countReviewsByProductId(int productId) {
        String query = "SELECT COUNT(*) FROM Reviews WHERE ProductID = ?";
        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }
}
