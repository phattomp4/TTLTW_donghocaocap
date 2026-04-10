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
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            processRequest(request, response);
        } else {
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.contains("login") && !referer.contains("register")) {
                request.getSession().setAttribute("redirect_url", referer);
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

        if (user == null) {
            request.setAttribute("mess", "Tài khoản không tồn tại!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (user.getLockExpiry() != null && user.getLockExpiry().after(new Date())) {
            request.setAttribute("mess", "Tài khoản bị khóa tạm thời. Vui lòng thử lại sau!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (BCrypt.checkpw(p, user.getPassword())) {

            if (!"Active".equals(user.getStatus())) {
                request.setAttribute("mess", "Tài khoản chưa kích hoạt! Vui lòng kiểm tra email.");
                request.setAttribute("username", u);
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            dao.resetFailedAttempts(user.getId());

            HttpSession session = request.getSession();
            session.setAttribute("acc", user);
            session.setMaxInactiveInterval(60 * 60);

            if ("ON".equals(r)) {
                String token = java.util.UUID.randomUUID().toString();
                dao.updateRememberToken(user.getUsername(), token);
                Cookie tokenCookie = new Cookie("remember_token", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 7); // 7 ngày
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
            int currentFailed = user.getFailedAttempts();
            dao.handleFailedLogin(user.getId(), currentFailed);

            int attemptsLeft = 5 - (currentFailed + 1);
            String errorMsg = "Sai mật khẩu!";
            if (attemptsLeft > 0) {
                errorMsg += " Bạn còn " + attemptsLeft + " lần thử.";
            } else {
                errorMsg = "Tài khoản đã bị khóa 15 phút do nhập sai quá 5 lần.";
            }

            request.setAttribute("mess", errorMsg);
            request.setAttribute("username", u);
            request.getRequestDispatcher("login.jsp").forward(request, response);
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

                if (existingUser != null) {
                    if (!"Active".equals(existingUser.getStatus())) {
                        dao.activateAccount(existingUser.getVerificationToken());
                        existingUser = dao.checkEmailExist(googleEmail);
                    }

                    HttpSession session = request.getSession();
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
                    HttpSession session = request.getSession();
                    session.setAttribute("acc", newUser);
                    response.sendRedirect("home");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mess", "Đã xảy ra lỗi đăng nhập Google!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}