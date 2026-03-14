<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 13/03/2026
  Time: 11:20 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Quên mật khẩu</title>
</head>
<body>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quên mật khẩu</title>
    <link rel="stylesheet" href="assets/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<div class="login-container">
    <div class="back-home">
        <a href="index.jsp" class="back-home-button">
            <i class="fa-solid fa-arrow-left"></i> Quay về Trang chủ
        </a>
    </div>

    <div class="login-box">
        <h2 class="login-title">Quên Mật Khẩu</h2>

        <c:if test="${not empty requestScope.mess}">
            <p style="color: red; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${requestScope.mess}
            </p>
        </c:if>

        <c:if test="${not empty requestScope.success}">
            <p style="color: green; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${requestScope.success}
            </p>
        </c:if>

        <p style="text-align: center; margin-bottom: 20px; font-size: 14px; color: #555;">
            Vui lòng nhập địa chỉ email bạn đã sử dụng để đăng ký. Chúng tôi sẽ gửi hướng dẫn khôi phục mật khẩu cho bạn.
        </p>

        <form action="forgotPassword" method="POST">
            <div class="input-group">
                <label for="email">Địa chỉ Email</label>
                <input type="email" id="email" name="email" placeholder="Nhập email của bạn" required>
            </div>

            <div class="login-button">
                <button type="submit" style="width: 100%; padding: 10px; background-color: #000; color: #fff; border: none; cursor: pointer;">Gửi Yêu Cầu</button>
            </div>
        </form>

        <p class="register-link">
            Đã nhớ ra mật khẩu? <a href="login.jsp">Đăng nhập</a>
        </p>
    </div>
</div>
</body>
</html>
</body>
</html>
