package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Review;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "LoadMoreReviewsServlet", urlPatterns = {"/load-more-reviews"})
public class LoadMoreReviewsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int offset = Integer.parseInt(request.getParameter("offset"));

            ReviewDAO reviewDAO = new ReviewDAO();
            List<Review> listReviews = reviewDAO.getReviewsWithPagination(pid, offset, 10);

            // truyền dữ liệu sang file jsp nhỏ để render HTML
            request.setAttribute("listReviews", listReviews);
            request.setAttribute("pid", pid); // truyền theo id sản phẩm để dùng cho form xóa

            request.getRequestDispatcher("review-fragment.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}