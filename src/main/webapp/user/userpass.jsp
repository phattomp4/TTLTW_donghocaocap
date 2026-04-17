<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Khôi phục mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/forgot_password.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">

    <style>
        .auth-card {
            max-width: 500px;
            margin: 50px auto;
            background-color: rgba(255, 255, 255, 0.3);
            padding: 40px;
            border-radius: 12px;
            box-shadow: 0 5px 20px rgba(0, 0, 0, 0.08);
            text-align: center;
            backdrop-filter: blur(8px);
        }
        .auth-card h2 {
            color: #0e3e43;
            margin-bottom: 15px;
            font-weight: 700;
            text-transform: uppercase;
        }
        .error-msg {
            color: #ff4d4d;
            background: #fff1f0;
            border: 1px solid #ffa39e;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .success-msg {
            color: #52c41a;
            background: #f6ffed;
            border: 1px solid #b7eb8f;
            padding: 10px;
            border-radius: 4px;
            margin-bottom: 20px;
            font-size: 14px;
        }
        .otp-input-large {
            width: 100%;
            text-align: center;
            font-size: 28px;
            font-weight: bold;
            letter-spacing: 15px;
            padding: 10px;
            border: 2px solid #dddddd;
            border-radius: 8px;
            margin: 20px 0;
            outline: none;
        }
        .otp-input-large:focus {
            border-color: #0e3e43;
        }
        #timer-box {
            margin-bottom: 15px;
            font-size: 14px;
            color: #666;
        }
        #timer {
            color: #ff4d4d;
            font-weight: bold;
        }
        .resend-link {
            background: none;
            border: none;
            color: #0e3e43;
            text-decoration: underline;
            cursor: pointer;
            font-size: 14px;
            display: none;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #666666;
            text-decoration: none;
            font-size: 14px;
        }
    </style>
</head>
<body>

<div class="container">
    <div class="auth-card">
        <c:choose>
            <c:when test="${empty step || step == 1}">
                <h2>Quên mật khẩu</h2>
                <p style="color: #666; margin-bottom: 25px;">Nhập Email hoặc Số điện thoại để nhận mã xác thực.</p>

                <c:if test="${not empty error}">
                    <div class="error-msg"><i class="fa fa-exclamation-circle"></i> ${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/forgotPassword" method="post">
                    <input type="hidden" name="action" value="send_code">
                    <div class="input_group">
                        <i class="fa fa-user"></i>
                        <input type="text" name="account_info" placeholder="Email hoặc số điện thoại" required>
                    </div>
                    <button type="submit" class="login-button" style="width: 100%; margin-top: 10px;">GỬI YÊU CẦU</button>
                </form>
            </c:when>

            <c:when test="${step == 2}">
                <h2>Xác nhận mã</h2>
                <p style="color: #666;">Mã xác minh đã được gửi đến: <br><strong>${sessionScope.resetAccount}</strong></p>

                <c:if test="${not empty error}">
                    <div class="error-msg">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="success-msg">${success}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/forgotPassword" method="post" id="otpForm">
                    <input type="hidden" name="action" value="verify_code">
                    <input type="text" name="otp" id="otpInput" class="otp-input-large"
                           placeholder="••••••" maxlength="6" required autocomplete="off">

                    <div id="timer-box">
                        Mã hết hạn sau: <span id="timer">02:00</span>
                    </div>

                    <button type="submit" id="submitBtn" class="login-button" style="width: 100%;">TIẾP THEO</button>
                </form>
                <form action="${pageContext.request.contextPath}/forgotPassword" method="post" id="resendForm">
                    <input type="hidden" name="action" value="send_code">
                    <input type="hidden" name="account_info" value="${sessionScope.resetAccount}">
                    <button type="submit" id="resendBtn" class="resend-link">Chưa nhận được mã? Gửi lại</button>
                </form>
            </c:when>

            <c:when test="${step == 3}">
                <h2>Đặt lại mật khẩu</h2>
                <p style="color: #666; margin-bottom: 25px;">Mật khẩu mới phải có ít nhất 8 ký tự.</p>

                <c:if test="${not empty error}">
                    <div class="error-msg">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/forgotPassword" method="post">
                    <input type="hidden" name="action" value="update_password">
                    <div class="input_group">
                        <i class="fa fa-lock"></i>
                        <input type="password" name="new_password" placeholder="Mật khẩu mới" required minlength="8">
                    </div>
                    <div class="input_group" style="margin-top: 15px;">
                        <i class="fa fa-shield-alt"></i>
                        <input type="password" name="confirm_password" placeholder="Xác nhận mật khẩu" required minlength="8">
                    </div>
                    <button type="submit" class="login-button" style="width: 100%; margin-top: 20px;">ĐẶT LẠI MẬT KHẨU</button>
                </form>
            </c:when>
        </c:choose>

        <a href="${pageContext.request.contextPath}/login.jsp" class="back-link">
            <i class="fa fa-arrow-left"></i> Quay lại Đăng nhập
        </a>
    </div>
</div>

<script>
    const otpInput = document.getElementById('otpInput');
    const timerDisplay = document.getElementById('timer');
    const resendBtn = document.getElementById('resendBtn');
    const submitBtn = document.getElementById('submitBtn');

    if (otpInput) {
        otpInput.addEventListener('input', function() {
            this.value = this.value.replace(/[^0-9]/g, '');
            if (this.value !== "" && parseInt(this.value) <= 0) {
                this.value = "";
            }
        });

        // 2. Bộ đếm ngược thời gian (120 giây = 2 phút)
        let duration = 120;
        const countdown = setInterval(function () {
            let minutes = Math.floor(duration / 60);
            let seconds = duration % 60;

            minutes = minutes < 10 ? "0" + minutes : minutes;
            seconds = seconds < 10 ? "0" + seconds : seconds;

            timerDisplay.textContent = minutes + ":" + seconds;

            if (--duration < 0) {
                clearInterval(countdown);
                timerDisplay.textContent = "Hết hạn";
                submitBtn.disabled = true;
                submitBtn.style.opacity = "0.5";
                resendBtn.style.display = "inline-block";
            }
        }, 1000);
    }
</script>
</body>
</html>