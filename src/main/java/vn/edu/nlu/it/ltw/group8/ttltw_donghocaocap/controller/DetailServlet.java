package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Review;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "DetailServlet", urlPatterns = {"/detail"})
public class DetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));

            ProductDAO dao = new ProductDAO();
            Product p = dao.getProductById(pid);
            dao.incrementProductScore(pid, 1);

            if(p != null) {
                request.setAttribute("p", p);

                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("acc");
                boolean canReview = false;

                // kiểm tra user đã mua hàng chưa để ẩn hiện form đánh giá
                if (user != null) {
                    OrderDAO orderDAO = new OrderDAO();
                    canReview = orderDAO.checkUserBoughtProduct(user.getId(), pid);
                }
                request.setAttribute("canReview", canReview);

                ReviewDAO reviewDAO = new ReviewDAO();
                if (user != null) {
                    Review myReview = reviewDAO.getMyReview(user.getId(), pid);
                    request.setAttribute("myReview", myReview);
                    // nếu myReview != null, nghĩa là khách đã đánh giá rồi, hiện form chỉnh sửa thay vì thêm mới
                }
                int totalReviews = reviewDAO.countReviewsByProductId(pid);
                request.setAttribute("totalReviews", totalReviews);

                List<Review> listReviews = reviewDAO.getReviewsWithPaginationAndFilter(pid, 0, 10, 0, false);
                request.setAttribute("listReviews", listReviews);

                boolean isFavorite = false;
                if (user != null) {
                    FavoriteDAO favDao = new FavoriteDAO();
                    isFavorite = favDao.isFavorite(user.getId(), pid);
                }
                request.setAttribute("isFavorite", isFavorite);
                request.getRequestDispatcher("detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("home");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("home");
        }
    }
}