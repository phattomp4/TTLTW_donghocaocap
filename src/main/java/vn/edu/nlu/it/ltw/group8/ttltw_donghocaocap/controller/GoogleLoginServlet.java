package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import org.apache.http.client.ClientProtocolException;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.constant.Iconstant;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Form;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.GoogleAccount;

import java.io.IOException;

public class GoogleLoginServlet {

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
        System.out.println("GOOGLE TOKEN RESPONSE >>> " + response); // ⚠⚠ IN RA LOG

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