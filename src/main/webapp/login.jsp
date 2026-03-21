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


            <button type="submit" class="login-button">Đăng Nhập</button>

            <div class="social-separator">
                <span>Hoặc đăng nhập bằng</span>
            </div>

            <div class="social-login">
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/WebBanDongHo_war_exploded/login&response_type=code&client_id=1088549234572-pj2jekip8g464mt2k5ole8k9jf6p1ivg.apps.googleusercontent.com&approval_prompt=force" class="btn-social btn-google">
                    <i class="fa-brands fa-google"></i> Google
                </a>
            </div>

        </form>

        <p class="register-link">
            Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
        </p>
    </div>
</div>
</body>
</html>