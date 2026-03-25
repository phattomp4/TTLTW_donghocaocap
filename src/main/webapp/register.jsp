<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký tài khoản</title>
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
            <p style="color: #ff6b6b; font-size: 14px; text-align: center; margin-bottom: 10px;">${requestScope.mess}</p>
        </c:if>

        <form action="register" method="POST" id="registerForm">
            <div class="form-grid">
                <div class="input-group">
                    <label for="user">Tên đăng nhập</label>
                    <input type="text" id="user" name="user" value="${oldUser}" placeholder="Viết liền không dấu"
                           oninput="this.value = this.value.replace(/[^a-zA-Z0-9]/g, '')"
                           onblur="checkDuplicate('user', this.value)" required>
                    <small id="msg-user" class="ajax-msg"></small>
                </div>

                <div class="input-group">
                    <label for="fullname">Họ và Tên</label>
                    <input type="text" id="fullname" name="fullname" value="${oldFullName}" placeholder="Họ tên của bạn" required>
                    <small class="ajax-msg"></small>
                </div>

                <div class="input-group">
                    <label for="email">Địa chỉ Email</label>
                    <input type="email" id="email" name="email" value="${oldEmail}" placeholder="email@example.com"
                           onblur="checkDuplicate('email', this.value)" required>
                    <small id="msg-email" class="ajax-msg"></small>
                </div>

                <div class="input-group">
                    <label for="phone">Số điện thoại</label>
                    <input type="text" id="phone" name="phone" value="${oldPhone}" placeholder="Số điện thoại"
                           onblur="checkDuplicate('phone', this.value)" required>
                    <small id="msg-phone" class="ajax-msg"></small>
                </div>

                <div class="input-group">
                    <label for="pass">Mật khẩu</label>
                    <input type="password" id="pass" name="pass" placeholder="Tối thiểu 8 ký tự"
                           onkeyup="validatePass(this.value)" required>
                    <small id="msg-pass" class="ajax-msg"></small>
                </div>

                <div class="input-group">
                    <label for="re_pass">Xác nhận mật khẩu</label>
                    <input type="password" id="re_pass" name="re_pass" placeholder="Nhập lại mật khẩu"
                           onkeyup="checkMatch()" required>
                    <small id="msg-repass" class="ajax-msg"></small>
                </div>

                <button type="submit" class="login-button">Đăng Ký</button>
            </div>
        </form>
    </div>
</div>
<script>
    const VALIDATOR = {
        email: /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,6}$/,
        phone: /^(0|84)(3|5|7|8|9)([0-9]{8})$/,
        pass: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
    };

    function checkDuplicate(type, value) {
        const msgTag = document.getElementById('msg-' + type);
        const inputTag = document.getElementById(type);

        if (!value.trim()) {
            clearStatus(msgTag, inputTag);
            return;
        }

        if (type === 'email' && !VALIDATOR.email.test(value)) {
            showUI(msgTag, inputTag, "✕ Email phải có đuôi hợp lệ (vd: .com, .vn)", false);
            return;
        }
        if (type === 'phone' && !VALIDATOR.phone.test(value)) {
            showUI(msgTag, inputTag, "✕ Số điện thoại không hợp lệ", false);
            return;
        }

        fetch('checkDuplicate?type=' + type + '&value=' + encodeURIComponent(value))
            .then(res => res.text())
            .then(data => {
                if (data.trim() !== "") {
                    showUI(msgTag, inputTag, "✕ " + data, false);
                } else {
                    showUI(msgTag, inputTag, "✓ Hợp lệ", true);
                }
            })
            .catch(() => showUI(msgTag, inputTag, "✕ Lỗi kết nối", false));
    }

    function showUI(msgTag, inputTag, text, isSuccess) {
        msgTag.innerText = text;
        msgTag.className = isSuccess ? "ajax-msg success" : "ajax-msg error";
        inputTag.style.border = isSuccess ? "2px solid #2ecc71" : "2px solid #ff4757";
    }

    function clearStatus(msgTag, inputTag) {
        msgTag.innerText = "";
        inputTag.style.border = "1px solid #ddd";
    }

    function validatePass(pass) {
        const msg = document.getElementById('msg-pass');
        const input = document.getElementById('pass');
        if (!pass) { clearStatus(msg, input); return; }
        if (!VALIDATOR.pass.test(pass)) {
            showUI(msg, input, "✕ Mật khẩu quá yếu", false);
        } else {
            showUI(msg, input, "✓ Mật khẩu mạnh", true);
        }
        checkMatch();
    }

    function checkMatch() {
        const p1 = document.getElementById('pass').value;
        const p2 = document.getElementById('re_pass').value;
        const msg = document.getElementById('msg-repass');
        const input = document.getElementById('re_pass');
        if (!p2) return;
        if (p1 !== p2) showUI(msg, input, "✕ Không khớp", false);
        else showUI(msg, input, "✓ Khớp", true);
    }
</script>
</body>
</html>