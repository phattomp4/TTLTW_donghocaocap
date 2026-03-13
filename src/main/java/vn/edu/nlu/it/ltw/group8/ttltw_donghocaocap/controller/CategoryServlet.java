package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;


import java.io.IOException;
import java.util.List;

@WebServlet(name = "CategoryServlet", urlPatterns = {"/category"})
public class CategoryServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        List<Product> list = null;
        String title = "Danh sách sản phẩm";

        String type = request.getParameter("type");

        if ("price".equals(type)) {
            double min = Double.parseDouble(request.getParameter("min"));
            double max = Double.parseDouble(request.getParameter("max"));
            list = dao.getProductsByPriceRange(min, max);
            if (max == -1) title = "Giá trên " + (long)min + " đ";
            else title = "Giá từ " + (long)min + " đ - " + (long)max + " đ";
        }

        else if ("search".equals(type)) {
            String keyword = request.getParameter("keyword");
            list = dao.searchProducts(keyword);
            title = "Kết quả tìm kiếm: " + keyword;
        }

        else if ("brand".equals(type)) {
            String brandName = request.getParameter("name");
            list = dao.getProductsByBrand(brandName);
            title = "Thương hiệu: " + brandName;
        }

        else if ("luxury".equals(type)) {
            list = dao.getLuxuryProducts1();
            title = "Bộ sưu tập Luxury";
        }

        else if ("origin".equals(type)) {
            String origin = request.getParameter("name");
            list = dao.getProductsByOrigin(origin);
            title = "Đồng hồ " + origin;
        }

        else if ("accessories".equals(type)) {
            list = dao.searchProducts("Phụ kiện");
            title = "Phụ kiện đồng hồ";
        }

        else if ("nam".equals(type)) {
            list = dao.getMenProducts();
            title = "Đồng hồ nam";
        }

        else if ("nu".equals(type)) {
            list = dao.getWomenProducts();
            title = "Đồng hồ nữ";
        }

        else if ("collection".equals(type)) {
            String name = request.getParameter("name");

            if (name != null) {
                if (name.toLowerCase().contains("luxury")) {
                    list = dao.getLuxuryProducts();
                    title = "Bộ sưu tập Luxury";
                }
                else if (name.toLowerCase().contains("nhật")) {
                    list = dao.getProductsByOrigin("Nhật");
                    title = "Đồng hồ Nhật Bản";
                }
                else if (name.toLowerCase().contains("thụy")) {
                    list = dao.getProductsByOrigin("Thụy");
                    title = "Đồng hồ Thụy Sỹ";
                }
                else {
                    list = dao.searchProducts(name);
                    title = name;
                }
            }
        }

        request.setAttribute("listProduct", list);
        request.setAttribute("pageTitle", title);
        request.getRequestDispatcher("product-list.jsp").forward(request, response);
    }
}