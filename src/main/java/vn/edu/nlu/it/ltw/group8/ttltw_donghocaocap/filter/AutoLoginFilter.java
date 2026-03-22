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
                User userRemember = dao.getUserByToken(token);
                if (userRemember != null) {
                    session.setAttribute("acc", userRemember);
                }
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

