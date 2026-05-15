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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        Map<String, String> fields = new HashMap<>();

        Enumeration<String> params = request.getParameterNames();

        while (params.hasMoreElements()) {
            String fieldName = params.nextElement();
            String fieldValue = request.getParameter(fieldName);

            if (fieldValue != null && !fieldValue.isEmpty()) {
                fields.put(fieldName, fieldValue);
            }
        }

        String secureHash = request.getParameter("vnp_SecureHash");

        fields.remove("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");

        String signValue = VNPayService.hashAllFields(fields);

        if (!signValue.equals(secureHash)) {
            request.setAttribute("msg", "Chữ ký bảo mật không hợp lệ.");
            request.setAttribute("status", "error");

            request.getRequestDispatcher("/user/payment_result.jsp")
                    .forward(request, response);
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("vnp_TxnRef"));
            String responseCode = request.getParameter("vnp_ResponseCode");
            String transactionNo = request.getParameter("vnp_TransactionNo");

            double amount = 0;

            try {
                amount = Double.parseDouble(request.getParameter("vnp_Amount")) / 100.0;
            } catch (Exception ignored) {
            }

            OrderDAO orderDAO = new OrderDAO();
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                request.setAttribute("msg", "Không tìm thấy đơn hàng.");
                request.setAttribute("status", "error");
            } else if ("Paid".equalsIgnoreCase(order.getPaymentStatus())) {

                request.setAttribute("msg", "Thanh toán thành công đơn hàng #" + orderId);
                request.setAttribute("status", "success");

            } else {

                if ("00".equals(responseCode)) {

                    try {

                        boolean updated = orderDAO.confirmPaid(orderId, transactionNo);

                        if (updated) {
                            request.setAttribute("msg", "Thanh toán thành công đơn hàng #" + orderId);
                            request.setAttribute("status", "success");
                        } else {
                            request.setAttribute("msg", "Không thể cập nhật trạng thái thanh toán.");
                            request.setAttribute("status", "warning");
                        }

                    } catch (Exception e) {

                        FileLogger.log(
                                orderId,
                                amount,
                                transactionNo,
                                "DB Crash/Congestion: " + e.getMessage()
                        );

                        request.setAttribute(
                                "msg",
                                "Giao dịch đã thành công. Hệ thống đang xử lý cập nhật đơn hàng."
                        );

                        request.setAttribute("status", "warning");
                    }

                } else {

                    orderDAO.updatePaymentStatus(orderId, "Failed", transactionNo);

                    request.setAttribute("msg", "Giao dịch thất bại hoặc đã bị hủy.");
                    request.setAttribute("status", "failed");
                    request.setAttribute("allowRepay", true);
                }
            }

            request.setAttribute("orderId", orderId);
            request.setAttribute("amount", amount);
            request.setAttribute("transId", transactionNo);

        } catch (Exception e) {

            request.setAttribute("msg", "Dữ liệu giao dịch không hợp lệ.");
            request.setAttribute("status", "error");
        }

        request.getRequestDispatcher("/user/payment_result.jsp")
                .forward(request, response);
    }
}