package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();

        Cookie userCookie = new Cookie("userC", "");
        Cookie passCookie = new Cookie("passC", "");
        userCookie.setMaxAge(0);
        passCookie.setMaxAge(0);
        response.addCookie(userCookie);
        response.addCookie(passCookie);
        response.sendRedirect("home");
    }


}