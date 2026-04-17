package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.GoogleAccount;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import org.mindrot.jbcrypt.BCrypt;
import java.io.IOException;
import java.util.Date;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        String u = request.getParameter("username");
        String p = request.getParameter("password");
        String r = request.getParameter("remember");
        UserDAO dao = new UserDAO();
        User user = dao.getUserByAccount(u);

        HttpSession session = request.getSession();
        Long lockTime = (Long) session.getAttribute("lockTime");
        if (lockTime != null && lockTime > System.currentTimeMillis()) {
            long minutesLeft = ((lockTime - System.currentTimeMillis()) / 1000 / 60) + 1;
            session.setAttribute("mess", "Tài khoản bị khóa tạm thời. Vui lòng thử lại sau " + minutesLeft + " phút!");
            response.sendRedirect("login");
            return;
        }
        if (user == null) {
            session.setAttribute("mess", "Tài khoản không tồn tại!");
            response.sendRedirect("login");
            return;
        }

        if (BCrypt.checkpw(p, user.getPassword())) {
            if (!"Active".equals(user.getStatus())) {
                session.setAttribute("mess", "Tài khoản chưa kích hoạt! Vui lòng kiểm tra email.");
                response.sendRedirect("login");
                return;
            }

            session.removeAttribute("failedAttempts");
            session.removeAttribute("lockTime");

            session.setAttribute("acc", user);
            session.setMaxInactiveInterval(60 * 60);

            if ("ON".equals(r)) {
                String token = java.util.UUID.randomUUID().toString();
                dao.updateRememberToken(user.getUsername(), token);
                Cookie tokenCookie = new Cookie("remember_token", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 7);
                response.addCookie(tokenCookie);
            }

            String redirectUrl = (String) session.getAttribute("redirect_url");
            if (redirectUrl != null) {
                session.removeAttribute("redirect_url");
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect("home");
            }
        } else {
           // fix: đăng nhập sai, dùng session tối ưu
            Integer failedCount = (Integer) session.getAttribute("failedAttempts");
            if (failedCount == null) failedCount = 0;
            failedCount++;
            session.setAttribute("failedAttempts", failedCount);

            String errorMsg = "Sai mật khẩu!";
            if (failedCount >= 5) {
                session.setAttribute("lockTime", System.currentTimeMillis() + (15 * 60 * 1000));
                errorMsg = "Tài khoản đã bị khóa 15 phút do nhập sai quá 5 lần.";
            } else {
                int attemptsLeft = 5 - failedCount;
                errorMsg += " Bạn còn " + attemptsLeft + " lần thử.";
            }

            session.setAttribute("mess", errorMsg);
            response.sendRedirect("login"); // fix: Redirect để khi F5 không trừ số lần đăng nhập sai
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
                String googleEmail = acc.getEmail();
                User existingUser = dao.checkEmailExist(googleEmail);
                HttpSession session = request.getSession();

                session.removeAttribute("failedAttempts");
                session.removeAttribute("lockTime");

                if (existingUser != null) {
                    if (!"Active".equals(existingUser.getStatus())) {
                        dao.activateAccount(existingUser.getVerificationToken());
                        existingUser = dao.checkEmailExist(googleEmail);
                    }

                    session.setAttribute("acc", existingUser);

                    String redirectUrl = (String) session.getAttribute("redirect_url");
                    if (redirectUrl != null) {
                        session.removeAttribute("redirect_url");
                        response.sendRedirect(redirectUrl);
                    } else {
                        response.sendRedirect("home");
                    }

                } else {
                    String autoUsername = googleEmail.substring(0, googleEmail.indexOf("@"));
                    String randomPassword = java.util.UUID.randomUUID().toString();
                    String googleToken = "GOOGLE_" + java.util.UUID.randomUUID().toString();

                    dao.signup(autoUsername, randomPassword, acc.getName(), googleEmail, "", googleToken);
                    dao.activateAccount(googleToken);

                    User newUser = dao.checkEmailExist(googleEmail);
                    session.setAttribute("acc", newUser);
                    response.sendRedirect("home");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            HttpSession session = request.getSession();
            session.setAttribute("mess", "Đã xảy ra lỗi đăng nhập Google!");
            response.sendRedirect("login");
        }
    }
}