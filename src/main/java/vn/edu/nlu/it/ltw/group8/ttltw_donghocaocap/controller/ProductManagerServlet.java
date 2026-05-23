package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.AdminDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductManagerServlet", urlPatterns = {"/admin/product-manager"})
public class ProductManagerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        AdminDAO adminDao = new AdminDAO();

        if ("toggleStatus".equals(action)) {
            try {
                int pid = Integer.parseInt(request.getParameter("pid"));
                int currentStatus = Integer.parseInt(request.getParameter("status"));

                int newStatus = (currentStatus == 1) ? 0 : 1;

                adminDao.toggleProductStatus(pid, newStatus);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String keyword = request.getParameter("keyword") != null ? request.getParameter("keyword") : "";
            String gender = request.getParameter("gender") != null ? request.getParameter("gender") : "";
            String brandId = request.getParameter("brandId") != null ? request.getParameter("brandId") : "";
            String priceRange = request.getParameter("priceRange") != null ? request.getParameter("priceRange") : "";
            String page = request.getParameter("page") != null ? request.getParameter("page") : "1";

            response.sendRedirect("product-manager?page=" + page + "&keyword=" + keyword + "&gender=" + gender + "&brandId=" + brandId + "&priceRange=" + priceRange);
            return;
        }

        String keyword = request.getParameter("keyword");
        String gender = request.getParameter("gender");
        String brandId = request.getParameter("brandId");
        String priceRange = request.getParameter("priceRange");

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

        List<Product> list = adminDao.getProductsWithFilter(keyword, gender, brandId, priceRange, currentPage, pageSize);

        int totalProducts = adminDao.getTotalProductsWithFilter(keyword, gender, brandId, priceRange);

        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
        if (totalPages == 0) {
            totalPages = 1;
        }

        ProductDAO productDao = new ProductDAO();
        Map<String, String> brandMap = productDao.getAllBrandsWithLogo();

        request.setAttribute("listProducts", list);
        request.setAttribute("brandMap", brandMap);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("selectedGender", gender);
        request.setAttribute("selectedBrand", brandId);
        request.setAttribute("selectedPrice", priceRange);

        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("/admin/product-manager.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        AdminDAO adminDao = new AdminDAO();

        try {

            if ("delete".equals(action)) {
                int pid = Integer.parseInt(request.getParameter("pid"));
                adminDao.deleteProduct(pid);
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
                adminDao.insertProduct(p);
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
                p.setIsActive(Integer.parseInt(request.getParameter("isActive"))); // Nhận 0 hoặc 1 từ form sửa

                adminDao.updateProduct(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect("product-manager");
    }
}