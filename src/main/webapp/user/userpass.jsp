<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Khôi phục mật khẩu</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/forgot_password.css">

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
        .code-input-group {
            display: flex;
            justify-content: center;
            gap: 10px;
            margin: 25px 0;
        }
        .code-box {
            width: 45px;
            height: 50px;
            text-align: center;
            font-size: 24px;
            font-weight: bold;
            border: 2px solid #dddddd;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        .code-box:focus {
            border-color: #0e3e43;
            box-shadow: 0 0 8px rgba(128, 0, 128, 0.2);
            outline: none;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #666666;
            text-decoration: none;
            font-size: 14px;
        }
        .back-link:hover {
            color: #0e3e43;
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
                <p style="color: #666;">Mã xác minh đã được gửi đến: <br><strong>${sessionScope.resetAccount}</strong>
                </p>

                <c:if test="${not empty error}">
                    <div class="error-msg">${error}</div>
                </c:if>

                <form action="${pageContext.request.contextPath}/forgotPassword" method="post">
                    <input type="hidden" name="action" value="verify_code">
                    <div class="code-input-group">
                        <c:forEach var="i" begin="1" end="6">
                            <input type="number" name="digit" maxlength="1" class="code-box" required
                                   oninput="moveNext(this)">
                        </c:forEach>
                    </div>
                    <button type="submit" class="btn_login" style="width: 100%;">TIẾP THEO</button>
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
                        <input type="password" name="confirm_password" placeholder="Xác nhận mật khẩu" required
                               minlength="8">
                    </div>
                    <button type="submit" class="btn_login" style="width: 100%; margin-top: 20px;">ĐẶT LẠI MẬT KHẨU
                    </button>
                </form>
            </c:when>
        </c:choose>
    <%-- Fix lại lỗi quay lại trang đăng nhập--%>
        <a href="${pageContext.request.contextPath}/login.jsp" class="back-link">
            <i class="fa fa-arrow-left"></i> Quay lại Đăng nhập
        </a>
    </div>
</div>


<script>
    function moveNext(el) {
        if (el.value.length === 1 && el.nextElementSibling) {
            el.nextElementSibling.focus();
        }
    }
    document.querySelectorAll('.code-box').forEach((box, idx, all) => {
        box.addEventListener('keydown', (e) => {
            if (e.key === 'Backspace' && !box.value && idx > 0) {
                all[idx - 1].focus();
            }
        });
    });
</script>
</body>
</html>