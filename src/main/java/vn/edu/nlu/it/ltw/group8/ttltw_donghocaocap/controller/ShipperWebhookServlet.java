package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ShipperWebhookServlet", urlPatterns = {"/api/shipper/webhook"})
public class ShipperWebhookServlet extends HttpServlet {
    private final OrderDAO orderDAO = new OrderDAO();
    private static final String SHIPPER_SECRET_TOKEN = "NHOM8_DONGHOCAOCAP_SECRET_2026";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        try {
            String clientToken = request.getHeader("Authorization");
            if (clientToken == null || !clientToken.equals(SHIPPER_SECRET_TOKEN)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                out.print("{\"status\": \"error\", \"message\": \"Không có quyền truy cập cổng tín hiệu này!\"}");
                return;
            }

            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader reader = request.getReader()) {
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }

            JsonObject jsonObject = new JsonParser().parse(sb.toString()).getAsJsonObject();
            int orderId = jsonObject.get("order_id").getAsInt();
            String shipperStatus = jsonObject.get("shipping_status").getAsString();

            String systemStatus = "";
            String logNote = "";

            if ("DELIVERED".equalsIgnoreCase(shipperStatus)) {
                systemStatus = "Completed";
                logNote = "Hệ thống tự động: Đối tác vận chuyển xác nhận ĐÃ GIAO HÀNG THÀNH CÔNG.";
            } else if ("SHIPPING".equalsIgnoreCase(shipperStatus)) {
                systemStatus = "Shipping";
                logNote = "Hệ thống tự động: Đơn vị vận chuyển xác nhận ĐANG ĐI GIAO HÀNG.";
            }

            if (!systemStatus.isEmpty()) {
                boolean isSuccess = orderDAO.updateOrderStatus(orderId, systemStatus, logNote);
                if (isSuccess) {
                    out.print("{\"status\": \"success\", \"message\": \"Hệ thống đã nhận tín hiệu và cập nhật đơn hàng!\"}");
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    out.print("{\"status\": \"error\", \"message\": \"Cập nhật Database thất bại hoặc không tìm thấy đơn hàng.\"}");
                }
            } else {
                out.print("{\"status\": \"ignored\", \"message\": \"Trạng thái vận chuyển không cần xử lý.\"}");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"status\": \"error\", \"message\": \"Lỗi xử lý hệ thống nội bộ.\"}");
        }
    }
}