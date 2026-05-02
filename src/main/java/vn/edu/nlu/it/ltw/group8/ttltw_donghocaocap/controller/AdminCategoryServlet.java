package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CategoryDAO;

import java.io.IOException;

@WebServlet(name = "AdminCategoryServlet", urlPatterns = {"/admin/category-manager"})
public class AdminCategoryServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();

        if ("delete".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deleteCategory(id);
            clearMenuCache(request);
            response.sendRedirect("category-manager");
            return;
        }

        if ("deletePrice".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            dao.deletePriceRange(id);
            clearMenuCache(request);
            response.sendRedirect("category-manager");
            return;
        }

        if ("toggleStatus".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            boolean status = Boolean.parseBoolean(request.getParameter("status"));
            dao.updateCategoryStatus(id, status);
            clearMenuCache(request);
            response.sendRedirect("category-manager");
            return;
        }

        request.setAttribute("listCats", dao.getAllCategories());
        request.setAttribute("menuPrices", dao.getAllPriceRanges());
        request.getRequestDispatcher("category-manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        CategoryDAO dao = new CategoryDAO();

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            int parentId = Integer.parseInt(request.getParameter("parentId"));
            int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
            boolean isActive = request.getParameter("isActive") != null;
            dao.addCategory(name, parentId, sortOrder, isActive);
        } else if ("update".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            int parentId = Integer.parseInt(request.getParameter("parentId"));
            int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
            boolean isActive = request.getParameter("isActive") != null;
            dao.updateCategory(id, name, parentId, sortOrder, isActive);
        } else if ("updateOrder".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
            dao.updateCategoryOrder(id, sortOrder);
        } else if ("addPrice".equals(action)) {
            String label = request.getParameter("label");
            double min = Double.parseDouble(request.getParameter("min"));
            double max = Double.parseDouble(request.getParameter("max"));
            dao.addPriceRange(label, min, max);
        } else if ("updatePrice".equals(action)) {
            int id = Integer.parseInt(request.getParameter("id"));
            String label = request.getParameter("label");
            double min = Double.parseDouble(request.getParameter("min"));
            double max = Double.parseDouble(request.getParameter("max"));
            dao.updatePriceRange(id, label, min, max);
        }

        clearMenuCache(request);
        response.sendRedirect("category-manager");
    }

    private void clearMenuCache(HttpServletRequest request) {
        request.getServletContext().removeAttribute("menuBrands");
        request.getServletContext().removeAttribute("menuCollections");
        request.getServletContext().removeAttribute("menuAccessories");
        request.getServletContext().removeAttribute("menuPrices");
    }
}