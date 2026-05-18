package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import java.io.IOException;

@WebServlet("/like-reply")
public class LikeReplyServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        int replyId = Integer.parseInt(request.getParameter("replyId"));
        ReviewDAO dao = new ReviewDAO();
        int newLikes = dao.incrementReplyLike(replyId);
        response.getWriter().write(String.valueOf(newLikes));
    }
}