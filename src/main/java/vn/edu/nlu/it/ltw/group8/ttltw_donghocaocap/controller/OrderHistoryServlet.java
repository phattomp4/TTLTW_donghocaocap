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

        // Lấy danh sách đơn hàng
        OrderDAO dao = new OrderDAO();
        List<Order> listOrders = dao.getOrdersByUserId(acc.getId());

        // hủy đơn
        String action = request.getParameter("action");
        if ("requestCancel".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));
                Order order = dao.getOrderById(orderId);

                // Chỉ hủy đơn của chính mình và đúng trạng thái mới cho hủy
                if (order != null && order.getUserId() == acc.getId()) {
                    if ("Pending".equals(order.getStatus()) || "Processing".equals(order.getStatus())) {
                        dao.updateOrderStatus(orderId, "Request Cancel");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Load lại trang để thấy trạng thái mới
            response.sendRedirect("order-history");
            return;
        }
        request.setAttribute("listOrders", listOrders);
        request.getRequestDispatcher("user/order-history.jsp").forward(request, response);
    }
}