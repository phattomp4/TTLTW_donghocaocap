package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ProductManagerServlet", urlPatterns = {"/admin/product-manager"})
public class ProductManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AdminDAO dao = new AdminDAO();
        List<Product> list;

        String keyword = request.getParameter("keyword");

        if (keyword != null && !keyword.trim().isEmpty()) {
            list = dao.searchProductsByName(keyword.trim());
        } else {
            list = dao.getAllProducts();
        }
        request.setAttribute("listProducts", list);
        request.setAttribute("searchKeyword", keyword);
        request.getRequestDispatcher("/admin/product-manager.jsp").forward(request, response);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("delete".equals(action)) {
            try {
                int pid = Integer.parseInt(request.getParameter("pid"));
                AdminDAO dao = new AdminDAO();
                dao.deleteProduct(pid);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("product-manager");
        }
    }
}