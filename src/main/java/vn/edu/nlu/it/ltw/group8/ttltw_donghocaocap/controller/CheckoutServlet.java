package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.VNPayService;
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
            response.sendRedirect("login");
            return;
        }

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("cart");
            return;
        }

        prepareCheckoutData(request, acc, cart);

        request.getRequestDispatcher("user/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");


        if (acc == null || cart == null || cart.isEmpty()) {
            response.sendRedirect("home");
            return;
        }

        String addressIdRaw = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");

        if (addressIdRaw == null || addressIdRaw.isEmpty()) {
            handleError(request, response, acc, cart, "Vui lòng chọn địa chỉ nhận hàng!");
            return;
        }

        if (paymentMethod == null || paymentMethod.isEmpty()) {
            handleError(request, response, acc, cart, "Vui lòng chọn phương thức thanh toán!");
            return;
        }

        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }
        double discount = 0; // chua co voucher
        double finalAmount = totalMoney - discount;

        try {
            int addressId = Integer.parseInt(addressIdRaw);
            OrderDAO orderDAO = new OrderDAO();

            int orderId = orderDAO.insertOrder(acc, cart, addressId, paymentMethod, finalAmount, discount);

            if (orderId > 0) {
                session.removeAttribute("cart");
                session.removeAttribute("cartCount");

                if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
                    String vnpayUrl = VNPayService.createPaymentUrl(finalAmount, orderId, request);
                    response.sendRedirect(vnpayUrl);
                } else {
                    response.sendRedirect("order-history?msg=success");
                }
            } else {
                handleError(request, response, acc, cart, "Sản phẩm trong kho đã hết hoặc hệ thống gặp sự cố. Vui lòng thử lại!");
            }

        } catch (NumberFormatException e) {
            handleError(request, response, acc, cart, "Dữ liệu địa chỉ không hợp lệ!");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private void prepareCheckoutData(HttpServletRequest request, User acc, List<CartItem> cart) {
        UserDAO userDAO = new UserDAO();
        List<UserAddress> listAddress = userDAO.getAddresses(acc.getId());

        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }

        request.setAttribute("listAddress", listAddress);
        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("finalTotal", totalMoney);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, User acc, List<CartItem> cart, String errorMsg) throws ServletException, IOException {
        prepareCheckoutData(request, acc, cart);
        request.setAttribute("error", errorMsg);
        request.getRequestDispatcher("user/checkout.jsp").forward(request, response);
    }
}