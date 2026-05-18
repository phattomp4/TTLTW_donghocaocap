package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;

import java.io.IOException;

@WebServlet("/update-user-role")
public class UpdateRoleServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        String newRole = request.getParameter("role");

        AdminDAO adminDAO = new AdminDAO();
        boolean success = adminDAO.updateUserRole(userId, newRole);

        if (success) {
            response.sendRedirect("manage-users?msg=success");
        } else {
            response.sendRedirect("manage-users?msg=error");
        }
    }
}
