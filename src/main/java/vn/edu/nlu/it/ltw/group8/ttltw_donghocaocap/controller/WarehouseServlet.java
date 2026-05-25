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

        // Lấy danh sách sản phẩm cho Bảng theo dõi và Dropdown nhập kho
        List<Product> listProducts = productDao.getAllProducts(); // chưa có hàm này
        // Lấy lịch sử biến động
        List<Map<String, Object>> history = warehouseDao.getInventoryHistory();

        request.setAttribute("listProducts", listProducts);
        request.setAttribute("history", history);
        request.getRequestDispatcher("/admin/warehouse.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("import".equals(action)) {
            int productId = Integer.parseInt(request.getParameter("productId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            double price = Double.parseDouble(request.getParameter("importPrice"));

            WarehouseDAO dao = new WarehouseDAO();
            dao.importProduct(productId, quantity, price);

            response.sendRedirect("warehouse?msg=success");
        }
    }
}