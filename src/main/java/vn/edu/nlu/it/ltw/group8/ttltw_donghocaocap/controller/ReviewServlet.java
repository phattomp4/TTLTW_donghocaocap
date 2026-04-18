package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;
import java.util.Map;

@WebServlet(name = "ReviewServlet", urlPatterns = {"/submit-review"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, maxFileSize = 1024 * 1024 * 5)
public class ReviewServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain; charset=UTF-8");
        request.setCharacterEncoding("UTF-8");

        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");

            if (user == null) {
                response.getWriter().write("Vui lòng đăng nhập để đánh giá.");
                return;
            }

            String pidStr = request.getParameter("productId");
            String ratingStr = request.getParameter("rating");
            String comment = request.getParameter("comment");
            String reviewIdStr = request.getParameter("reviewId");

            if (pidStr == null || comment == null || comment.trim().isEmpty()) {
                response.getWriter().write("Vui lòng nhập nội dung đánh giá.");
                return;
            }

            int productId = Integer.parseInt(pidStr);
            int rating = 0;
            if (ratingStr != null && !ratingStr.isEmpty()) {
                rating = Integer.parseInt(ratingStr);
            }

            OrderDAO orderDAO = new OrderDAO();
            if (!orderDAO.checkUserBoughtProduct(user.getId(), productId)) {
                response.getWriter().write("Bạn chưa mua sản phẩm này nên không thể đánh giá.");
                return;
            }

            String imageUrl = null;
            Part filePart = request.getPart("reviewImage");
          
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                            "cloud_name", "dnrpxyuwo",
                            "api_key", "261138144329333",
                            "api_secret", "beBh1tv2UJYTuS8CWkVmKS48CO4"
                    ));
                    byte[] fileBytes = filePart.getInputStream().readAllBytes();
                    Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap("folder", "reviews"));
                    imageUrl = (String) uploadResult.get("secure_url");
                } catch (Exception e) {
                    System.out.println("Lỗi Cloudinary (Bỏ qua ảnh): " + e.getMessage());
                }
            }

            ReviewDAO reviewDAO = new ReviewDAO();
            boolean success = false;

            if (reviewIdStr != null && !reviewIdStr.isEmpty()) {
                int existingReviewId = Integer.parseInt(reviewIdStr);
                success = reviewDAO.updateReview(existingReviewId, rating, comment, imageUrl);
            } else {
                if (reviewDAO.getMyReview(user.getId(), productId) != null) {
                    response.getWriter().write("Bạn đã đánh giá sản phẩm này rồi! Vui lòng chọn tính năng chỉnh sửa.");
                    return;
                }
                success = reviewDAO.insertReview(productId, user.getId(), rating, comment, imageUrl);
            }

            if (success) {
                response.getWriter().write("success|" + (imageUrl != null ? imageUrl : ""));
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi hệ thống Server: " + e.getMessage());
        }
    }
}