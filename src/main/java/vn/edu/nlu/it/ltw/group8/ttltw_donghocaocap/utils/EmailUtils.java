package com.vvp.utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtils {

    public static void sendEmail(String toEmail, String subject, String body) {
        // 1. Cấu hình SMTP Server (Dùng Gmail)
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
        props.put("mail.smtp.port", "587"); // TLS Port
        props.put("mail.smtp.auth", "true"); // Enable Auth
        props.put("mail.smtp.starttls.enable", "true"); // Enable TLS

        // 2. Tài khoản Gmail của ADMIN (Người gửi)
        final String fromEmail = "email_cua_ban@gmail.com";
        final String password = "mat_khau_ung_dung_cua_ban"; // KHÔNG PHẢI PASS GMAIL, LÀ APP PASSWORD

        // 3. Tạo Session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        // 4. Gửi Email
        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            msg.setSubject(subject);
            msg.setText(body); // Hoặc dùng setContent(body, "text/html") nếu muốn gửi HTML

            Transport.send(msg);
            System.out.println("Gửi mail thành công!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}