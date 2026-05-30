package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.ProductDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.WarehouseDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/admin/warehouse")
public class WarehouseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ProductDAO productDao = new ProductDAO();
        WarehouseDAO warehouseDao = new WarehouseDAO();

        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        String sort = request.getParameter("sort");
        if (sort == null) sort = "default";

        int page = 1;
        int pageSize = 10;
        try {
            if (request.getParameter("page") != null) {
                page = Integer.parseInt(request.getParameter("page"));
            }
        } catch (Exception e) { page = 1; }

        int offset = (page - 1) * pageSize;

        List<Product> listProducts = productDao.getInventoryProducts(keyword, sort, offset, pageSize);
        int totalProducts = productDao.countInventoryProducts(keyword);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        List<Product> allProducts = productDao.getAllProducts();

        List<Map<String, Object>> history = warehouseDao.getInventoryHistory();

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("allProducts", allProducts); // Dùng cho datalist
        request.setAttribute("history", history);

        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("searchKeyword", keyword);
        request.setAttribute("currentSort", sort);

        request.getRequestDispatcher("/admin/warehouse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        WarehouseDAO dao = new WarehouseDAO();

        if ("importMulti".equals(action)) {
            String[] productIds = request.getParameterValues("productIds[]");
            String[] quantities = request.getParameterValues("quantities[]");
            String[] prices = request.getParameterValues("prices[]");
            
            if (productIds != null) {
                for (int i = 0; i < productIds.length; i++) {
                    if (productIds[i] != null && !productIds[i].isEmpty()) {
                        int pId = Integer.parseInt(productIds[i]);
                        int qty = Integer.parseInt(quantities[i]);
                        double price = Double.parseDouble(prices[i]);

                        dao.importProduct(pId, qty, price);
                    }
                }
            }
            response.sendRedirect("warehouse?msg=success");
        }
    }
}