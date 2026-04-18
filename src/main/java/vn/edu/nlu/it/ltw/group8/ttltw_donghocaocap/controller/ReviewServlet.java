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

            if (pidStr == null || ratingStr == null || comment == null || comment.trim().isEmpty()) {
                response.getWriter().write("Vui lòng nhập đầy đủ số sao và nội dung đánh giá.");
                return;
            }

            int productId = Integer.parseInt(pidStr);
            int rating = Integer.parseInt(ratingStr);

            OrderDAO orderDAO = new OrderDAO();
            if (!orderDAO.checkUserBoughtProduct(user.getId(), productId)) {
                response.getWriter().write("Bạn chưa mua sản phẩm này nên không thể đánh giá.");
                return;
            }

            String imageUrl = null;
            Part filePart = request.getPart("reviewImage");

            // Xử lý Cloudinary an toàn (Bỏ qua nếu bị lỗi API key)
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
                            "cloud_name", "dnrpxyuwo",
                            "api_key", "261138144329333",
                            "api_secret", "beBh1tv2UJYTuS8CWkVmKS48CO4"
                    ));
                    byte[] fileBytes = filePart.getInputStream().readAllBytes();
                    Map uploadResult = cloudinary.uploader().upload(fileBytes, ObjectUtils.asMap("folder", "vvp_store_reviews"));
                    imageUrl = (String) uploadResult.get("secure_url");
                } catch (Exception e) {
                    System.out.println("Lỗi Cloudinary (Bỏ qua ảnh): " + e.getMessage());
                }
            }

            ReviewDAO reviewDAO = new ReviewDAO();
            boolean success = reviewDAO.insertReview(productId, user.getId(), rating, comment, imageUrl);

            if (success) {
                response.getWriter().write("success|" + (imageUrl != null ? imageUrl : ""));
            } else {
                response.getWriter().write("Lỗi Database: Không thể lưu đánh giá.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Lỗi hệ thống Server: " + e.getMessage());
        }
    }
}