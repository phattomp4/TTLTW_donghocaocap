package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.EmailUtil;
import java.io.IOException;

@WebServlet(name = "ForgotPasswordServlet", urlPatterns = {"/forgotPassword"})
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
            if (accountInfo != null) accountInfo = accountInfo.trim();
            Long lastSent = (Long) session.getAttribute("lastSentTime");
            long currentTime = System.currentTimeMillis();
            long waitTime = 120 * 1000;

            if (lastSent != null && (currentTime - lastSent) < waitTime) {
                long secondsLeft = (waitTime - (currentTime - lastSent)) / 1000;
                request.setAttribute("error", "Thao tác quá nhanh! Vui lòng đợi " + secondsLeft + " giây để gửi lại.");
                step = "true".equals(request.getParameter("isResend")) ? 2 : 1;
            } else if (dao.checkEmailExist(accountInfo) != null) {
                String code = String.valueOf((int) (Math.random() * 900000) + 100000);
                boolean sent = EmailUtil.sendOTP(accountInfo, code);

                if (sent) {
                    session.setAttribute("resetOTP", code);
                    session.setAttribute("resetAccount", accountInfo);
                    session.setAttribute("otpExpiry", currentTime + (2 * 60 * 1000));
                    session.setAttribute("lastSentTime", currentTime);

                    if ("true".equals(request.getParameter("isResend"))) {
                        request.setAttribute("success", "Mã xác thực mới đã được gửi!");
                    }
                    step = 2;
                } else {
                    request.setAttribute("error", "Lỗi hệ thống không thể gửi email. Vui lòng thử lại.");
                    step = 1;
                }
            } else {
                request.setAttribute("error", "Email không tồn tại trong hệ thống!");
                step = 1;
            }

        } else if ("verify_code".equals(action)) {
            String userOtp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("resetOTP");
            Long expiry = (Long) session.getAttribute("otpExpiry");

            if (sessionOtp == null || expiry == null) {
                request.setAttribute("error", "Phiên làm việc hết hạn, vui lòng làm lại.");
                step = 1;
            } else if (System.currentTimeMillis() > expiry) {
                request.setAttribute("error", "Mã OTP đã hết hạn. Vui lòng gửi lại mã.");
                step = 2;
            } else if (sessionOtp.equals(userOtp)) {
                step = 3;
            } else {
                request.setAttribute("error", "Mã xác nhận không đúng!");
                step = 2;
            }

        } else if ("update_password".equals(action)) {
            String newPass = request.getParameter("new_password");
            String confirmPass = request.getParameter("confirm_password");
            String accountInfo = (String) session.getAttribute("resetAccount");

            if (accountInfo == null) {
                request.setAttribute("error", "Phiên làm việc hết hạn.");
                step = 1;
            } else if (newPass == null || !newPass.equals(confirmPass)) {
                request.setAttribute("error", "Mật khẩu xác nhận không khớp!");
                step = 3;
            } else {
                boolean success = dao.resetPassword(accountInfo, newPass);
                if (success) {
                    session.removeAttribute("resetOTP");
                    session.removeAttribute("resetAccount");
                    session.removeAttribute("otpExpiry");
                    session.removeAttribute("lastSentTime");
                    request.setAttribute("success", "Đã đổi mật khẩu thành công! Mời bạn đăng nhập.");
                    request.getRequestDispatcher("/login.jsp").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Lỗi cập nhật mật khẩu.");
                    step = 3;
                }
            }
        }

        request.setAttribute("step", step);
        request.getRequestDispatcher("/user/userpass.jsp").forward(request, response);
    }
}