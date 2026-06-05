package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/apply-voucher")
public class ApplyVoucherServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String code = request.getParameter("code");
        HttpSession session = request.getSession();

        // Giả sử lấy tổng tiền tạm tính từ session (tùy thuộc vào cách bạn lưu giỏ hàng)
        // Ví dụ: Long totalMoney = (Long) request.getAttribute("totalMoney");
        // Ở đây lấy tạm con số cứng hoặc tính từ session.cart để minh họa logic:
        // SỬA ĐOẠN NÀY TRONG SERVLET:
// Thay vì: long totalMoney = 2000000;
// Bạn hãy lấy từ biến totalMoney mà Controller trang thanh toán đã từng tính toán và bỏ vào session:
        Long sessionTotal = (Long) session.getAttribute("totalMoney");
        long totalMoney = (sessionTotal != null) ? sessionTotal : 15000000; // Phòng hờ nếu null
        if (code != null) {
            code = code.trim(); // Xóa bỏ toàn bộ khoảng trắng thừa do người dùng lỡ tay gõ vào
        }
        if ("GIAM20K".equalsIgnoreCase(code)) {
            long discount = 20000;
            long finalTotal = totalMoney - discount;

            session.setAttribute("appliedVoucherCode", code);
            session.setAttribute("discount", discount);
            session.setAttribute("finalTotal", finalTotal);

            response.getWriter().write("{\"success\": true, \"message\": \"Áp dụng mã giảm giá 20k thành công!\"}");
        } else {
            response.getWriter().write("{\"success\": false, \"message\": \"Mã giảm giá không hợp lệ hoặc đã hết hạn!\"}");
        }
    }
}