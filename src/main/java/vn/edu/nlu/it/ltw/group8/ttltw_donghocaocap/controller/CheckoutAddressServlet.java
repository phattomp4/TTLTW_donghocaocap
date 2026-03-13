package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CheckoutAddressServlet", urlPatterns = {"/checkout-address"})
public class CheckoutAddressServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {
            String name = request.getParameter("new_name");
            String phone = request.getParameter("new_phone");
            String address = request.getParameter("new_address");

            UserDAO dao = new UserDAO();
            dao.addAddress(acc.getId(), name, phone, address);

            response.sendRedirect("checkout");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("checkout?error=1");
        }
    }
}