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
        AdminDAO adminDAO = new AdminDAO();

        // 1. Xử lý chức năng ẩn hiện (toggleStatus) nếu có yêu cầu
        if ("toggleStatus".equals(action)) {
            int pid = Integer.parseInt(request.getParameter("pid"));
            int status = Integer.parseInt(request.getParameter("status"));
            adminDAO.toggleProductStatus(pid, status); // Cập nhật trạng thái xuống DB

            // Sau khi xử lý xong, redirect về lại trang quản lý để tránh trùng lặp dữ liệu
            response.sendRedirect("product-manager");
            return;
        }

        // 2. Xử lý Phân trang và Bộ lọc hiển thị dữ liệu
        // 2. Xử lý Phân trang và Bộ lọc hiển thị dữ liệu
        int pageSize = 10;
        int currentPage = 1;

        String pageRaw = request.getParameter("page");
        if (pageRaw != null && !pageRaw.isEmpty()) {
            currentPage = Integer.parseInt(pageRaw);
        }

// Lấy tham số và lọc sạch giá trị rác
        String keyword = request.getParameter("keyword");
        if (keyword == null || keyword.trim().isEmpty() || keyword.equalsIgnoreCase("null")) keyword = "";

        String gender = request.getParameter("gender");
        if (gender == null || gender.trim().isEmpty() || gender.equalsIgnoreCase("null")) gender = "0";

        String brandId = request.getParameter("brandId");
        if (brandId == null || brandId.trim().isEmpty() || brandId.equalsIgnoreCase("null")) brandId = "0";

        String priceRange = request.getParameter("priceRange");
        if (priceRange == null || priceRange.trim().isEmpty() || priceRange.equalsIgnoreCase("null")) priceRange = "0";
        // Lấy danh sách sản phẩm theo bộ lọc và phân trang từ AdminDAO hoàn chỉnh đã sửa
        List<Product> listProducts = adminDAO.getProductsWithFilter(keyword, gender, brandId, priceRange, currentPage, pageSize);

        // Tính toán tổng số lượng sản phẩm thỏa mãn điều kiện lọc để chia trang
        int totalProducts = adminDAO.getTotalProductsWithFilter(keyword, gender, brandId, priceRange);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        // 3. Đẩy toàn bộ biến lên Request để file JSP nhận diện chuẩn xác 100%
        request.setAttribute("listProducts", listProducts);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        // Giữ lại trạng thái các bộ lọc để thanh phân trang không bị mất điều kiện tìm kiếm
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("selectedGender", gender);
        request.setAttribute("selectedBrand", brandId);
        request.setAttribute("selectedPrice", priceRange);

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