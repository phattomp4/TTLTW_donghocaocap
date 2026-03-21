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

    function checkDuplicate(type, value) {
        const msgTag = document.getElementById('msg-' + type);
        if (!value.trim()) { msgTag.innerText = ""; return; }
        fetch('checkDuplicate?type=' + type + '&value=' + value)
            .then(res => res.text())
            .then(data => {
                if (data.trim() !== "") {
                    msgTag.innerText = "✕ " + data;
                    msgTag.className = "ajax-msg error";
                } else {
                    msgTag.innerText = "✓ Hợp lệ";
                    msgTag.className = "ajax-msg success";
                }
            });
    }

    function validatePass(pass) {
        const msg = document.getElementById('msg-pass');
        const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        if (pass.length > 0 && !regex.test(pass)) {
            msg.innerText = "Cần Hoa, Thường, Số, Ký tự đặc biệt";
            msg.className = "ajax-msg error";
        } else if (pass.length >= 8) {
            msg.innerText = "✓ Mật khẩu mạnh";
            msg.className = "ajax-msg success";
        }
        checkMatch();
    }

    function checkMatch() {
        const p1 = document.getElementById('pass').value;
        const p2 = document.getElementById('re_pass').value;
        const msg = document.getElementById('msg-repass');
        if (p2 !== "" && p1 !== p2) {
            msg.innerText = "Không khớp!";
            msg.className = "ajax-msg error";
        } else if (p2 !== "") {
            msg.innerText = "✓ Khớp";
            msg.className = "ajax-msg success";
        }
    }
</script>
</body>
</html>