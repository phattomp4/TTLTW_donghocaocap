package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.StatisticDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.OrderDetail;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        AdminDAO dao = new AdminDAO();

        if ("approveCancel".equals(action) || "rejectCancel".equals(action)) {
            try {
                int orderId = Integer.parseInt(request.getParameter("id"));
                if ("approveCancel".equals(action)) {
                    dao.updateOrderStatusWithLog(orderId, "Cancelled");
                    OrderDAO orderDao = new OrderDAO();
                    orderDao.rollbackStock(orderId);
                } else if ("rejectCancel".equals(action)) {
                    dao.updateOrderStatus(orderId, "Shipping");
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

        String period = request.getParameter("period");
        if (period == null || period.trim().isEmpty()) {
            period = "month";
        }

        StatisticDAO statDao = new StatisticDAO();

        double revenue = statDao.getRevenueByPeriod(period);
        int totalOrders = statDao.countOrdersByPeriod(period);
        int totalUsers = dao.countUsers();
        int pendingProcessingCount = statDao.countPendingAndProcessingOrders();
        int cancelRequestsCount = statDao.countOrderStatus("Request Cancel");
        int lowStockCount = statDao.countLowStockProducts(3);

        List<Order> recentOrders = statDao.getRecentOrders(5);
        List<Product> topSellingWatches = statDao.getTopSellingProducts(5);


        Map<String, Double> revenueChartData = statDao.getRevenueTimelineData(period);
        StringBuilder chartLabels = new StringBuilder();
        StringBuilder chartData = new StringBuilder();
        int index = 0;
        for (Map.Entry<String, Double> entry : revenueChartData.entrySet()) {
            if (index > 0) {
                chartLabels.append(",");
                chartData.append(",");
            }
            chartLabels.append("'").append(entry.getKey()).append("'");
            chartData.append(entry.getValue());
            index++;
        }

        Map<String, Double> brandChartData = statDao.getBrandRevenueShare();
        StringBuilder brandLabels = new StringBuilder();
        StringBuilder brandData = new StringBuilder();
        index = 0;
        for (Map.Entry<String, Double> entry : brandChartData.entrySet()) {
            if (index > 0) {
                brandLabels.append(",");
                brandData.append(",");
            }
            brandLabels.append("'").append(entry.getKey()).append("'");
            brandData.append(entry.getValue());
            index++;
        }

        request.setAttribute("revenue", revenue);
        request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalUsers", totalUsers);

        request.setAttribute("pendingProcessingCount", pendingProcessingCount);
        request.setAttribute("cancelRequestsCount", cancelRequestsCount);
        request.setAttribute("lowStockCount", lowStockCount);

        request.setAttribute("recentOrders", recentOrders);
        request.setAttribute("topSellingWatches", topSellingWatches);

        request.setAttribute("chartLabels", chartLabels.toString());
        request.setAttribute("chartData", chartData.toString());
        request.setAttribute("brandLabels", brandLabels.toString());
        request.setAttribute("brandData", brandData.toString());

        request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
    }
}