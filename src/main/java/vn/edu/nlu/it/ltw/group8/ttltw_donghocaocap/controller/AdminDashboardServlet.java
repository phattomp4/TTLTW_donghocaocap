package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        AdminDAO adminDao = new AdminDAO();

        if ("approveCancel".equals(action) || "rejectCancel".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));

                if ("approveCancel".equals(action)) {
                    adminDao.updateOrderStatusWithLog(orderId, "Cancelled");
                } else if ("rejectCancel".equals(action)) {
                    adminDao.updateOrderStatus(orderId, "Shipping");
                }

                String source = request.getParameter("source");
                if ("detail_page".equals(source)) {
                    response.sendRedirect("order-detail?id=" + orderId);
                } else {
                    response.sendRedirect("dashboard");
                }
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double revenue = adminDao.getTotalRevenue();
        int totalOrders = adminDao.countOrders();
        int totalUsers = adminDao.countUsers();
        List<Order> listOrders = adminDao.getAllOrders();

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
            try {
                int orderId = Integer.parseInt(request.getParameter("orderId"));
                String nextStatus = request.getParameter("status");

                AdminDAO dao = new AdminDAO();
                dao.updateOrderStatusWithLog(orderId, nextStatus);

                String source = request.getParameter("source");
                if ("detail_page".equals(source)) {
                    response.sendRedirect("order-detail?id=" + orderId);
                } else {
                    response.sendRedirect("dashboard");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("dashboard");
            }
        }
    }
}