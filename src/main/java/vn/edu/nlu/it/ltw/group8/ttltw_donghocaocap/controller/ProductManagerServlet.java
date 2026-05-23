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
        String action = request.getParameter("action");
        AdminDAO dao = new AdminDAO();

        if ("toggleStatus".equals(action)) {
            try {
                int pid = Integer.parseInt(request.getParameter("pid"));
                int currentStatus = Integer.parseInt(request.getParameter("status"));
                int newStatus = (currentStatus == 1) ? 0 : 1;

                dao.toggleProductStatus(pid, newStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }
            response.sendRedirect("product-manager");
            return;
        }

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
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        AdminDAO dao = new AdminDAO();

        try {
            if ("delete".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("pid"));
                dao.deleteProduct(pid);
            }

            else if ("add".equals(action)) {
                Product p = new Product();
                p.setBrandId(Integer.parseInt(request.getParameter("brandId")));
                p.setName(request.getParameter("name"));
                p.setSku(request.getParameter("sku"));
                p.setDescription(request.getParameter("description"));
                p.setOriginalPrice(Double.parseDouble(request.getParameter("originalPrice")));
                p.setCurrentPrice(Double.parseDouble(request.getParameter("currentPrice")));
                p.setImageUrl(request.getParameter("imageUrl"));
                p.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
                p.setLuxury(request.getParameter("isLuxury") != null);
                p.setIsActive(1);

                dao.insertProduct(p);
            }

            else if ("edit".equals(action)) {
                Product p = new Product();
                p.setId(Integer.parseInt(request.getParameter("pid")));
                p.setBrandId(Integer.parseInt(request.getParameter("brandId")));
                p.setName(request.getParameter("name"));
                p.setSku(request.getParameter("sku"));
                p.setDescription(request.getParameter("description"));
                p.setOriginalPrice(Double.parseDouble(request.getParameter("originalPrice")));
                p.setCurrentPrice(Double.parseDouble(request.getParameter("currentPrice")));
                p.setImageUrl(request.getParameter("imageUrl"));
                p.setStockQuantity(Integer.parseInt(request.getParameter("stockQuantity")));
                p.setLuxury(request.getParameter("isLuxury") != null);
                p.setIsActive(Integer.parseInt(request.getParameter("isActive"))); // Nhận 0 hoặc 1 từ form chỉnh sửa

                dao.updateProduct(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("product-manager");
    }
}