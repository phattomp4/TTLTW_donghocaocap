package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.StatisticDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO adminDao = new AdminDAO();
        StatisticDAO statDao = new StatisticDAO();

        String action = request.getParameter("action");

        if (action != null && ("approveCancel".equals(action) || "rejectCancel".equals(action))) {
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

        Map<String, Integer> orderStats = statDao.getOrderStatusStats();
        List<Product> lowStockList = statDao.getLowStockProducts();
        List<Order> recentOrders = statDao.getRecentOrders(5);

        request.setAttribute("revenue", revenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);
        request.setAttribute("listOrders", listOrders);
        request.setAttribute("orderStats", orderStats);
        request.setAttribute("lowStockList", lowStockList);
        request.setAttribute("recentOrders", recentOrders);

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}