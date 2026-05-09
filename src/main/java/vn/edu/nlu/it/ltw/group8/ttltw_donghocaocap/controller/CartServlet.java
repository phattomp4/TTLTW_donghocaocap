package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Voucher;

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


        String voucherCode = request.getParameter("voucherCode");
        double discount = 0;
        AdminDAO adminDAO = new AdminDAO();

        if (voucherCode != null && !voucherCode.isEmpty()) {
            Voucher v = adminDAO.getVoucherByCode(voucherCode);

            if (v != null) {
                String checkStatus = v.validateVoucher(totalMoney);

                if ("OK".equals(checkStatus)) {
                    if (v.getDiscountType().equals("Percent")) {
                        discount = totalMoney * (v.getDiscountValue() / 100);
                        if (v.getMaxDiscount() > 0 && discount > v.getMaxDiscount()) {
                            discount = v.getMaxDiscount();
                        }
                    } else {
                        discount = v.getDiscountValue();
                    }
                    session.setAttribute("appliedVoucher", v);
                    session.setAttribute("discount", discount);
                    request.setAttribute("voucherMessage", "Áp dụng mã thành công!");
                } else {
                    request.setAttribute("voucherMessage", checkStatus);
                    session.removeAttribute("appliedVoucher");
                    session.removeAttribute("discount");
                }
            } else {
                request.setAttribute("voucherMessage", "Mã giảm giá không tồn tại!");
                session.removeAttribute("appliedVoucher");
                session.removeAttribute("discount");
            }
        } else {
            Voucher appliedVoucher = (Voucher) session.getAttribute("appliedVoucher");
            Double sessionDiscount = (Double) session.getAttribute("discount");
            if (appliedVoucher != null && sessionDiscount != null) {
                if ("OK".equals(appliedVoucher.validateVoucher(totalMoney))) {
                    discount = sessionDiscount;
                } else {
                    session.removeAttribute("appliedVoucher");
                    session.removeAttribute("discount");
                    request.setAttribute("voucherMessage", "Đơn hàng không còn đủ điều kiện áp dụng mã này.");
                }
            }
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