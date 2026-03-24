package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.constant.Iconstant;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.GoogleAccount;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller.GoogleLoginServlet;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            processRequest(request, response);
        } else {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
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

            if("ON".equals(r)){
                String token = java.util.UUID.randomUUID().toString();
                dao.updateRememberToken(user.getUsername(), token);
                Cookie tokenCookie = new Cookie("remember_token", token);
                int maxAge = 60 * 60 * 24 * 7; //Cookie lưu 7 ngày
                tokenCookie.setMaxAge(maxAge);
                response.addCookie(tokenCookie);
            }
            response.sendRedirect("home");
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String code = request.getParameter("code");

        try {
            String accessToken = GoogleLoginServlet.getToken(code);
            GoogleAccount acc = GoogleLoginServlet.getUserInfo(accessToken);

            System.out.println(">>> THÔNG TIN TỪ GOOGLE: " + acc.getEmail() + " | " + acc.getName());

            if (acc != null) {
                User user = new User();
                user.setUsername(acc.getName());

                 user.setFullName(acc.getName());
                user.setEmail(acc.getEmail());

                HttpSession session = request.getSession();
                session.setAttribute("acc", user);
                session.setMaxInactiveInterval(60 * 60);

                response.sendRedirect("home");
            } else {
                request.setAttribute("mess", "Lỗi lấy thông tin từ Google!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            System.out.println("Lỗi khi đăng nhập bằng Google: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("mess", "Đã xảy ra lỗi đăng nhập Google!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }


}