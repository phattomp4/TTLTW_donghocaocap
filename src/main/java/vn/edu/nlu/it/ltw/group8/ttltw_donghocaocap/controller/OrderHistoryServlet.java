package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderHistoryServlet", urlPatterns = {"/order-history"})
public class OrderHistoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        OrderDAO dao = new OrderDAO();

        String status = request.getParameter("status");
        if (status == null || status.isEmpty()) status = "all";

        int page = 1;
        int limit = 5;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        int offset = (page - 1) * limit;

        String action = request.getParameter("action");
        if ("requestCancel".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));
                Order order = dao.getOrderById(orderId);

                if (order != null && order.getUserId() == acc.getId()) {
                    if ("Pending".equals(order.getStatus())) {
                        dao.updateOrderStatus(orderId, "Cancelled", "Khách hàng tự hủy đơn");
                    } else if ("Processing".equals(order.getStatus())) {
                        dao.updateOrderStatus(orderId, "Request Cancel", "Khách hàng gửi yêu cầu hủy đơn");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("order-history?status=" + status + "&page=" + page);
            return;
        }

        List<Order> listOrders = dao.getOrdersWithPagination(acc.getId(), status, offset, limit);
        int totalOrders = dao.countOrdersByStatus(acc.getId(), status);
        int totalPages = (int) Math.ceil((double) totalOrders / limit);

        request.setAttribute("listOrders", listOrders);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentStatus", status);

        request.getRequestDispatcher("user/order-history.jsp").forward(request, response);
    }
}