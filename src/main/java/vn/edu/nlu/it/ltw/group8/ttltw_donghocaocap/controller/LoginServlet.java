package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.FavoriteDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.GoogleAccount;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String checkValue = request.getParameter("checkAccount");
        if (checkValue != null) {
            response.setContentType("text/plain;charset=UTF-8");
            UserDAO dao = new UserDAO();
            if (dao.getUserByAccount(checkValue) == null) {
                response.getWriter().write("Tài khoản không tồn tại!");
            }
            return;
        }

        HttpSession session = request.getSession();
        String mess = (String) session.getAttribute("mess");
        if (mess != null) {
            request.setAttribute("mess", mess);
            session.removeAttribute("mess");
        }

        String code = request.getParameter("code");
        if (code != null && !code.isEmpty()) {
            processRequest(request, response);
        } else {
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.contains("login") && !referer.contains("register")) {
                session.setAttribute("redirect_url", referer);
            }
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String u = request.getParameter("username");
        String p = request.getParameter("password");
        String r = request.getParameter("remember");

        if (u == null || u.trim().isEmpty() || p == null || p.trim().isEmpty()) {
            out.write("ERROR|Vui lòng điền đầy đủ thông tin!");
            return;
        }

        UserDAO dao = new UserDAO();
        User user = dao.getUserByAccount(u);
        HttpSession session = request.getSession();

        Long lockTime = (Long) session.getAttribute("lockTime");
        if (lockTime != null && lockTime > System.currentTimeMillis()) {
            long minutesLeft = ((lockTime - System.currentTimeMillis()) / 1000 / 60) + 1;
            out.write("ERROR|Tài khoản bị khóa. Thử lại sau " + minutesLeft + " phút!");
            return;
        }

        if (user == null) {
            out.write("ERROR|Tài khoản không tồn tại!");
            return;
        }

        if (BCrypt.checkpw(p, user.getPassword())) {
            if (!"Active".equals(user.getStatus())) {
                String newToken = java.util.UUID.randomUUID().toString();
                dao.refreshVerificationToken(user.getEmail(), newToken);
                vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.EmailUtil.sendActivationEmail(user.getEmail(), newToken);

                out.write("INFO|Tài khoản chưa kích hoạt. Mã mới đã được gửi vào Email!");
                return;
            }

            session.removeAttribute("failedAttempts");
            session.removeAttribute("lockTime");
            session.setAttribute("acc", user);
            session.setMaxInactiveInterval(60 * 60);
            FavoriteDAO favDao = new FavoriteDAO();
            int favCount = favDao.countFavorites(user.getId());
            session.setAttribute("favCount", favCount);

            if ("ON".equals(r)) {
                String token = java.util.UUID.randomUUID().toString();
                dao.updateRememberToken(user.getUsername(), token);
                Cookie tokenCookie = new Cookie("remember_token", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(tokenCookie);
            }

            String redirectUrl = (String) session.getAttribute("redirect_url");
            String target = (redirectUrl != null) ? redirectUrl : "home";
            if (redirectUrl != null) session.removeAttribute("redirect_url");

            out.write("SUCCESS|" + target);
        } else {
            Integer failedCount = (Integer) session.getAttribute("failedAttempts");
            if (failedCount == null) failedCount = 0;
            failedCount++;
            session.setAttribute("failedAttempts", failedCount);

            if (failedCount >= 5) {
                session.setAttribute("lockTime", System.currentTimeMillis() + (15 * 60 * 1000));
                out.write("ERROR|Sai quá 5 lần. Tài khoản bị khóa 15 phút.");
            } else {
                out.write("ERROR|Sai mật khẩu! Bạn còn " + (5 - failedCount) + " lần thử.");
            }

            session.setAttribute("mess", errorMsg);
            response.sendRedirect("login");
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String code = request.getParameter("code");
        try {
            String accessToken = GoogleLoginServlet.getToken(code);
            GoogleAccount acc = GoogleLoginServlet.getUserInfo(accessToken);
            if (acc != null) {
                UserDAO dao = new UserDAO();
                User existingUser = dao.checkEmailExist(acc.getEmail());
                HttpSession session = request.getSession();
                session.removeAttribute("failedAttempts");
                session.removeAttribute("lockTime");

                FavoriteDAO favDao = new FavoriteDAO();

                if (existingUser != null) {
                    if (!"Active".equals(existingUser.getStatus())) {
                        dao.activateAccount(existingUser.getVerificationToken());
                        existingUser = dao.checkEmailExist(acc.getEmail());
                    }
                    session.setAttribute("acc", existingUser);

                    int favCount = favDao.countFavorites(existingUser.getId());
                    session.setAttribute("favCount", favCount);

                    String redirectUrl = (String) session.getAttribute("redirect_url");
                    response.sendRedirect(redirectUrl != null ? redirectUrl : "home");
                } else {
                    String autoUsername = acc.getEmail().substring(0, acc.getEmail().indexOf("@"));
                    String randomPassword = java.util.UUID.randomUUID().toString();
                    String googleToken = "GOOGLE_" + java.util.UUID.randomUUID().toString();
                    dao.signup(autoUsername, randomPassword, acc.getName(), acc.getEmail(), "", googleToken);
                    dao.activateAccount(googleToken);

                    User newUser = dao.checkEmailExist(googleEmail);
                    session.setAttribute("acc", newUser);

                    int favCount = favDao.countFavorites(newUser.getId());
                    session.setAttribute("favCount", favCount);

                    response.sendRedirect("home");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login");
        }
    }
}