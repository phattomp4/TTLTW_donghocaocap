package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "OrderDetailServlet", urlPatterns = {"/order-detail"})
public class OrderDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            int orderId = Integer.parseInt(request.getParameter("id"));
            OrderDAO orderDAO = new OrderDAO();
            UserDAO userDAO = new UserDAO();

            Order order = orderDAO.getOrderById(orderId);

            if (order == null || order.getUserId() != acc.getId()) {
                response.sendRedirect("order-history");
                return;
            }

            UserAddress address = userDAO.getAddressById(order.getShippingAddressId());

            List<OrderDetail> details = orderDAO.getOrderDetails(orderId);

            List<OrderLog> logs = orderDAO.getOrderLog(orderId);

            request.setAttribute("order", order);
            request.setAttribute("address", address);
            request.setAttribute("details", details);
            request.setAttribute("logs", logs);

            request.getRequestDispatcher("user/order-detail.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("order-history");
        }
    }
}