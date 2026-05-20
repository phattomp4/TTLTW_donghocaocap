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

        int pageSize = 10;
        int currentPage = 1;

        String pageRaw = request.getParameter("page");
        if (pageRaw != null && !pageRaw.isEmpty()) {
            try {
                currentPage = Integer.parseInt(pageRaw);
            } catch (NumberFormatException e) {
                currentPage = 1;
            }
        }

        String keyword = request.getParameter("keyword");
        int totalProducts = 0;

        if (keyword != null && !keyword.trim().isEmpty()) {
            String trimmedKeyword = keyword.trim();

            list = dao.searchProductsByName(trimmedKeyword);
            totalProducts = list.size();

            int fromIndex = (currentPage - 1) * pageSize;
            int toIndex = Math.min(fromIndex + pageSize, totalProducts);
            if (fromIndex < totalProducts) {
                list = list.subList(fromIndex, toIndex);
            }
        } else {
            list = dao.getProductsByPage(currentPage, pageSize);
            totalProducts = dao.getTotalProducts();
        }

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }

        request.setAttribute("listProducts", list);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

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