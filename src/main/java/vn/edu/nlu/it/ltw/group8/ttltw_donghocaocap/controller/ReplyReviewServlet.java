package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ReviewDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import java.io.IOException;

@WebServlet("/reply-review")
public class ReplyReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain; charset=UTF-8");
        User user = (User) request.getSession().getAttribute("acc");
        if (user == null) {
            response.getWriter().write("error|Vui lòng đăng nhập để trả lời.");
            return;
        }
        int rid = Integer.parseInt(request.getParameter("rid"));
        String text = request.getParameter("text");

        ReviewDAO dao = new ReviewDAO();
        if (dao.insertReply(rid, user.getId(), text)) {
            response.getWriter().write("success|" + user.getUsername() + "|" + (user.getAvatar() != null ? user.getAvatar() : "https://cdn-icons-png.flaticon.com/512/149/149071.png") + "|" + user.getRole());
        } else {
            response.getWriter().write("error|Lỗi lưu vào CSDL.");
        }
    }
}