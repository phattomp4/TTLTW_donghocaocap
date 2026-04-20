package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.OrderDetail;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("approveCancel".equals(action) || "rejectCancel".equals(action)) {
            String redirectUrl = "dashboard"; 
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));
                OrderDAO orderDao = new OrderDAO();
                AdminDAO adminDao = new AdminDAO();

                if ("approveCancel".equals(action)) {
                    adminDao.updateOrderStatus(orderId, "Cancelled");

                    ProductDAO productDao = new ProductDAO();
                    List<OrderDetail> details = orderDao.getOrderDetails(orderId);
                    for (OrderDetail d : details) {
                        productDao.updateStock(d.getProductId(), d.getQuantity());
                    }
                } else {
                    adminDao.updateOrderStatus(orderId, "Processing");
                }

                String source = request.getParameter("source");
                if ("detail_page".equals(source)) {
                    redirectUrl = "order-detail?id=" + orderId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(redirectUrl);
            return;
        }

        AdminDAO dao = new AdminDAO();
        double revenue = dao.getTotalRevenue();
        int totalOrders = dao.countOrders();
        int totalUsers = dao.countUsers();
        List<Order> listOrders = dao.getAllOrders();

        request.setAttribute("revenue", revenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("listOrders", listOrders);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("update_status".equals(action)) {
            String redirectUrl = "dashboard";

            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String status = request.getParameter("status");

                AdminDAO dao = new AdminDAO();
                dao.updateOrderStatus(orderId, status);

                String source = request.getParameter("source");
                if ("detail_page".equals(source)) {
                    redirectUrl = "order-detail?id=" + orderId;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            response.sendRedirect(redirectUrl);
        }
    }
}