package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CategoryDAO;

import java.io.IOException;

@WebServlet("/admin/category-manager")
public class AdminCategoryServlet extends HttpServlet {

    private void clearMenuCache(HttpServletRequest request) {
        request.getServletContext().removeAttribute("menuBrands");
        request.getServletContext().removeAttribute("menuCollections");
        request.getServletContext().removeAttribute("menuAccessories");
        request.getServletContext().removeAttribute("menuPrices");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDAO dao = new CategoryDAO();
        String action = request.getParameter("action");
        String idParam = request.getParameter("id");

        try {
            if ("delete".equals(action) && idParam != null) {
                dao.deleteCategory(Integer.parseInt(idParam));
                clearMenuCache(request);
                response.sendRedirect("category-manager");
                return;
            }
            else if ("toggleStatus".equals(action) && idParam != null) {
                boolean status = Boolean.parseBoolean(request.getParameter("status"));
                dao.toggleCategoryStatus(Integer.parseInt(idParam), status);
                clearMenuCache(request);
                response.sendRedirect("category-manager");
                return;
            }
            else if ("deletePrice".equals(action) && idParam != null) {
                dao.deletePriceRange(Integer.parseInt(idParam));
                clearMenuCache(request);
                response.sendRedirect("category-manager");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        request.setAttribute("listCats", dao.getAllCategoriesForAdmin());
        request.setAttribute("menuPrices", dao.getAllPriceRanges());
        request.getRequestDispatcher("/admin/category-manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CategoryDAO dao = new CategoryDAO();
        String action = request.getParameter("action");

        try {
            if ("add".equals(action) || "update".equals(action)) {
                String name = request.getParameter("name");
                int parentId = Integer.parseInt(request.getParameter("parentId"));
                int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
                boolean isActive = request.getParameter("isActive") != null;

                if ("add".equals(action)) {
                    dao.addCategory(name, parentId, sortOrder, isActive);
                } else {
                    int id = Integer.parseInt(request.getParameter("id"));
                    dao.updateCategory(id, name, parentId, sortOrder, isActive);
                }
                clearMenuCache(request);
            }
            else if ("updateOrder".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                int sortOrder = Integer.parseInt(request.getParameter("sortOrder"));
                dao.updateCategorySortOrder(id, sortOrder);
                clearMenuCache(request);
                return;
            }
            else if ("addPrice".equals(action) || "updatePrice".equals(action)) {
                String label = request.getParameter("label");
                double min = Double.parseDouble(request.getParameter("min"));
                double max = Double.parseDouble(request.getParameter("max"));

                if ("addPrice".equals(action)) {
                    dao.addPriceRange(label, min, max);
                } else {
                    int id = Integer.parseInt(request.getParameter("id"));
                    dao.updatePriceRange(id, label, min, max);
                }
                clearMenuCache(request);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("category-manager");
    }
}