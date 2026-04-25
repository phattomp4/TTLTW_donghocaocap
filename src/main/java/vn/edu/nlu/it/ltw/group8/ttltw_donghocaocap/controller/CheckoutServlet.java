package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.VNPayService; // FIX: Import Service thanh toán
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        UserDAO userDAO = new UserDAO();
        List<UserAddress> listAddress = userDAO.getAddresses(acc.getId());
        request.setAttribute("listAddress", listAddress);

        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }

        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("finalTotal", totalMoney);

        request.getRequestDispatcher("user/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (acc == null || cart == null) {
            response.sendRedirect("home");
            return;
        }

        String addressIdRaw = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");

        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }
        double discount = 0; // Xử lý voucher (xử lý sau)

        try {
            int addressId = Integer.parseInt(addressIdRaw);
            OrderDAO orderDAO = new OrderDAO();
            int orderId = orderDAO.insertOrder(acc, cart, addressId, paymentMethod, totalMoney - discount, discount);

            if (orderId > 0) {
                session.removeAttribute("cart");
                session.removeAttribute("cartCount");

                if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
                    String vnpayUrl = VNPayService.createPaymentUrl(totalMoney - discount, orderId, request);
                    response.sendRedirect(vnpayUrl);
                } else {
                    response.sendRedirect("order-history?msg=success");
                }
            } else {
                request.setAttribute("error", "Đặt hàng thất bại. Sản phẩm có thể đã hết hàng hoặc lỗi hệ thống!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}