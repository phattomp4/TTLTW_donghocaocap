package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }


        String action = request.getParameter("action");
        if (action != null) {
            int pid = Integer.parseInt(request.getParameter("pid"));


            CartItem target = null;
            for (CartItem item : cart) {
                if (item.getProduct().getId() == pid) {
                    target = item;
                    break;
                }
            }

            if (target != null) {
                if ("delete".equals(action)) {
                    cart.remove(target);
                } else if ("inc".equals(action)) {
                    target.setQuantity(target.getQuantity() + 1);
                } else if ("dec".equals(action)) {
                    if (target.getQuantity() > 1) {
                        target.setQuantity(target.getQuantity() - 1);
                    } else {
                        cart.remove(target);
                    }
                }
            }


            int totalCount = 0;
            for (CartItem item : cart) totalCount += item.getQuantity();
            session.setAttribute("cartCount", totalCount);
            response.sendRedirect("cart");
            return;
        }


        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }


        String voucher = request.getParameter("voucherCode");
        double discount = 0;
        if ("GIAM10".equals(voucher)) {
            discount = totalMoney * 0.1;
            request.setAttribute("voucherMessage", "Áp dụng mã GIAM10 thành công!");
        } else if (voucher != null && !voucher.isEmpty()) {
            request.setAttribute("voucherMessage", "Mã giảm giá không hợp lệ.");
        }

        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("discount", discount);
        request.setAttribute("finalTotal", totalMoney - discount);

        request.getRequestDispatcher("user/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}