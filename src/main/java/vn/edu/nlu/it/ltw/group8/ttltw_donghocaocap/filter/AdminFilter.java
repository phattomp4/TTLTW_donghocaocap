package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.filter;

import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();

        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            res.sendRedirect(req.getContextPath() + "/login.jsp");
            return;
        }

        String role = acc.getRole();

        if ("Customer".equalsIgnoreCase(role) || "User".equalsIgnoreCase(role)) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Bạn không có quyền truy cập khu vực quản trị.");
            return;
        }

        if ("Staff".equalsIgnoreCase(role)) {
            String uri = req.getRequestURI().toLowerCase();
            String action = req.getParameter("action");
            if (action == null) action = "";

            boolean isRestrictedUri = uri.contains("/dashboard") ||
                    uri.contains("/export-statistics") ||
                    uri.contains("/voucher-manager") ||
                    uri.contains("/interface-manager") ||
                    uri.contains("/category-manager");

            boolean isRestrictedAction = action.equalsIgnoreCase("delete") ||
                    action.equalsIgnoreCase("cancel") ||
                    action.equalsIgnoreCase("lock") ||
                    action.equalsIgnoreCase("unlock") ||
                    action.equalsIgnoreCase("ban");

            if (isRestrictedUri || isRestrictedAction) {
                res.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied: Tài khoản nhân viên không có quyền thực hiện chức năng này.");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}