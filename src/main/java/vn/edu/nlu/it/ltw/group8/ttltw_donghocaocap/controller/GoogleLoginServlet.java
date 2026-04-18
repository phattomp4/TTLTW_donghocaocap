package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.http.client.ClientProtocolException;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.constant.Iconstant;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.GoogleAccount;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;

import java.io.IOException;
@WebServlet("/google-login")
public class GoogleLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String code = request.getParameter("code");

        if (code != null && !code.isEmpty()) {
            try {
                String accessToken = getToken(code);

                GoogleAccount googleAcc = getUserInfo(accessToken);

                String email = googleAcc.getEmail();
                String name = googleAcc.getName();
                String avatar = googleAcc.getPicture();

                UserDAO userDAO = new UserDAO();
                User user = userDAO.getUserByEmail(email);

                if (user == null) {
                    user = new User();
                    user.setEmail(email);
                    user.setFullName(name);
                    user.setAvatar(avatar);
                    userDAO.insertGoogleUser(user);
                    user = userDAO.getUserByEmail(email);
                }

                HttpSession session = request.getSession();
                session.setAttribute("acc", user);

                response.sendRedirect(request.getContextPath() + "/home");

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("login.jsp?error=google_login_failed");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String name = request.getParameter("name");
        String avatar = request.getParameter("avatar");
        if (avatar == null || avatar.isEmpty()) {
            avatar = request.getParameter("picture");
        }
        UserDAO userDAO = new UserDAO();

        User user = userDAO.getUserByEmail(email);

        if (user == null) {
            user = new User();
            user.setEmail(email);
            user.setFullName(name);
            user.setAvatar(avatar);
            userDAO.insertGoogleUser(user);


            user = userDAO.getUserByEmail(email);
        }


        HttpSession session = request.getSession();
        session.setAttribute("acc", user);

        response.getWriter().write("success");
    }
    public static String getToken(String code) throws IOException {

        String response = Request.Post(Iconstant.GOOGLE_LINK_GET_TOKEN)
                .bodyForm(
                        Form.form()
                                .add("client_id", Iconstant.GOOGLE_CLIENT_ID)
                                .add("client_secret", Iconstant.GOOGLE_CLIENT_SECRET)
                                .add("redirect_uri", Iconstant.GOOGLE_REDIRECT_URL)
                                .add("code", code)
                                .add("grant_type", Iconstant.GOOGLE_GRANT_TYPE)
                                .build()
                )
                .execute()
                .returnContent()
                .asString();
        System.out.println("GOOGLE TOKEN RESPONSE >>> " + response);
        JsonObject jobj = new Gson().fromJson(response, JsonObject.class);
        return jobj.get("access_token").getAsString();
    }

    public static GoogleAccount getUserInfo(final String accessToken) throws IOException {
        String link = Iconstant.GOOGLE_LINK_GET_USER_INFO + accessToken;

        java.net.URL url = new java.net.URL(link);
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "UTF-8"));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new Gson().fromJson(response.toString(), GoogleAccount.class);
    }
}