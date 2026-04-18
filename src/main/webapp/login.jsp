<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập - Luxury Watch</title>
    <link rel="stylesheet" href="assets/css/login.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .input-group { position: relative; }
        .toggle-password {
            position: absolute;
            right: 15px;
            top: 50%;
            transform: translateY(5px);
            cursor: pointer;
            color: #000;
            z-index: 10;
        }
        #mess-display {
            display: none;
            text-align: center;
            margin-bottom: 15px;
            font-weight: bold;
            font-size: 14px;
            padding: 10px;
            border-radius: 5px;
        }
        .error-realtime { color: #ff4757; font-size: 12px; margin-top: 5px; display: block; }
    </style>
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

        <div id="mess-display"></div>

        <c:if test="${not empty sessionScope.mess_success}">
            <p style="color: #2ecc71; text-align: center; font-weight: bold; margin-bottom: 15px;">
                    ${sessionScope.mess_success}
            </p>
            <c:remove var="mess_success" scope="session" />
        </c:if>

        <form id="loginForm">
            <div class="input-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" placeholder="Nhập tên đăng nhập"
                       value="${cookie.userC.value}"
                       onblur="checkAccountExistence(this.value)" required>
                <small id="user-err" class="error-realtime"></small>
            </div>

            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" placeholder="Nhập mật khẩu"
                       value="${cookie.passC.value}" required>
                <i class="fa-solid fa-eye toggle-password" id="toggleBtn"></i>
            </div>

            <div class="options-group">
                <label>
                    <input type="checkbox" name="remember" value="ON" ${not empty cookie.userC ? 'checked': ''}> Ghi nhớ đăng nhập
                </label>
                <a href="user/userpass.jsp" class="forgot-password">Quên mật khẩu?</a>
            </div>

            <button type="submit" id="submitBtn" class="login-button" style="width: 100%; padding: 10px; background: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43); color: #fff; border: none; cursor: pointer;">Đăng Nhập</button>

            <div class="social-separator">
                <span>Hoặc đăng nhập bằng</span>
            </div>

            <div class="social-login">
                <c:url var="googleRedirect" value="/google-login" />
                <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080${googleRedirect}&response_type=code&client_id=1019706270086-r01p0g06ev31mmkl51lg2joopf8dfthr.apps.googleusercontent.com&approval_prompt=force" class="btn-social btn-google">
                    <i class="fa-brands fa-google"></i> Google
                </a>
            </div>
        </form>

        <p class="register-link">
            Chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a>
        </p>
    </div>
</div>

<script>
    const toggleBtn = document.querySelector('#toggleBtn');
    const password = document.querySelector('#password');
    toggleBtn.addEventListener('click', function () {
        const type = password.getAttribute('type') === 'password' ? 'text' : 'password';
        password.setAttribute('type', type);
        this.classList.toggle('fa-eye');
        this.classList.toggle('fa-eye-slash');
    });

    function checkAccountExistence(value) {
        const errTag = document.getElementById('user-err');
        if(!value.trim()) return;

        fetch('${pageContext.request.contextPath}/login?checkAccount=' + encodeURIComponent(value))
            .then(res => res.text())
            .then(data => {
                errTag.innerText = data;
            });
    }

    // fix: dung ajax cho dang nhap
    document.getElementById('loginForm').addEventListener('submit', function(e) {
        e.preventDefault();

        const btn = document.getElementById('submitBtn');
        const msgArea = document.getElementById('mess-display');
        const formData = new URLSearchParams(new FormData(this));

        btn.disabled = true;
        btn.innerText = "Đang kiểm tra...";
        msgArea.style.display = 'none';

        fetch('${pageContext.request.contextPath}/login', {
            method: 'POST',
            body: formData,
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        })
            .then(res => res.text())
            .then(data => {
                const [status, content] = data.split('|');

                if (status === 'SUCCESS') {
                    window.location.href = content;
                } else {
                    msgArea.innerText = content;
                    msgArea.style.display = 'block';
                    msgArea.style.backgroundColor = (status === 'INFO') ? '#e1f5fe' : '#ffebee';
                    msgArea.style.color = (status === 'INFO') ? '#01579b' : '#c62828';

                    btn.disabled = false;
                    btn.innerText = "Đăng Nhập";
                }
            })
            .catch(() => {
                alert("Lỗi kết nối máy chủ!");
                btn.disabled = false;
                btn.innerText = "Đăng Nhập";
            });
    });
</script>
</body>
</html>