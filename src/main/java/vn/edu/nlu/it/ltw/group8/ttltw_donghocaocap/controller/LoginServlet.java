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
        String r = request.getParameter("remember");

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

            //Ghi nhớ đăng nhập bằng Cookies
            Cookie userCookie = new Cookie("userC", u);
            Cookie passCookie = new Cookie("passC", p);
            if("ON".equals(r)){
                int maxAge = 60 * 60 * 24 * 7; //Cookie lưu 7 ngày
                userCookie.setMaxAge(maxAge);
                passCookie.setMaxAge(maxAge);
            } else {
                userCookie.setMaxAge(0);
                passCookie.setMaxAge(0);
            }

            // Gửi cookie về trình duyệt của người dùng
            response.addCookie(userCookie);
            response.addCookie(passCookie);

            response.sendRedirect("home");
        }
    }
}