package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String u = request.getParameter("username");
        String p = request.getParameter("password");

        UserDAO dao = new UserDAO();
        User user = dao.login(u, p);

        if (user == null) {
            request.setAttribute("mess", "Sai tên đăng nhập hoặc mật khẩu!");
            request.setAttribute("username", u);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("acc", user);
            session.setMaxInactiveInterval(60 * 60);

            response.sendRedirect("home");
        }
    }
}