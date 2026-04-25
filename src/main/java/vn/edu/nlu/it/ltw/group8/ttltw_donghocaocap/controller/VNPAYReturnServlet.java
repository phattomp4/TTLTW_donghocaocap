package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.FileLogger;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.VNPayService;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "VNPAYReturnServlet", urlPatterns = {"/vnpay-return"})
public class VNPAYReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = request.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        String signValue = VNPayService.hashAllFields(fields);

        if (signValue.equals(vnp_SecureHash)) {
            int orderId = Integer.parseInt(request.getParameter("vnp_TxnRef"));
            String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
            String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
            double amount = Double.parseDouble(request.getParameter("vnp_Amount")) / 100;

            OrderDAO dao = new OrderDAO();

            if ("00".equals(vnp_ResponseCode)) {
                try {
                    boolean updated = dao.confirmPaid(orderId, vnp_TransactionNo);
                    if (updated) {
                        request.setAttribute("msg", "Thanh toán thành công đơn hàng #" + orderId);
                        request.setAttribute("status", "success");
                    }
                } catch (Exception e) {
                    FileLogger.log(orderId, amount, vnp_TransactionNo, "Lỗi DB khi cập nhật Paid: " + e.getMessage());
                    request.setAttribute("msg", "Giao dịch thành công nhưng hệ thống đang bận cập nhật. Vui lòng liên hệ Admin.");
                    request.setAttribute("status", "warning");
                }
            } else {

                dao.updatePaymentStatus(orderId, "Failed", null);
                request.setAttribute("msg", "Giao dịch không thành công. Mã lỗi: " + vnp_ResponseCode);
                request.setAttribute("status", "failed");
                request.setAttribute("allowRepay", true);
            }
            request.setAttribute("orderId", orderId);
            request.setAttribute("amount", amount);
            request.setAttribute("transId", vnp_TransactionNo);
        } else {
            request.setAttribute("msg", "Chữ ký không hợp lệ! Dữ liệu có dấu hiệu bị can thiệp.");
            request.setAttribute("status", "error");
        }

        request.getRequestDispatcher("user/payment_result.jsp").forward(request, response);
    }
}