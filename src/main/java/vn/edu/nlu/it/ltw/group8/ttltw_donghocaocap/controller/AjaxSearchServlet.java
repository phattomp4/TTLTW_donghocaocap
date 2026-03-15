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

@WebServlet(name = "AjaxSearchServlet", urlPatterns = {"/ajax-search"})
public class AjaxSearchServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");


        String txt = request.getParameter("txt");
        ProductDAO dao = new ProductDAO();
        List<Product> list = dao.searchProducts(txt);
        request.setAttribute("listSearch", list);
        request.getRequestDispatcher("ajax-result.jsp").forward(request, response);
    }
}