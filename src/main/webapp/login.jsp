<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
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
        <h2 class="login-title">Đăng Nhập Tài Khoản</h2>

        <p style="color: red; text-align: center; font-style: italic;">${requestScope.mess}</p>

        <form action="login" method="POST">

            <div class="input-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" value="${requestScope.username}" placeholder="Nhập tên đăng nhập" required>
            </div>

            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
            </div>

            <div class="options-group">
                <label>
                    <input type="checkbox" name="remember"> Ghi nhớ đăng nhập
                </label>
                <a href="user/userpass.jsp" class="forgot-password">Quên mật khẩu?</a>
            </div>

            <div class="login-button">
                <button type="submit" style="width: 100%; padding: 10px; background-color: #000; color: #fff; border: none; cursor: pointer;">Đăng Nhập</button>
            </div>
        </form>

        <p class="register-link">
            Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
        </p>
    </div>
</div>
</body>
</html>