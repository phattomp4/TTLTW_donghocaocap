package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Order;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminUserDetailServlet", urlPatterns = {"/admin/user-detail"})
public class AdminUserDetailServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String idRaw = request.getParameter("id");
            if (idRaw != null) {
                int userId = Integer.parseInt(idRaw);

                UserDAO userDAO = new UserDAO();
                OrderDAO orderDAO = new OrderDAO();

                User user = userDAO.getUserById(userId);


                List<Order> userOrders = orderDAO.getOrdersByUserId(userId);


                List<UserAddress> listAddress = userDAO.getAddresses(userId);

                request.setAttribute("user", user);
                request.setAttribute("userOrders", userOrders);
                request.setAttribute("listAddress", listAddress); // Gửi sang JSP

                request.getRequestDispatcher("/admin/user-detail.jsp").forward(request, response);
            } else {
                response.sendRedirect("user-manager");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("user-manager");
        }
    }
}