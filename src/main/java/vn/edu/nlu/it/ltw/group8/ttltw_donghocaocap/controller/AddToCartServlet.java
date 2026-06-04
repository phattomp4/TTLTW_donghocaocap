package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CartDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

@WebServlet(name = "AddToCartServlet", urlPatterns = {"/add-to-cart"})
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");

            String ajax = request.getParameter("ajax");
            String action = request.getParameter("action");

            if (user == null) {
                if ("buynow".equals(action)) {
                    session.setAttribute("redirectAfterLogin", "checkout");
                }
                response.sendRedirect("login");
                return;
            }

            if ("Admin".equals(user.getRole())) {
                if ("true".equals(ajax)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Admin không thể mua hàng!");
                } else {
                    response.sendRedirect("home");
                }
                return;
            }

            int productId = Integer.parseInt(request.getParameter("pid"));
            int qty = 1;
            try {
                if (request.getParameter("quantity") != null) {
                    qty = Integer.parseInt(request.getParameter("quantity"));
                }
            } catch (Exception e) { qty = 1; }

            ProductDAO pDao = new ProductDAO();
            CartDAO cartDao = new CartDAO();

            Product realProduct = null;
            int stockLeft = 0;
            for(Product p : pDao.getAllProducts()) {
                if(p.getId() == productId) {
                    realProduct = p;
                    stockLeft = p.getStockQuantity();
                    break;
                }
            }

            // xử lý riêng nút mua ngay
            if ("buynow".equals(action)) {
                if (qty > stockLeft) {
                    response.sendRedirect("cart?error=out_of_stock");
                    return;
                }

                // tạo giỏ hàng ảo chỉ chứa 1 sản phẩm
                List<CartItem> buyNowCart = new ArrayList<>();
                CartItem item = new CartItem();
                item.setProductId(productId);
                item.setQuantity(qty);
                item.setProduct(realProduct);
                buyNowCart.add(item);

                // đè tạm giỏ hàng ảo lên Session và gán cờ isBuyNow
                session.setAttribute("cart", buyNowCart);
                session.setAttribute("isBuyNow", true);

                response.sendRedirect("checkout");
                return; // ngắt luồng, ko lưu vào db
            }


            // thêm vào giỏ thông thường
            int currentQtyInCart = 0;
            List<CartItem> existingCart = cartDao.getCartByUserId(user.getId());
            for(CartItem item : existingCart) {
                if(item.getProductId() == productId) {
                    currentQtyInCart = item.getQuantity();
                    break;
                }
            }

            if (currentQtyInCart + qty > stockLeft) {
                if ("true".equals(ajax)) {
                    response.setStatus(400);
                    response.setContentType("text/plain");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("Sản phẩm chỉ còn " + stockLeft + " chiếc trong kho!");
                    return;
                } else {
                    response.sendRedirect("cart?error=out_of_stock");
                    return;
                }
            }

            cartDao.addToCart(user.getId(), productId, qty);
            pDao.incrementProductScore(productId, 3);

            // đồng bộ lại giỏ hàng thực lên Session
            List<CartItem> cart = cartDao.getCartByUserId(user.getId());
            int totalCount = 0;
            for (CartItem item : cart) totalCount += item.getQuantity();

            session.setAttribute("cart", cart);
            session.setAttribute("cartCount", totalCount);
            session.removeAttribute("isBuyNow"); // xóa cờ ảo

            if ("true".equals(ajax)) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(String.valueOf(totalCount));
                return;
            } else {
                String referer = request.getHeader("Referer");
                response.sendRedirect(referer != null ? referer : "home");
            }

        } catch (Exception e) {
            e.printStackTrace();
            if ("true".equals(request.getParameter("ajax"))) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server Error: " + e.getMessage());
            } else {
                response.sendRedirect("home");
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}