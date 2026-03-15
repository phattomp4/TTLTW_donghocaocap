package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.EmailUtil;

import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", value = "/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {
    private final UserDAO dao = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setAttribute("step", 1);
        request.getRequestDispatcher("/user/userpass.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        HttpSession session = request.getSession();
        int step = 1;
        if ("send_code".equals(action)) {

            String accountInfo = request.getParameter("account_info");
            if (accountInfo != null) {
                accountInfo = accountInfo.trim();
            }

            String code = String.valueOf((int) (Math.random() * 900000) + 100000);

            if (dao.setResetCode(accountInfo, code)) {

                boolean sent = EmailUtil.sendOTP(accountInfo, code);

                if (sent) {
                    session.setAttribute("resetAccount", accountInfo);
                    step = 2;
                } else {
                    request.setAttribute("error", "Không gửi được email. Vui lòng thử lại.");
                    step = 1;
                }
            } else {
                request.setAttribute("error", "Email không tồn tại trong hệ thống!");
                step = 1;
            }
        } else if ("verify_code".equals(action)) {

            String[] digits = request.getParameterValues("digit");
            String fullCode = (digits != null) ? String.join("", digits) : "";

            String accountInfo = (String) session.getAttribute("resetAccount");

            if (accountInfo == null) {
                request.setAttribute("error", "Phiên làm việc hết hạn, vui lòng làm lại từ đầu.");
                step = 1;
            } else if (dao.verifyCode(accountInfo, fullCode)) {
                step = 3;
            } else {
                request.setAttribute("error", "Mã xác nhận không đúng hoặc đã hết hạn!");
                step = 2;
            }
        } else if ("update_password".equals(action)) {

            String newPass = request.getParameter("new_password");
            String confirmPass = request.getParameter("confirm_password");
            String accountInfo = (String) session.getAttribute("resetAccount");

            if (accountInfo == null) {
                request.setAttribute("error", "Phiên làm việc hết hạn, vui lòng làm lại từ đầu.");
                step = 1;

            } else if (newPass == null || !newPass.equals(confirmPass)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                step = 3;

            } else {
                boolean success = dao.resetPassword(accountInfo, newPass);

                if (success) {
                    session.removeAttribute("resetAccount");
                    request.setAttribute("success",
                            "Đã đổi mật khẩu thành công! Mời bạn đăng nhập.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Không thể đổi mật khẩu. Vui lòng thử lại.");
                    step = 3;
                }
            }
        }
        request.setAttribute("step", step);
        request.getRequestDispatcher("/user/userpass.jsp").forward(request, response);
    }
}
