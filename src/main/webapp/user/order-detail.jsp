<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng #${order.orderId}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        .detail-container { max-width: 900px; margin: 5px auto 5px; padding: 20px; background: #fff; }
        .info-box { background: #f9f9f9; padding: 20px; border-radius: 8px; margin-bottom: 20px; display: flex; justify-content: space-between; gap: 20px;}
        .info-col h4 { margin-top: 0; color: #1b6e76; border-bottom: 2px solid #ddd; display: inline-block; padding-bottom: 5px; }
        .item-row { display: flex; align-items: center; border-bottom: 1px solid #eee; padding: 15px 0; }
        .item-img { width: 70px; height: 70px; object-fit: cover; border: 1px solid #eee; margin-right: 20px; }
        .item-info { flex: 1; }
        .back-btn { display: inline-block; margin-bottom: 20px; color: #666; text-decoration: none; }
        .back-btn:hover { color: #d0011b; }

        /* Màu trạng thái */
        .status-badge { padding: 5px 10px; border-radius: 4px; color: #fff; font-weight: bold; font-size: 14px; background: #666; }
        .st-Pending { background: #f0ad4e; }
        .st-Processing { background: #0275d8; }
        .st-Shipping { background: #5bc0de; }
        .st-Completed { background: #5cb85c; }
        .st-Cancelled { background: #d9534f; }

        @media (max-width: 768px) {
            .info-box { flex-direction: column; }
        }
    </style>
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />

<div class="detail-container">
    <a href="order-history" class="back-btn"><i class="fa-solid fa-arrow-left"></i> Quay lại lịch sử đơn hàng</a>

    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <h2>Chi tiết đơn hàng #${order.orderId}</h2>
        <span class="status-badge st-${order.status}">${order.status}</span>
    </div>

    <div class="info-box">
        <div class="info-col">
            <h4>Địa chỉ nhận hàng</h4>
            <p><b>${address.name}</b> (${address.phone})</p>
            <p>${address.address} <c:if test="${not empty address.city}"> - ${address.city}</c:if></p>
        </div>
        <div class="info-col" style="text-align: right;">
            <h4>Thông tin thanh toán</h4>
            <p>Phương thức: <b>${order.paymentMethod}</b></p>
            <p>Ngày đặt: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></p>
        </div>
    </div>

    <h4 style="margin-bottom: 10px;">Sản phẩm đã mua</h4>
    <div style="border: 1px solid #eee; padding: 0 20px; border-radius: 8px;">
        <c:forEach items="${details}" var="d">
            <div class="item-row">
                <img src="${d.product.imageUrl}" class="item-img" alt="Ảnh sản phẩm" onerror="this.src='https://via.placeholder.com/70'">

                <div class="item-info">
                    <b style="font-size: 16px;">${d.product.name}</b>

                    <p style="margin: 5px 0; color: #777; font-size: 13px;">Mã SP: #${d.product.id}</p>
                    <p style="margin: 0; color: #555;">Số lượng: x${d.quantity}</p>
                </div>

                <div style="font-weight: bold; color: #d0011b;">
                    <fmt:formatNumber value="${d.priceAtPurchase}" type="currency" currencySymbol="₫"/>
                </div>
            </div>
        </c:forEach>
    </div>

    <div style="text-align: right; margin-top: 20px;">
        <p>Tạm tính: <fmt:formatNumber value="${order.totalAmount + order.discountAmount}" type="currency" currencySymbol="₫"/></p>
        <c:if test="${order.discountAmount > 0}">
            <p>Giảm giá: <span style="color: green;">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="₫"/></span></p>
        </c:if>
        <h3 style="color: #d0011b; margin-top: 10px; border-top: 1px dashed #ddd; padding-top: 10px;">
            Tổng cộng: <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
        </h3>
    </div>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />

</body>
</html>