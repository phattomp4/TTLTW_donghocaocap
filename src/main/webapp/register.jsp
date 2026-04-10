<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản - Luxury Watch</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="assets/css/signup.css">
</head>
<body>

<div class="login-container">
    <div class="side-box">
        <h2>Luxury Watch</h2>
        <p>Để duy trì kết nối với chúng tôi vui lòng đăng nhập bằng thông tin cá nhân của bạn</p>
        <a href="login.jsp" class="login-link-btn">ĐĂNG NHẬP NGAY</a>
    </div>

    <div class="login-box">
        <h2 class="login-title">Đăng ký tài khoản</h2>

        <c:if test="${not empty requestScope.mess}">
            <p style="color: #ff6b6b; font-size: 14px; text-align: center; margin-bottom: 10px;">
                <i class="fa-solid fa-triangle-exclamation"></i> ${requestScope.mess}
            </p>
        </c:if>

        <c:if test="${not empty requestScope.mess_info}">
            <div class="info-box">
                <i class="fa-solid fa-circle-info"></i> ${requestScope.mess_info}
            </div>
        </c:if>

        <form action="register" method="POST" id="registerForm">
            <div class="form-grid">
                <div class="input-group">
                    <label for="user">Tên đăng nhập</label>
                    <input type="text" id="user" name="user" value="${oldUser}"
                           class="${not empty errors.user ? 'is-invalid' : ''}"
                           oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, ''); validateForm();"
                           onblur="checkDuplicate('user', this.value)" required>
                    <small id="msg-user" class="error-msg">${errors.user}</small>
                </div>

                <div class="input-group">
                    <label for="fullname">Họ và Tên</label>
                    <input type="text" id="fullname" name="fullname" value="${oldFullName}"
                           oninput="validateForm()" required>
                    <small class="error-msg">${errors.fullname}</small>
                </div>

                <div class="input-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" id="email" name="email" value="${oldEmail}"
                           class="${not empty errors.email ? 'is-invalid' : ''}"
                           onblur="checkDuplicate('email', this.value)"
                           oninput="validateForm()" required>
                    <small id="msg-email" class="error-msg">${errors.email}</small>
                </div>

                <div class="input-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" id="phone" name="phone" value="${oldPhone}"
                           class="${not empty errors.phone ? 'is-invalid' : ''}"
                           onblur="checkDuplicate('phone', this.value)"
                           oninput="validateForm()" required>
                    <small id="msg-phone" class="error-msg">${errors.phone}</small>
                </div>

                <div class="input-group">
                    <label for="pass">Mật khẩu</label>
                    <input type="password" id="pass" name="pass"
                           class="${not empty errors.pass ? 'is-invalid' : ''}"
                           onkeyup="validatePass(this.value)" required>
                    <i class="fa fa-eye toggle-password" onclick="toggleView('pass', this)"></i>
                    <small id="msg-pass" class="error-msg">${errors.pass}</small>
                </div>

                <div class="input-group">
                    <label for="re_pass">Xác nhận mật khẩu</label>
                    <input type="password" id="re_pass" name="re_pass"
                           class="${not empty errors.re_pass ? 'is-invalid' : ''}"
                           onkeyup="checkMatch()" required>
                    <i class="fa fa-eye toggle-password" onclick="toggleView('re_pass', this)"></i>
                    <small id="msg-repass" class="error-msg">${errors.re_pass}</small>
                </div>

                <button type="submit" id="submitBtn" class="login-button">Đăng Ký</button>
            </div>
        </form>
    </div>
</div>

<script>
    const VALIDATOR = {
        user: /^[a-zA-Z0-9]{5,20}$/,
        email: /^[A-Za-z0-9._%+-]+@gmail\.com$/,
        phone: /^(0|84)(3|5|7|8|9)([0-9]{8})$/,
        pass: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
    };

    let validationState = { user: false, fullname: false, email: false, phone: false, pass: false, repass: false };
    let ajaxPending = 0;

    function validateForm() {
        validationState.fullname = document.getElementById('fullname').value.trim().length > 0;
        const btn = document.getElementById('submitBtn');
        const allValid = Object.values(validationState).every(v => v === true);
        btn.disabled = (ajaxPending > 0 || !allValid);
    }

    function toggleView(id, btn) {
        const input = document.getElementById(id);
        input.type = (input.type === "password") ? "text" : "password";
        btn.classList.toggle("fa-eye");
        btn.classList.toggle("fa-eye-slash");
    }

    function checkDuplicate(type, value) {
        const msgTag = document.getElementById('msg-' + type);
        const inputTag = document.getElementById(type);

        if (!value.trim() || !VALIDATOR[type].test(value)) {
            showUI(msgTag, inputTag, "✕ Định dạng không hợp lệ", false, type);
            return;
        }

        ajaxPending++;
        validateForm();

        fetch('checkDuplicate?type=' + type + '&value=' + encodeURIComponent(value))
            .then(res => res.text())
            .then(data => {
                if (data.trim() !== "") {
                    showUI(msgTag, inputTag, "✕ " + data, false, type);
                } else {
                    showUI(msgTag, inputTag, "✓ Hợp lệ", true, type);
                }
            })
            .catch(() => showUI(msgTag, inputTag, "✕ Lỗi kết nối", false, type))
            .finally(() => {
                ajaxPending--;
                validateForm();
            });
    }

    function showUI(msgTag, inputTag, text, isSuccess, field) {
        msgTag.innerText = text;
        msgTag.style.color = isSuccess ? "#2ecc71" : "#ff4757";
        inputTag.style.border = isSuccess ? "2px solid #2ecc71" : "2px solid #ff4757";
        if(field) {
            validationState[field] = isSuccess;
            validateForm();
        }
    }

    function validatePass(pass) {
        const isOk = VALIDATOR.pass.test(pass);
        showUI(document.getElementById('msg-pass'), document.getElementById('pass'), isOk ? "✓ Mật khẩu mạnh" : "✕ Quá yếu", isOk, 'pass');
        checkMatch();
    }

    function checkMatch() {
        const p1 = document.getElementById('pass').value;
        const p2 = document.getElementById('re_pass').value;
        const isMatch = (p1 === p2 && p2.length > 0);
        showUI(document.getElementById('msg-repass'), document.getElementById('re_pass'), isMatch ? "✓ Khớp" : "✕ Không khớp", isMatch, 'repass');
    }
</script>
</body>
</html>