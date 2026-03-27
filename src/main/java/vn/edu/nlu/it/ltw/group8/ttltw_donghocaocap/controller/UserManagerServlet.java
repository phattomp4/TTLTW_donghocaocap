package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "UserManagerServlet", urlPatterns = {"/admin/user-manager"})
public class UserManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        List<User> list;

        String keyword = request.getParameter("keyword");

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchUsers(keyword.trim());
        } else {
            list = dao.getAllUsers();
        }

        request.setAttribute("listUsers", list);
        request.setAttribute("searchKeyword", keyword);

        request.getRequestDispatcher("/admin/user-manager.jsp").forward(request, response);
    }
}