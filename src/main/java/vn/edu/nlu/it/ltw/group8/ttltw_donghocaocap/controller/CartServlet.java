package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CartDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Voucher;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        CartDAO cartDao = new CartDAO();
        String action = request.getParameter("action");

        if (action != null) {
            try {
                int pid = Integer.parseInt(request.getParameter("pid"));

                if ("update".equals(action)) {
                    int quantity = Integer.parseInt(request.getParameter("quantity"));
                    if (quantity > 0) {
                        int stockLeft = 0;
                        for(vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product p : new vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO().getAllProducts()) {
                            if(p.getId() == pid) { stockLeft = p.getStockQuantity(); break; }
                        }

                        if (quantity > stockLeft) {
                            request.setAttribute("error", "Sản phẩm chỉ còn tối đa " + stockLeft + " chiếc.");
                            quantity = stockLeft;
                        }

                        cartDao.updateCartQuantityDirect(user.getId(), pid, quantity);
                    } else {
                        cartDao.removeCartItemDirect(user.getId(), pid);
                    }
                } else if ("delete".equals(action)) {
                    cartDao.removeCartItemDirect(user.getId(), pid);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<CartItem> cart = cartDao.getCartByUserId(user.getId());

        double totalMoney = 0;
        int totalCount = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
            totalCount += item.getQuantity();
        }

        session.setAttribute("cart", cart);
        session.setAttribute("cartCount", totalCount);

        double discount = 0;
        String voucherCode = request.getParameter("voucherCode");
        AdminDAO adminDao = new AdminDAO();

        if (voucherCode != null && !voucherCode.trim().isEmpty()) {
            Voucher v = adminDao.getVoucherByCode(voucherCode);
            if (v != null) {
                String validateMsg = v.validateVoucher(totalMoney);
                if ("OK".equals(validateMsg)) {
                    if (adminDao.hasUserUsedVoucher(user.getId(), voucherCode)) {
                        request.setAttribute("voucherMessage", "Bạn đã sử dụng mã giảm giá này rồi!");
                        session.removeAttribute("appliedVoucher");
                        session.removeAttribute("discount");
                    } else {
                        if ("Fixed".equals(v.getDiscountType())) {
                            discount = v.getDiscountValue();
                        } else if ("Percent".equals(v.getDiscountType())) {
                            discount = totalMoney * (v.getDiscountValue() / 100);
                            if (v.getMaxDiscount() > 0 && discount > v.getMaxDiscount()) {
                                discount = v.getMaxDiscount();
                            }
                        }
                        session.setAttribute("appliedVoucher", v);
                        session.setAttribute("discount", discount);
                        request.setAttribute("voucherMessage", "Áp dụng mã giảm giá thành công!");
                    }
                } else {
                    request.setAttribute("voucherMessage", validateMsg);
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