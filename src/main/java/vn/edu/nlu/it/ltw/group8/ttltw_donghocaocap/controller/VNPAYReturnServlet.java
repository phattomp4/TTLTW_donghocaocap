package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.FileLogger;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.VNPayService;

import java.io.IOException;
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
            try {
                int orderId = Integer.parseInt(request.getParameter("vnp_TxnRef"));
                String vnp_ResponseCode = request.getParameter("vnp_ResponseCode");
                String vnp_TransactionNo = request.getParameter("vnp_TransactionNo");
                double amount = Double.parseDouble(request.getParameter("vnp_Amount")) / 100;

                OrderDAO dao = new OrderDAO();
                Order order = dao.getOrderById(orderId);

                if (order == null) {
                    request.setAttribute("msg", "Không tìm thấy thông tin đơn hàng.");
                    request.setAttribute("status", "error");
                } else if ("Paid".equals(order.getPaymentStatus())) {
                    request.setAttribute("msg", "Thanh toán thành công đơn hàng #" + orderId);
                    request.setAttribute("status", "success");
                } else {
                    if ("00".equals(vnp_ResponseCode)) {
                        try {
                            boolean updated = dao.confirmPaid(orderId, vnp_TransactionNo);
                            if (updated) {
                                request.setAttribute("msg", "Thanh toán thành công đơn hàng #" + orderId);
                                request.setAttribute("status", "success");
                            }
                        } catch (Exception e) {
                            FileLogger.log(orderId, amount, vnp_TransactionNo, "DB Crash/Congestion: " + e.getMessage());
                            request.setAttribute("msg", "Giao dịch thành công. Hệ thống đang cập nhật, vui lòng không thanh toán lại.");
                            request.setAttribute("status", "warning");
                        }
                    } else {
                        dao.updatePaymentStatus(orderId, "Failed", vnp_TransactionNo); // Tự động rollback kho
                        request.setAttribute("msg", "Giao dịch thất bại hoặc đã bị hủy.");
                        request.setAttribute("status", "failed");
                        request.setAttribute("allowRepay", true);
                    }
                }

                request.setAttribute("orderId", orderId);
                request.setAttribute("amount", amount);
                request.setAttribute("transId", vnp_TransactionNo);

            } catch (NumberFormatException e) {
                request.setAttribute("msg", "Dữ liệu giao dịch không hợp lệ.");
                request.setAttribute("status", "error");
            }
        } else {
            request.setAttribute("msg", "Chữ ký bảo mật không hợp lệ (Checksum failed).");
            request.setAttribute("status", "error");
        }

        request.getRequestDispatcher("user/payment_result.jsp").forward(request, response);
    }
}