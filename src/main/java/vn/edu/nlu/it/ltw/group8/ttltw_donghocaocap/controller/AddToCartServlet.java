package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
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
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "AddToCartServlet", urlPatterns = {"/add-to-cart"})
public class AddToCartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("acc");


            if (user != null && "Admin".equals(user.getRole())) {
                String ajax = request.getParameter("ajax");
                if ("true".equals(ajax)) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("Admin không được phép mua hàng! Vui lòng dùng tài khoản Khách.");
                } else {
                    response.sendRedirect("admin/dashboard");
                }
                return;
            }

            String pidRaw = request.getParameter("pid");
            String quantityRaw = request.getParameter("quantity");


            if (pidRaw == null || pidRaw.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Product ID");
                return;
            }

            int productId = Integer.parseInt(pidRaw);
            int quantity = 1;
            try {
                if (quantityRaw != null && !quantityRaw.isEmpty()) {
                    quantity = Integer.parseInt(quantityRaw);
                }
            } catch (NumberFormatException e) { }

            ProductDAO dao = new ProductDAO();
            Product pCheck = dao.getProductById(productId);

            if (pCheck == null || pCheck.getStockQuantity() <= 0) {
                if ("true".equals(request.getParameter("ajax"))) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Sản phẩm này đã hết hàng!");
                } else {
                    response.sendRedirect("detail?pid=" + productId);
                }
                return;
            }

            if (quantity > pCheck.getStockQuantity()) {
                if ("true".equals(request.getParameter("ajax"))) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("Chỉ còn " + pCheck.getStockQuantity() + " sản phẩm trong kho!");
                    return;
                }

                quantity = pCheck.getStockQuantity();
            }




            List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");
            if (cart == null) {
                cart = new ArrayList<>();
            }


            boolean found = false;
            for (CartItem item : cart) {
                if (item.getProduct().getId() == productId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                Product product = dao.getProductById(productId);
                if (product != null) {
                    cart.add(new CartItem(product, quantity));
                }
            }


            session.setAttribute("cart", cart);


            int totalCount = 0;
            for (CartItem item : cart) totalCount += item.getQuantity();
            session.setAttribute("cartCount", totalCount);

            String ajax = request.getParameter("ajax");
            String action = request.getParameter("action");


            if ("true".equals(ajax)) {
                response.setContentType("text/plain");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(String.valueOf(totalCount));
                return;
            }


            else if ("buynow".equals(action)) {
                if (user == null) {
                    session.setAttribute("redirectAfterLogin", "checkout");
                    response.sendRedirect("login.jsp");
                } else {
                    response.sendRedirect("checkout");
                }
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
}