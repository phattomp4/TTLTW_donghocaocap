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
        String contextPath = req.getContextPath();

        String pathInsideApp = requestURI.substring(contextPath.length());

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
                User userRemember = dao.getUserByToken(token);
                if (userRemember != null) {
                    session.setAttribute("acc", userRemember);
                    FavoriteDAO favDao = new FavoriteDAO();
                    int favCount = favDao.countFavorites(userRemember.getId());
                    session.setAttribute("favCount", favCount);
                }
            }
        }

        boolean isLoggedIn = (session.getAttribute("acc") != null);

        boolean isLoginRequest = pathInsideApp.equals("/login") || pathInsideApp.equals("/login.jsp");
        boolean isRegisterRequest = pathInsideApp.equals("/register") || pathInsideApp.equals("/register.jsp");
        boolean isForgotRequest = pathInsideApp.equals("/forgotPassword") || pathInsideApp.equals("/userpass.jsp");

        if (isLoggedIn && (isLoginRequest || isRegisterRequest || isForgotRequest)) {
            res.sendRedirect(contextPath + "/home");
            return;
        }

        chain.doFilter(request, response);
    }

    public void init(FilterConfig filterConfig) throws ServletException {}
    public void destroy() {}
}