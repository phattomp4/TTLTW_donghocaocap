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
        <a href="${pageContext.request.contextPath}/home" class="back-home-button">
            <i class="fa-solid fa-arrow-left"></i> Quay về Trang chủ
        </a>
    </div>

    <div class="login-box">
        <h2 class="login-title">Đăng Nhập Tài Khoản</h2>

        <c:if test="${not empty sessionScope.mess_success}">
            <p style="color: #2ecc71; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${sessionScope.mess_success}
            </p>
            <c:remove var="mess_success" scope="session" />
        </c:if>

        <c:if test="${not empty requestScope.mess}">
            <p style="color: #e74c3c; text-align: center; font-style: italic; margin-bottom: 15px;">
                    ${requestScope.mess}
            </p>
        </c:if>

        <form action="login" method="POST">

            <div class="input-group">
                <label for="username">Tên đăng nhập / Email / SĐT</label>
                <input type="text" id="username" name="username" value="${requestScope.username}" placeholder="Nhập tên đăng nhập" required>
            </div>

            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
            </div>

            <div class="options-group">
                <label style="cursor: pointer;">
                    <input type="checkbox" name="remember"> Ghi nhớ đăng nhập
                </label>
                <a href="user/userpass.jsp" class="forgot-password">Quên mật khẩu?</a>
            </div>

            <button type="submit" class="login-button" style="width: 100%; padding: 10px; background: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43); color: #fff; border: none; cursor: pointer;">Đăng Nhập</button>

        </form>

        <p class="register-link">
            Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
        </p>
    </div>
</div>
</body>
</html>