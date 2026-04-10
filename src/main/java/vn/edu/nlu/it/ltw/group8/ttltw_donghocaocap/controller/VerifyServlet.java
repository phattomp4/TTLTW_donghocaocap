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
        if (token == null || token.isEmpty()) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDAO dao = new UserDAO();
        boolean isActivated = dao.activateAccount(token);

        HttpSession session = request.getSession();
        if (isActivated) {
            session.setAttribute("mess_success", "Tài khoản đã được kích hoạt thành công! Mời bạn đăng nhập.");
            response.sendRedirect("login.jsp");
        } else {
            request.setAttribute("mess", "Mã kích hoạt không hợp lệ hoặc đã hết hạn!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}