<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Kết quả thanh toán | VVP Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/payment-result.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />

<div class="result-wrapper">
    <div class="result-card status-${status}">
        <div class="status-bar"></div>

        <div class="status-icon">
            <c:choose>
                <c:when test="${status == 'success'}"><i class="fa-solid fa-circle-check"></i></c:when>
                <c:when test="${status == 'failed'}"><i class="fa-solid fa-circle-xmark"></i></c:when>
                <c:when test="${status == 'warning'}"><i class="fa-solid fa-circle-exclamation"></i></c:when>
                <c:otherwise><i class="fa-solid fa-triangle-exclamation"></i></c:otherwise>
            </c:choose>
        </div>

        <h2>${status == 'success' ? 'Thanh toán thành công!' : (status == 'failed' ? 'Thanh toán thất bại' : 'Thông báo')}</h2>

        <p class="message">${msg}</p>

        <c:if test="${not empty orderId}">
            <div class="order-details">
                <div class="detail-row">
                    <span class="detail-label">Mã đơn hàng</span>
                    <span class="detail-value">#${orderId}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Số tiền</span>
                    <span class="detail-value">
                        <fmt:formatNumber value="${amount}" type="currency" currencySymbol="₫"/>
                    </span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Mã giao dịch</span>
                    <span class="detail-value">${transId != null ? transId : 'N/A'}</span>
                </div>
                <div class="detail-row">
                    <span class="detail-label">Thời gian</span>
                    <span class="detail-value"><fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/></span>
                </div>
            </div>
        </c:if>

        <div class="action-group">
            <a href="${pageContext.request.contextPath}/home" class="btn-result btn-home">Về trang chủ</a>

            <c:if test="${allowRepay}">
                <a href="${pageContext.request.contextPath}/repay?orderId=${orderId}" class="btn-result btn-repay">
                    Thanh toán lại ngay
                </a>
            </c:if>

            <c:if test="${status == 'success'}">
                <a href="${pageContext.request.contextPath}/order-history" class="btn-result btn-repay">Xem đơn hàng</a>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />

</body>
</html>