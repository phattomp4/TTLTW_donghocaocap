package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserShoppingStats;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.VoucherUsageDTO;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserManagerServlet", urlPatterns = {"/admin/user-manager"})
public class UserManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        String action = request.getParameter("action");
        OrderDAO orderDAO = new OrderDAO();
        VoucherUsageDTO voucherUsageDTO = new VoucherUsageDTO();
        if ("detail".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            User user = dao.getUserById(id);
            UserShoppingStats stats = dao.getCustomerStats(id);

            List<Order> userOrders = orderDAO.getOrdersByUserId(id);

            List<VoucherUsageDTO> userVouchers = dao.getVoucherHistoryByUserId(id);
            request.setAttribute("userVouchers", userVouchers);
            request.setAttribute("user", user);
            request.setAttribute("stats", stats);
            request.getRequestDispatcher("/admin/user-detail.jsp").forward(request, response);
        } else {
            String keyword = request.getParameter("keyword");
            int page = 1;
            int limit = 10;
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
            int offset = (page - 1) * limit;

            List<User> list = dao.getUsersWithPagination(offset, limit, keyword);
            int totalUsers = dao.getTotalUsersCount(keyword);
            int totalPages = (int) Math.ceil((double) totalUsers / limit);

            request.setAttribute("listUsers", list);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("searchKeyword", keyword);
            request.getRequestDispatcher("/admin/user-manager.jsp").forward(request, response);
        }
    }
}