<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
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
        <h2 class="login-title">Đăng Ký Tài Khoản Mới</h2>

        <c:if test="${not empty requestScope.mess}">
            <p style="color: red; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${requestScope.mess}
            </p>
        </c:if>

        <form action="register" method="POST" id="registerForm">

            <div class="input-group">
                <label for="user">Tên đăng nhập</label>
                <input type="text" id="user" name="user" placeholder="Nhập tên đăng nhập (viết liền)" required>
            </div>

            <div class="input-group">
                <label for="fullname">Họ và Tên</label>
                <input type="text" id="fullname" name="fullname" placeholder="Nhập họ và tên đầy đủ" required>
            </div>

            <div class="input-group">
                <label for="email">Địa chỉ Email</label>
                <input type="email" id="email" name="email" placeholder="Nhập email của bạn" required>
            </div>

            <div class="input-group">
                <label for="pass">Mật khẩu</label>
                <input type="password" id="pass" name="pass" placeholder="Tạo mật khẩu (ít nhất 6 ký tự)" required>
            </div>

            <div class="input-group">
                <label for="re_pass">Xác nhận Mật khẩu</label>
                <input type="password" id="re_pass" name="re_pass" placeholder="Nhập lại mật khẩu" required>
            </div>

            <button type="submit" class="login-button">Đăng Ký</button>
        </form>

        <p class="register-link">
            Đã có tài khoản? <a href="login.jsp">Đăng nhập ngay</a>
        </p>
    </div>
</div>


</body>
</html>