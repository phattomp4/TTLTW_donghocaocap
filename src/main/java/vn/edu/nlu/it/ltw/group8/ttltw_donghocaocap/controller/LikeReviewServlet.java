package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import java.io.IOException;

@WebServlet("/like-review")
public class LikeReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int rid = Integer.parseInt(request.getParameter("rid"));
        ReviewDAO dao = new ReviewDAO();
        int newLikes = dao.incrementLike(rid);
        response.getWriter().write(String.valueOf(newLikes));
    }
}