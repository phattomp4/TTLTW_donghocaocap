package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;

import java.io.IOException;
import java.util.regex.Pattern;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String USER_REGEX = "^[a-zA-Z0-9]{5,20}$";
    private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@gmail\\.com$";
    private static final String PHONE_REGEX = "^(0|84)(3|5|7|8|9)([0-9]{8})$";
    private static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        String user = (request.getParameter("user") != null) ? request.getParameter("user").trim() : "";
        String pass = request.getParameter("pass");
        String re_pass = request.getParameter("re_pass");
        String fullName = (request.getParameter("fullname") != null) ? request.getParameter("fullname").trim() : "";
        String email = (request.getParameter("email") != null) ? request.getParameter("email").trim() : "";
        String phone = (request.getParameter("phone") != null) ? request.getParameter("phone").trim() : "";

        if (user.isEmpty() || pass == null || pass.isEmpty() || email.isEmpty() || phone.isEmpty() || fullName.isEmpty()) {
            sendError(request, response, "Vui lòng nhập đầy đủ thông tin!", user, fullName, email, phone);
            return;
        }

        if (!Pattern.compile(USER_REGEX).matcher(user).matches()) {
            sendError(request, response, "Tên đăng nhập phải từ 5-20 ký tự và không chứa ký tự đặc biệt!", user, fullName, email, phone);
            return;
        }

        if (!Pattern.compile(EMAIL_REGEX).matcher(email).matches()) {
            sendError(request, response, "Vui lòng sử dụng đúng định dạng Gmail (ví dụ: @gmail.com)!", user, fullName, email, phone);
            return;
        }

        if (!Pattern.compile(PHONE_REGEX).matcher(phone).matches()) {
            sendError(request, response, "Số điện thoại không hợp lệ!", user, fullName, email, phone);
            return;
        }

        if (!Pattern.compile(PASSWORD_REGEX).matcher(pass).matches()) {
            sendError(request, response, "Mật khẩu tối thiểu 8 ký tự, bao gồm chữ hoa, chữ thường, số và ký tự đặc biệt!", user, fullName, email, phone);
            return;
        }

        if (!pass.equals(re_pass)) {
            sendError(request, response, "Mật khẩu xác nhận không khớp!", user, fullName, email, phone);
            return;
        }

        UserDAO dao = new UserDAO();

        if (dao.checkUserExist(user) != null) {
            sendError(request, response, "Tên đăng nhập đã tồn tại!", user, fullName, email, phone);
            return;
        }
        if (dao.checkEmailExist(email) != null) {
            sendError(request, response, "Email này đã được sử dụng!", user, fullName, email, phone);
            return;
        }

        boolean success = dao.signup(user, pass, fullName, email, phone);

        if (success) {
            HttpSession session = request.getSession();
            session.setAttribute("mess_success", "Đăng ký thành công mời bạn đăng nhập");
            response.sendRedirect("login.jsp");
        } else {
            sendError(request, response, "Lỗi hệ thống (DB), vui lòng thử lại sau!", user, fullName, email, phone);
        }
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String message,
                           String user, String name, String email, String phone) throws ServletException, IOException {
        request.setAttribute("mess", message);
        request.setAttribute("oldUser", user);
        request.setAttribute("oldFullName", name);
        request.setAttribute("oldEmail", email);
        request.setAttribute("oldPhone", phone);
        request.getRequestDispatcher("register.jsp").forward(request, response);
    }
}