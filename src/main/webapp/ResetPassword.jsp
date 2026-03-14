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
    <title>Đặt lại mật khẩu</title>
</head>
<body>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đặt lại mật khẩu</title>
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
        <h2 class="login-title">Đặt Lại Mật Khẩu</h2>

        <c:if test="${not empty requestScope.mess}">
            <p style="color: red; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${requestScope.mess}
            </p>
        </c:if>

        <form action="resetPassword" method="POST">
            <input type="hidden" name="email" value="${requestScope.email}">
            <input type="hidden" name="token" value="${requestScope.token}">

            <div class="input-group">
                <label for="new_password">Mật khẩu mới</label>
                <input type="password" id="new_password" name="new_password" placeholder="Nhập mật khẩu mới" required>
            </div>

            <div class="input-group">
                <label for="confirm_password">Xác nhận mật khẩu mới</label>
                <input type="password" id="confirm_password" name="confirm_password" placeholder="Nhập lại mật khẩu mới" required>
            </div>

            <div class="login-button">
                <button type="submit" style="width: 100%; padding: 10px; background-color: #000; color: #fff; border: none; cursor: pointer;">Cập Nhật Mật Khẩu</button>
            </div>
        </form>

        <p class="register-link">
            Quay lại <a href="login.jsp">Đăng nhập</a>
        </p>
    </div>
</div>
</body>
</html>
</body>
</html>
