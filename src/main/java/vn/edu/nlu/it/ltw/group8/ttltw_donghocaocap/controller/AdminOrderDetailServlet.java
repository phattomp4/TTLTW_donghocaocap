package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.OrderDetail;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminOrderDetailServlet", urlPatterns = {"/admin/order-detail"})
public class AdminOrderDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();

            String action = request.getParameter("action");
            if (action != null) {
                if ("approveCancel".equals(action)) {
                    orderDAO.updateOrderStatus(orderId, "Cancelled", "Quản trị viên đã đồng ý hủy đơn hàng");
                } else if ("rejectCancel".equals(action)) {
                    orderDAO.updateOrderStatus(orderId, "Processing", "Quản trị viên từ chối hủy, tiếp tục chuẩn bị hàng");
                }

                response.sendRedirect("order-detail?id=" + orderId);
                return;
            }

            UserDAO userDAO = new UserDAO();
            Order order = orderDAO.getOrderById(orderId);

            if (order == null) {
                response.sendRedirect("dashboard");
                return;
            }

            UserAddress address = userDAO.getAddressById(order.getShippingAddressId());
            List<OrderDetail> details = orderDAO.getOrderDetails(orderId);
            User customer = userDAO.getUserById(order.getUserId());

            request.setAttribute("customer", customer);
            request.setAttribute("order", order);
            request.setAttribute("address", address);
            request.setAttribute("details", details);

            request.getRequestDispatcher("/admin/order-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard");
        }
    }
}