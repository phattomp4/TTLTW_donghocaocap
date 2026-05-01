package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import java.io.IOException;

@WebServlet("/toggle-favorite")
public class ToggleFavoriteServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("acc");

        if (user == null) {
            response.getWriter().write("unauthorized");
            return;
        }

        int pid = Integer.parseInt(request.getParameter("pid"));
        FavoriteDAO dao = new FavoriteDAO();

        boolean isAdded = dao.toggleFavorite(user.getId(), pid);

        Integer favCount = (Integer) session.getAttribute("favCount");
        if (favCount == null) {
            favCount = dao.getFavoriteProducts(user.getId()).size();
        } else {
            if (isAdded) {
                favCount++;
            } else {
                favCount = Math.max(0, favCount - 1);
            }
        }
        session.setAttribute("favCount", favCount);

        response.getWriter().write(isAdded ? "added" : "removed");
    }
}