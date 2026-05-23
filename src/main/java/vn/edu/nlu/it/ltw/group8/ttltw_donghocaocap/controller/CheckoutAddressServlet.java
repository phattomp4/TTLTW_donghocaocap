package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;

@WebServlet(name = "CheckoutAddressServlet", urlPatterns = {"/checkout-address"})
public class CheckoutAddressServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        try {

            String name = request.getParameter("new_name");
            String phone = request.getParameter("new_phone");
            String province = request.getParameter("provinceName");
            String district = request.getParameter("districtName");
            String ward = request.getParameter("wardName");
            String streetDetail = request.getParameter("streetDetail");

            if (name == null || name.trim().isEmpty()
                    || phone == null || phone.trim().isEmpty()
                    || province == null || province.trim().isEmpty()
                    || district == null || district.trim().isEmpty()
                    || ward == null || ward.trim().isEmpty()
                    || streetDetail == null || streetDetail.trim().isEmpty()) {

                response.sendRedirect("checkout?error=invalid");
                return;
            }

            UserDAO dao = new UserDAO();

            dao.addAddress(
                    acc.getId(),
                    name.trim(),
                    phone.trim(),
                    province.trim(),
                    district.trim(),
                    ward.trim(),
                    streetDetail.trim()
            );

            response.sendRedirect("checkout");

        } catch (Exception e) {

            e.printStackTrace();
            response.sendRedirect("checkout?error=system");
        }
    }
}