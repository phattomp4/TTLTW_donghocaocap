package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.MenuDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Category;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminMenuServlet", urlPatterns = {"/admin/menu-manager"})
public class AdminMenuServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        MenuDAO dao = new MenuDAO();
        List<Category> listMenus = dao.getAllMenus();
        request.setAttribute("listMenus", listMenus);
        request.getRequestDispatcher("/admin/manage-menu.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        MenuDAO dao = new MenuDAO();

        if ("toggleStatus".equals(action)) {
            // Xử lý bật tắt nhanh bằng AJAX
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            dao.updateStatus(id, status);
            response.getWriter().write("success");

        } else if ("updateOrder".equals(action)) {
            // Xử lý kéo thả thay đổi vị trí menu (tương tự banner)
            String[] ids = request.getParameterValues("ids[]");
            if (ids != null) {
                for (int i = 0; i < ids.length; i++) {
                    dao.updateMenuOrder(Integer.parseInt(ids[i]), i + 1);
                }
                response.getWriter().write("success");
            } else {
                response.getWriter().write("fail");
            }
        }
    }
}