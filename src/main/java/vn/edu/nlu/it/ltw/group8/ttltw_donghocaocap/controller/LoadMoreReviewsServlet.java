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

            int filterStar = 0;
            String starParam = request.getParameter("star");
            if (starParam != null && !starParam.isEmpty()) {
                filterStar = Integer.parseInt(starParam);
            }

            boolean hasImage = false;
            String hasImageParam = request.getParameter("hasImage");
            if (hasImageParam != null && !hasImageParam.isEmpty()) {
                hasImage = Boolean.parseBoolean(hasImageParam);
            }

            ReviewDAO reviewDAO = new ReviewDAO();

            List<Review> listReviews = reviewDAO.getReviewsWithPaginationAndFilter(pid, offset, 10, filterStar, hasImage);

            request.setAttribute("listReviews", listReviews);
            request.setAttribute("pid", pid);

            request.getRequestDispatcher("review-fragment.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}