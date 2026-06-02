package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.filter;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

@WebFilter("/*")
public class AutoLoginFilter implements Filter {

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        String requestURI = req.getRequestURI();

        User user = (User) session.getAttribute("acc");

        if (user == null) {
            Cookie[] cookies = req.getCookies();
            String token = null;
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals("remember_token")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token != null) {
                UserDAO dao = new UserDAO();
                user = dao.getUserByToken(token);
                if (user != null) {
                    session.setAttribute("acc", user);
                }
            }
        }

        if (user != null) {
            if (session.getAttribute("favoriteProductIds") == null || session.getAttribute("favCount") == null) {
                FavoriteDAO favDao = new FavoriteDAO();
                java.util.List<Integer> favIds = favDao.getFavoriteProductIds(user.getId());

                session.setAttribute("favoriteProductIds", favIds);
                session.setAttribute("favCount", favIds.size());
            }

            if (session.getAttribute("cartCount") == null) {
                vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CartDAO cartDao = new vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.CartDAO();
                java.util.List<vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem> cart = cartDao.getCartByUserId(user.getId());
                int totalCartCount = 0;
                if (cart != null) {
                    for (vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.CartItem item : cart) {
                        totalCartCount += item.getQuantity();
                    }
                }
                session.setAttribute("cart", cart);
                session.setAttribute("cartCount", totalCartCount);
            }
        }

        boolean isLoggedIn = (session.getAttribute("acc") != null);
        boolean isLoginRequest = requestURI.endsWith("login.jsp") || requestURI.endsWith("login");
        boolean isRegisterRequest = requestURI.endsWith("register.jsp") || requestURI.endsWith("register");
        boolean isForgotRequest = requestURI.endsWith("userpass.jsp") || requestURI.endsWith("forgotPassword");

        if (isLoggedIn && (isLoginRequest || isRegisterRequest || isForgotRequest)) {
            res.sendRedirect("home");
            return;
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {}
    public void destroy() {}
}