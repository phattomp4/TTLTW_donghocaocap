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
        .detail-container {
            max-width: 1000px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
        }
        .back-btn {
            display: inline-block;
            margin-bottom: 20px;
            color: #1b6e76;
            text-decoration: none;
            font-weight: bold;
        }

        .timeline-wrapper {
            margin: 40px 0;
            padding: 0 20px;
        }
        .timeline {
            display: flex;
            justify-content: space-between;
            position: relative;
        }

        .timeline-track {
            position: absolute;
            top: 15px;
            left: 12.5%;
            right: 12.5%;
            height: 4px;
            background: #eee;
            z-index: 1;
            border-radius: 4px;
        }

        .timeline-bar {
            position: absolute;
            top: 0;
            left: 0;
            height: 100%;
            background: #1b6e76;
            transition: 0.5s;
            border-radius: 4px;
        }

        .step {
            position: relative;
            z-index: 2;
            text-align: center;
            width: 25%;
            color: #999;
        }
        .step .circle {
            width: 34px;
            height: 34px;
            background: #eee;
            border-radius: 50%;
            margin: 0 auto 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            border: 4px solid #fff;
            transition: 0.3s;
        }
        .step p {
            font-size: 13px;
            font-weight: bold;
            margin: 0;
        }

        .step.active {
            color: #1b6e76;
        }
        .step.active .circle {
            background: #1b6e76;
            color: #fff;
            border-color: #e0f2f1;
        }

        .grid-3-cols {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        .info-card {
            background: #f9f9f9;
            padding: 20px;
            border-radius: 8px;
            border: 1px solid #eee;
        }
        .info-card h4 {
            margin-top: 0;
            color: #333; display: flex;
            align-items: center;
            gap: 8px;
            border-bottom: 2px solid #ddd;
            padding-bottom: 10px;
        }

        .log-list {
            max-height: 150px;
            overflow-y: auto;
            padding-left: 10px;
            border-left: 2px dashed #ddd;
            margin-left: 10px;
        }
        .log-item {
            margin-bottom: 15px;
            position: relative;
        }
        .log-item::before {
            content: '';
            position: absolute;
            left: -15px;
            top: 5px;
            width: 8px;
            height: 8px;
            background: #1b6e76;
            border-radius: 50%;
        }
        .log-time {
            font-size: 12px;
            color: #777;
        }
        .log-note {
            font-size: 14px;
            color: #333;
            margin: 2px 0 0;
        }

        .item-row {
            display: flex;
            align-items: center;
            border-bottom: 1px solid #eee;
            padding: 15px 0;
            justify-content: space-between;
        }
        .item-row:last-child {
            border-bottom: none;
        }
        .item-main {
            display: flex;
            align-items: center;
            gap: 15px;
            flex: 1;
        }
        .item-img {
            width: 70px;
            height: 70px;
            object-fit: cover;
            border: 1px solid #eee;
            border-radius: 4px;
        }

        .btn-group {
            display: flex;
            gap: 10px;
            flex-direction: column;
            align-items: flex-end;
        }
        .btn-outline {
            padding: 6px 12px;
            border: 1px solid #d0011b;
            color: #d0011b;
            text-decoration: none;
            border-radius: 4px;
            font-size: 13px;
        }
        .btn-outline:hover {
            background: #d0011b;
            color: #fff;
        }
        .btn-fill {
            padding: 6px 12px;
            background: #1b6e76;
            color: #fff;
            text-decoration: none;
            border-radius: 4px;
            font-size: 13px;
            border: 1px solid #1b6e76;
        }
        .btn-fill:hover {
            background: #14555b;
        }
    </style>
</head>
<body>
<jsp:include page="../WEB-INF/tags/header.jsp" />

<div class="detail-container">
    <a href="order-history" class="back-btn"><i class="fa-solid fa-arrow-left"></i> Trở về lịch sử</a>

    <h2>Chi tiết đơn hàng #${order.orderId}</h2>

    <c:set var="progress" value="0" />
    <c:choose>
        <c:when test="${order.status == 'Pending'}"><c:set var="progress" value="0" /></c:when>
        <c:when test="${order.status == 'Processing'}"><c:set var="progress" value="33.33" /></c:when>
        <c:when test="${order.status == 'Shipping'}"><c:set var="progress" value="66.66" /></c:when>
        <c:when test="${order.status == 'Completed'}"><c:set var="progress" value="100" /></c:when>
    </c:choose>

    <c:if test="${order.status != 'Cancelled'}">
        <div class="timeline-wrapper">
            <div class="timeline">
                <div class="timeline-track">
                    <div class="timeline-bar" style="width: ${progress}%;"></div>
                </div>

                <div class="step ${progress >= 0 ? 'active' : ''}">
                    <div class="circle"><i class="fa-solid fa-clipboard-list"></i></div>
                    <p>Đã đặt hàng</p>
                </div>
                <div class="step ${progress >= 33 ? 'active' : ''}">
                    <div class="circle"><i class="fa-solid fa-box-open"></i></div>
                    <p>Đã xác nhận</p>
                </div>
                <div class="step ${progress >= 66 ? 'active' : ''}">
                    <div class="circle"><i class="fa-solid fa-truck-fast"></i></div>
                    <p>Đang giao</p>
                </div>
                <div class="step ${progress == 100 ? 'active' : ''}">
                    <div class="circle"><i class="fa-solid fa-house-circle-check"></i></div>
                    <p>Hoàn thành</p>
                </div>
            </div>
        </div>
    </c:if>

    <c:if test="${order.status == 'Cancelled'}">
        <div style="background: #f8d7da; color: #721c24; padding: 15px; text-align: center; border-radius: 8px; font-weight: bold; margin-bottom: 20px;">
            <i class="fa-solid fa-circle-xmark"></i> Đơn hàng này đã bị hủy.
        </div>
    </c:if>

    <div class="grid-3-cols">
        <div class="info-card">
            <h4><i class="fa-solid fa-location-dot"></i> Địa chỉ nhận hàng</h4>
            <p><b>${address.name}</b></p>
            <p><i class="fa-solid fa-phone" style="font-size: 12px; width: 15px;"></i> ${address.phone}</p>
            <p><i class="fa-solid fa-map" style="font-size: 12px; width: 15px;"></i> ${address.address} <c:if test="${not empty address.city}"> - ${address.city}</c:if></p>
        </div>

        <div class="info-card">
            <h4><i class="fa-solid fa-credit-card"></i> Thông tin thanh toán</h4>
            <p>Phương thức: <b>${order.paymentMethod}</b></p>
            <p>Trạng thái:
                <c:choose>
                    <c:when test="${order.paymentStatus == 'Paid'}"><span style="color: green; font-weight: bold;">Đã thanh toán</span></c:when>
                    <c:otherwise><span style="color: #d0011b; font-weight: bold;">Chưa thanh toán</span></c:otherwise>
                </c:choose>
            </p>
            <p>Ngày đặt: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></p>
        </div>

        <div class="info-card">
            <h4><i class="fa-solid fa-clock-rotate-left"></i> Lịch sử hành trình</h4>
            <c:if test="${empty logs}">
                <p style="color: #999; font-style: italic;">Chưa có lịch sử cập nhật.</p>
            </c:if>
            <c:if test="${not empty logs}">
                <div class="log-list">
                    <c:forEach items="${logs}" var="log">
                        <div class="log-item">
                            <div class="log-time"><fmt:formatDate value="${log.createdAt}" pattern="dd/MM/yyyy - HH:mm"/></div>
                            <div class="log-note"><b>${log.status}:</b> ${log.note}</div>
                        </div>
                    </c:forEach>
                </div>
            </c:if>
        </div>
    </div>

    <h4 style="margin-bottom: 15px;"><i class="fa-solid fa-bag-shopping"></i> Sản phẩm đã mua</h4>
    <div style="border: 1px solid #eee; padding: 0 20px; border-radius: 8px; margin-bottom: 20px;">
        <c:forEach items="${details}" var="d">
            <div class="item-row">
                <div class="item-main">
                    <img src="${d.product.imageUrl}" class="item-img" alt="Ảnh SP" onerror="this.src='https://via.placeholder.com/70'">
                    <div>
                        <b style="font-size: 16px;">${d.product.name}</b>
                        <p style="margin: 5px 0; color: #777; font-size: 13px;">Số lượng: x${d.quantity}</p>
                        <div style="font-weight: bold; color: #d0011b;">
                            <fmt:formatNumber value="${d.priceAtPurchase}" type="currency" currencySymbol="₫"/>
                        </div>
                    </div>
                </div>

                <c:if test="${order.status == 'Completed'}">
                    <div class="btn-group">
                        <a href="review?productId=${d.product.id}&orderId=${order.orderId}" class="btn-fill">Đánh giá</a>
                        <a href="return-request?productId=${d.product.id}&orderId=${order.orderId}" class="btn-outline">Trả hàng</a>
                    </div>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <div style="text-align: right; background: #fdfdfd; padding: 20px; border-radius: 8px; border: 1px solid #eee;">
        <p>Tạm tính: <fmt:formatNumber value="${order.totalAmount + order.discountAmount}" type="currency" currencySymbol="₫"/></p>
        <c:if test="${order.discountAmount > 0}">
            <p>Giảm giá voucher: <span style="color: green;">-<fmt:formatNumber value="${order.discountAmount}" type="currency" currencySymbol="₫"/></span></p>
        </c:if>
        <h3 style="color: #d0011b; margin-top: 10px; border-top: 1px dashed #ddd; padding-top: 10px;">
            Tổng thanh toán: <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
        </h3>
    </div>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />
</body>
</html>