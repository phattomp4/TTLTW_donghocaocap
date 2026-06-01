<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng | VVP Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/GioHang.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        .cart-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .cart-table th, .cart-table td { padding: 15px; text-align: center; border-bottom: 1px solid #ddd; }
        .cart-img { width: 80px; height: 80px; object-fit: cover; border: 1px solid #eee; }
        .btn-qty { padding: 5px 10px; border: 1px solid #ccc; background: #fff; cursor: pointer; text-decoration: none; color: #333; display: inline-block;}
        .btn-qty:hover { background: #eee; }
        .btn-delete { color: #d0011b; cursor: pointer; }
        .cart-summary { margin-top: 30px; text-align: right; }
        .checkout-btn { background: #d0011b; color: #fff; padding: 12px 25px; text-decoration: none; display: inline-block; margin-top: 10px; border-radius: 4px; font-weight: bold;}
        .btn-empty-cart:hover { background-color: #14555b !important; }
    </style>
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/index.js"></script>
<c:if test="${not empty cartErrorMsg}">
    <script>
        alert('${cartErrorMsg}');
    </script>
</c:if>
<div class="container" style="margin-top: 5px; min-height: 500px; max-width: 1200px; margin-left: auto; margin-right: auto; padding: 20px;">

    <h3 style="font-size: 1.5em; font-weight: bold; border-bottom: 2px solid #1b6e76; padding-bottom: 10px; color: #1b6e76; margin-bottom: 40px;">
        Giỏ Hàng Của Bạn <i class="fa-solid fa-cart-shopping"></i>
    </h3>

    <c:if test="${not empty error}">
        <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #f5c6cb; font-weight: bold;">
            <i class="fa-solid fa-circle-exclamation"></i> ${error}
        </div>
    </c:if>

    <%-- Kiểm tra nếu chưa có giỏ hàng thì hiển thị giao diện trống --%>
    <c:if test="${empty sessionScope.cart}">
        <div style="text-align: center; padding: 50px 0;">
            <i class="fa-solid fa-cart-shopping" style="font-size: 80px; color: #888; margin-bottom: 20px;"></i>
            <p style="color: #666; font-size: 16px; margin-bottom: 20px;">Giỏ hàng của bạn đang trống.</p>

            <a href="${pageContext.request.contextPath}/home" class="btn-empty-cart" style="display: inline-block; background-color: #1b6e76; color: white; padding: 12px 30px; border-radius: 5px; text-decoration: none; font-weight: bold; transition: background 0.3s;">
                Tiếp tục mua sắm
            </a>
        </div>
    </c:if>

    <%-- Kiểm tra nếu có giỏ hàng thì hiển thị danh sách --%>
    <c:if test="${not empty sessionScope.cart}">
        <table class="cart-table">
            <thead>
            <tr style="background: #f9f9f9;">
                <th>Sản phẩm</th>
                <th>Đơn giá</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${sessionScope.cart}" var="item">
                <tr>
                    <td style="text-align: left; display: flex; align-items: center; gap: 15px;">
                        <img src="${item.product.imageUrl}" class="cart-img" alt="Product Image">
                        <div>
                            <b style="font-size: 15px;">${item.product.name}</b>
                            <p style="color: #888; font-size: 13px; margin: 0;">Mã: #${item.product.id}</p>
                        </div>
                    </td>
                    <td><fmt:formatNumber value="${item.product.currentPrice}" pattern="#,##0 ₫"/></td>
                    <td>
                        <a href="cart?action=update&pid=${item.product.id}&quantity=${item.quantity - 1}" class="btn-qty">-</a>
                        <span style="margin: 0 10px; font-weight: bold;">${item.quantity}</span>
                        <a href="cart?action=update&pid=${item.product.id}&quantity=${item.quantity + 1}" class="btn-qty">+</a>
                    </td>
                    <td style="color: #d0011b; font-weight: bold;">
                        <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0 ₫"/>
                    </td>
                    <td>
                        <a href="cart?action=delete&pid=${item.product.id}" class="btn-delete" onclick="return confirm('Bạn chắc chắn muốn xóa sản phẩm này?')">
                            <i class="fa-solid fa-trash"></i> Xóa
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="voucher-input" style="margin-bottom: 20px; text-align: right; margin-top: 20px;">
            <form action="cart" method="GET">
                <input type="text" name="voucherCode" placeholder="Nhập mã giảm giá..." style="padding: 8px; border: 1px solid #ddd; outline: none; border-radius: 4px 0 0 4px;">
                <button type="submit" style="padding: 8px 15px; background: #1b6e76; color: white; border: none; cursor: pointer; border-radius: 0 4px 4px 0;">Áp dụng</button>
            </form>
            <p style="color: #d0011b; margin-top: 5px;">${voucherMessage}</p>
        </div>
        <div class="cart-summary">
            <div style="background: #fdfdfd; padding: 20px; border: 1px solid #eee; display: inline-block; min-width: 300px; text-align: right; border-radius: 8px;">
                <p>Tạm tính: <b><fmt:formatNumber value="${totalMoney}" pattern="#,##0 ₫"/></b></p>
                <c:if test="${discount > 0}">
                    <p>Giảm giá: <b style="color: green;">- <fmt:formatNumber value="${discount}" pattern="#,##0 ₫"/></b></p>
                </c:if>
                <h3 style="color: #d0011b; margin-top: 10px; border-top: 1px dashed #ddd; padding-top: 15px;">
                    Tổng cộng: <fmt:formatNumber value="${finalTotal}" pattern="#,##0 ₫"/>
                </h3>

                <a href="${pageContext.request.contextPath}/checkout" class="checkout-btn" style="width: 100%; text-align: center; box-sizing: border-box; margin-top: 15px;">Tiến hành thanh toán</a>
            </div>
        </div>
    </c:if>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />
</body>
</html>