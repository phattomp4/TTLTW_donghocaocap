package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Voucher;
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

        Double discount = (Double) session.getAttribute("discount");
        if (discount == null) discount = 0.0;

        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("discount", discount);
        request.setAttribute("finalTotal", totalMoney - discount);

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

        String action = request.getParameter("action");
        AdminDAO adminDAO = new AdminDAO();
        OrderDAO orderDAO = new OrderDAO();
        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }


        if ("apply_voucher".equals(action)) {
            String code = request.getParameter("voucherCode");
            Voucher v = adminDAO.getVoucherByCode(code);

            if (v == null) {
                request.setAttribute("voucherError", "Mã giảm giá không tồn tại!");
            } else {
                String checkStatus = v.validateVoucher(totalMoney);
                if (!"OK".equals(checkStatus)) {
                    request.setAttribute("voucherError", checkStatus);
                } else if (adminDAO.hasUserUsedVoucher(acc.getId(), code)) {
                    request.setAttribute("voucherError", "Bạn đã sử dụng mã giảm giá này rồi!");
                } else {
                    double discountAmount = 0;
                    if ("Fixed".equals(v.getDiscountType())) {
                        discountAmount = v.getDiscountValue();
                    } else if ("Percent".equals(v.getDiscountType())) {
                        discountAmount = totalMoney * (v.getDiscountValue() / 100);
                        if (v.getMaxDiscount() > 0 && discountAmount > v.getMaxDiscount()) {
                            discountAmount = v.getMaxDiscount();
                        }
                    }

                    session.setAttribute("appliedVoucher", v);
                    session.setAttribute("discount", discountAmount);
                    request.setAttribute("voucherSuccess", "Áp dụng mã thành công!");
                }
            }

            doGet(request, response);
            return;
        }


        String addressIdRaw = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");

        Double discount = (Double) session.getAttribute("discount");
        if (discount == null) discount = 0.0;

        try {
            int addressId = Integer.parseInt(addressIdRaw);
            double finalTotal = totalMoney - discount;

            boolean result = orderDAO.insertOrder(acc, cart, addressId, paymentMethod, finalTotal, discount);

            if (result) {

                Voucher appliedVoucher = (Voucher) session.getAttribute("appliedVoucher");
                if (appliedVoucher != null) {


                    int newOrderId = 0;

                    adminDAO.processVoucherAfterOrder(acc.getId(), appliedVoucher.getCode(), newOrderId, finalTotal);
                }

                session.removeAttribute("cart");
                session.removeAttribute("cartCount");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("discount");

                response.sendRedirect("order-history");
            } else {
                request.setAttribute("error", "Đặt hàng thất bại. Vui lòng thử lại!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}