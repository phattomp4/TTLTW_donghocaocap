package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocapcom.vvp.dao.OrderDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
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
            response.sendRedirect("cart"); // Giỏ trống thì về trang giỏ hàng
            return;
        }

        UserDAO userDAO = new UserDAO();
        List<UserAddress> listAddress = userDAO.getAddresses(acc.getId());
        request.setAttribute("listAddress", listAddress);

        // tính tổng tiền
        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }

        request.setAttribute("totalMoney", totalMoney);
        request.setAttribute("finalTotal", totalMoney); // sau này có voucher thì trừ ở đây

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

        // Lấy thông tin từ Form
        String addressIdRaw = request.getParameter("addressId");
        String paymentMethod = request.getParameter("paymentMethod");

        // Tính lại tổng tiền (backend phải tự tính lại, không tin tưởng hoàn toàn số liệu từ frontend gửi lên)
        double totalMoney = 0;
        for (CartItem item : cart) {
            totalMoney += item.getTotalPrice();
        }
        double discount = 0; // Xử lý logic Voucher sau nếu cần

        try {
            int addressId = Integer.parseInt(addressIdRaw);

            OrderDAO orderDAO = new OrderDAO();
            boolean result = orderDAO.insertOrder(acc, cart, addressId, paymentMethod, totalMoney - discount, discount);

            if (result) {
                // Đặt hàng thành công
                session.removeAttribute("cart");      // Xóa giỏ hàng
                session.removeAttribute("cartCount"); // Reset số lượng icon

                // Chuyển sang trang lịch sử đơn hàng
                response.sendRedirect("order-history");
            } else {
                // Thất bại
                request.setAttribute("error", "Đặt hàng thất bại. Vui lòng thử lại!");
                doGet(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }
}