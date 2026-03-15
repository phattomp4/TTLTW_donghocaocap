package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String user = request.getParameter("user");
        String pass = request.getParameter("pass");
        String re_pass = request.getParameter("re_pass");
        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        if (!pass.equals(re_pass)) {
            request.setAttribute("mess", "Mật khẩu không khớp!");
            request.getRequestDispatcher("register.jsp").forward(request, response);
        } else {
            UserDAO dao = new UserDAO();
            User a = dao.checkUserExist(user);
            if (a == null) {
                dao.signup(user, pass, fullName, email);
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("mess", "Tài khoản đã tồn tại!");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }
        }
    }
}