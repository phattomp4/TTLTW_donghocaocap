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
            out.write("ERROR|Tài khoản hoặc mật khẩu không chính xác!");
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

            FavoriteDAO favDao = new FavoriteDAO();
            session.setAttribute("favCount", favDao.countFavorites(user.getId()));

            if ("ON".equals(r)) {
                String token = java.util.UUID.randomUUID().toString();
                dao.updateRememberToken(user.getUsername(), token);
                Cookie tokenCookie = new Cookie("remember_token", token);
                tokenCookie.setMaxAge(60 * 60 * 24 * 7);
                tokenCookie.setPath("/");
                response.addCookie(tokenCookie);
            }

            String redirectUrl = (String) session.getAttribute("redirect_url");
            String target = "";

            if (redirectUrl != null && !redirectUrl.trim().isEmpty()
                    && !redirectUrl.contains("login")
                    && !redirectUrl.contains("register")
                    && redirectUrl.startsWith("http")) {
                target = redirectUrl;
                session.removeAttribute("redirect_url");
            } else {
                if (user.getRole() != null && "Admin".equalsIgnoreCase(user.getRole())) {
                    target = request.getContextPath() + "/admin/dashboard";
                } else {
                    target = request.getContextPath() + "/home";
                }
            }
            out.write("SUCCESS|" + target);
        } else {
            Integer failedCount = (Integer) session.getAttribute("failedAttempts");
            if (failedCount == null) failedCount = 0;
            failedCount++;
            session.setAttribute("failedAttempts", failedCount);

            String errorMsg;
            if (failedCount >= 5) {
                session.setAttribute("lockTime", System.currentTimeMillis() + (15 * 60 * 1000));
                errorMsg = "Sai quá 5 lần. Tài khoản bị khóa 15 phút.";
                out.write("ERROR|" + errorMsg);
            } else {
                errorMsg = "Mật khẩu không chính xác! Bạn còn " + (5 - failedCount) + " lần thử.";
                out.write("ERROR|" + errorMsg);
            }
            session.setAttribute("mess", errorMsg);
        }
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        try {
            String accessToken = GoogleLoginServlet.getToken(code);
            GoogleAccount googleAcc = GoogleLoginServlet.getUserInfo(accessToken);
            if (googleAcc != null) {
                UserDAO dao = new UserDAO();
                User existingUser = dao.checkEmailExist(googleAcc.getEmail());
                HttpSession session = request.getSession();
                FavoriteDAO favDao = new FavoriteDAO();

                if (existingUser != null) {
                    if (!"Active".equals(existingUser.getStatus())) {
                        dao.activateAccount(existingUser.getVerificationToken());
                        existingUser = dao.checkEmailExist(googleAcc.getEmail());
                    }
                    session.setAttribute("acc", existingUser);
                    session.setAttribute("favCount", favDao.countFavorites(existingUser.getId()));

                    String redirectUrl = (String) session.getAttribute("redirect_url");
                    if (redirectUrl != null && !redirectUrl.contains("login") && !redirectUrl.contains("register") && redirectUrl.startsWith("http")) {
                        session.removeAttribute("redirect_url");
                        response.sendRedirect(redirectUrl);
                    } else {
                        response.sendRedirect(request.getContextPath() + "/home");
                    }
                } else {
                    User googleUser = new User();
                    googleUser.setEmail(googleAcc.getEmail());
                    googleUser.setFullName(googleAcc.getName());
                    googleUser.setAvatar(googleAcc.getPicture());

                    dao.insertGoogleUser(googleUser);

                    User newUser = dao.checkEmailExist(googleAcc.getEmail());
                    if (newUser != null) {
                        session.setAttribute("acc", newUser);
                        session.setAttribute("favCount", favDao.countFavorites(newUser.getId()));
                    }

                    response.sendRedirect(request.getContextPath() + "/home");
                }
            }
        } catch (Exception e) {
            System.out.println(">>> LỖI XỬ LÝ ĐĂNG NHẬP GOOGLE TẠI SERVLET:");
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/login");
        }
    }
}