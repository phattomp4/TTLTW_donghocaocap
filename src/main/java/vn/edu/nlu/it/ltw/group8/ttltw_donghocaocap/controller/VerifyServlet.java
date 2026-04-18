package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import java.io.IOException;

@WebServlet(name = "VerifyServlet", urlPatterns = {"/verify"})
public class VerifyServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        HttpSession session = request.getSession();
        if (token == null || token.isEmpty()) {
            response.sendRedirect("login");
            return;
        }
        UserDAO dao = new UserDAO();
        boolean isActivated = dao.activateAccount(token);

        if (isActivated) {
            session.setAttribute("mess_success", "Tài khoản đã được kích hoạt thành công! Mời bạn đăng nhập.");
            response.sendRedirect("login");
        } else {
            session.setAttribute("mess", "Mã kích hoạt đã hết hạn hoặc không hợp lệ!");
            response.sendRedirect("login");
        }
    }
}