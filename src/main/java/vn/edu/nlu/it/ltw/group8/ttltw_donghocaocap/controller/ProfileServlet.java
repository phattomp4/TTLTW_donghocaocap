package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDAO dao = new UserDAO();
        String action = request.getParameter("action");
        String idRaw = request.getParameter("id");
        if ("delete".equals(action) && idRaw != null) {

            dao.deleteAddress(Integer.parseInt(idRaw));
            session.setAttribute("mess", "Đã xóa địa chỉ thành công!");
            response.sendRedirect("profile");
            return;
        }
        List<UserAddress> listAddress = dao.getAddresses(acc.getId());

        request.setAttribute("listAddress", listAddress);
        request.getRequestDispatcher("user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        if ("updateInfo".equals(action)) {
            String fullName = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");
            String address = request.getParameter("address");

            acc.setFullName(fullName);
            acc.setEmail(email);
            acc.setPhone(phone);
            acc.setGender(gender);
            acc.setAddress(address);

            dao.updateAccountProfile(acc);

            session.setAttribute("acc", acc);

            session.setAttribute("mess", "Cập nhật hồ sơ thành công!");
        }

        else if ("addAddress".equals(action)) {
            String name = request.getParameter("new_name");
            String phone = request.getParameter("new_phone");
            String addr = request.getParameter("new_address");

            dao.addAddress(acc.getId(), name, phone, addr);

            session.setAttribute("mess", "Thêm địa chỉ mới thành công!");
        }

        else if ("editAddress".equals(action)) {
            try {
                int addrId = Integer.parseInt(request.getParameter("edit_id"));
                String name = request.getParameter("edit_name");
                String phone = request.getParameter("edit_phone");
                String street = request.getParameter("edit_address");

                dao.updateUserAddress(addrId, name, phone, street);
                session.setAttribute("mess", "Cập nhật địa chỉ thành công!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("profile");
    }
}