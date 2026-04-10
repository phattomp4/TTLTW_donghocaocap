package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailUtil {
    private static final String FROM_EMAIL = "sinhvo2801qt@gmail.com";
    private static final String APP_PASSWORD = "juvo vfzb psgn rdhz";

    public static boolean sendOTP(String toEmail, String otp) {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject("Mã xác thực đặt lại mật khẩu");
            message.setContent(
                    "<h3> VVP - Xác thực tài khoản</h3>" +
                            "<h2>" + otp + "</h2>" +
                            "<p>Mã có hiệu lực trong 5 phút</p>",
                    "text/html; charset=UTF-8"
            );

            Transport.send(message);
            System.out.println("OTP sent to " + toEmail);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendMail(String to, String subject, String content) {
        boolean isSent = false;

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL, "VVP Store - Hệ thống Đồng Hồ Cao Cấp"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));

            message.setSubject(subject);

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);
            isSent = true;
            System.out.println("Gửi email thành công tới: " + to);

        } catch (Exception e) {
            System.out.println("Lỗi khi gửi email: " + e.getMessage());
            e.printStackTrace();
        }

        return isSent;
    }
    public static boolean sendActivationEmail(String toEmail, String token) {
        String activationLink = "http://localhost:8080/TTLTW_donghocaocap_war_exploded/verify?token=" + token;

        String content = "<div style='font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto; border: 1px solid #ddd; padding: 20px;'>" +
                "<h2 style='color: #1b6e76; text-align: center;'>Chào mừng bạn đến với Luxury Watch!</h2>" +
                "<p>Cảm ơn bạn đã đăng ký thành viên. Để bắt đầu trải nghiệm dịch vụ, vui lòng kích hoạt tài khoản của bạn bằng cách nhấn vào nút bên dưới:</p>" +
                "<div style='text-align: center; margin: 30px 0;'>" +
                "<a href='" + activationLink + "' style='background-color: #1b6e76; color: white; padding: 12px 25px; text-decoration: none; border-radius: 5px; font-weight: bold;'>KÍCH HOẠT TÀI KHOẢN</a>" +
                "</div>" +
                "<p style='color: #666; font-size: 13px;'>Link này có hiệu lực trong vòng 24 giờ. Nếu bạn không thực hiện đăng ký, vui lòng bỏ qua email này.</p>" +
                "<hr style='border: 0; border-top: 1px solid #eee;'>" +
                "<p style='text-align: center; font-size: 12px; color: #999;'>© 2026 Luxury Watch Store - Hệ thống Đồng Hồ Cao Cấp</p>" +
                "</div>";

        return sendMail(toEmail, "Kích hoạt tài khoản thành viên Luxury Watch", content);
    }
}
