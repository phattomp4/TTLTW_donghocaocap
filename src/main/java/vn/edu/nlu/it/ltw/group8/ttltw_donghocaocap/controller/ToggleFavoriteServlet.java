package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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

        List<Integer> favIds = (List<Integer>) session.getAttribute("favoriteProductIds");

        if (favIds == null) {
            favIds = dao.getFavoriteProductIds(user.getId());
            if (favIds == null) favIds = new ArrayList<>();
        }

        if (isAdded) {
            if (!favIds.contains(pid)) {
                favIds.add(pid);
            }
        } else {
            favIds.remove(Integer.valueOf(pid));
        }

        session.setAttribute("favoriteProductIds", favIds);
        session.setAttribute("favCount", favIds.size());

        response.getWriter().write(isAdded ? "added" : "removed");
    }
}