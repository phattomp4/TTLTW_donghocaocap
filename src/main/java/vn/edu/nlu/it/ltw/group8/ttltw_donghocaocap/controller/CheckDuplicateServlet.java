package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import java.io.IOException;

@WebServlet(name = "CheckDuplicateServlet", urlPatterns = {"/checkDuplicate"})
public class CheckDuplicateServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String type = request.getParameter("type");
        String value = request.getParameter("value");
        UserDAO dao = new UserDAO();
        String message = "";

        if (value == null || value.trim().isEmpty()) {
            response.getWriter().write("");
            return;
        }

        switch (type) {
            case "user":
                if (dao.checkUserExist(value) != null) message = "Tên đăng nhập đã tồn tại!";
                break;
            case "email":
                if (dao.checkEmailExist(value) != null) message = "Email này đã được sử dụng!";
                break;
            case "phone":
                if (dao.checkPhoneExist(value) != null) message = "Số điện thoại này đã được sử dụng!";
                break;
        }

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(message);
    }
}