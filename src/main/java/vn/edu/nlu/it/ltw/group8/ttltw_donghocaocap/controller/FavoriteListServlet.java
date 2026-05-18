package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.Product;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import java.io.IOException;
import java.util.List;

@WebServlet("/favorite-list")
public class FavoriteListServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = (User) request.getSession().getAttribute("acc");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        FavoriteDAO dao = new FavoriteDAO();
        List<Product> listFavorites = dao.getFavoriteProducts(user.getId());

        request.setAttribute("listFavorites", listFavorites);
        request.getRequestDispatcher("user/favorite.jsp").forward(request, response);
    }
}