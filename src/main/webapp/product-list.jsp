<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="assets/css/ProductCards.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<jsp:include page="WEB-INF/tags/header.jsp" />
<div class="Product-list-container" style="margin-top: 150px;">
    <h2 style="text-align: center; margin-bottom: 20px; text-transform: uppercase;">${pageTitle}</h2>

    <div class="Product-page">
        <c:if test="${empty listProduct}">
            <p style="text-align: center; width: 100%;">Không tìm thấy sản phẩm nào.</p>
        </c:if>

        <c:forEach items="${listProduct}" var="p">
            <div class="link-product">
                <div class="Product">
                    <a href="${pageContext.request.contextPath}/detail?pid=${p.id}" class="img-product">
                        <img src="${p.imageUrl}" alt="${p.name}" onerror="this.src='https://via.placeholder.com/300'">
                    </a>
                    <br />
                    <a href="${pageContext.request.contextPath}/detail?pid=${p.id}"><p class="Item">${p.name}</p></a>
                    <p class="PriceOfPoduct"><fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></p>
                    <div class="Discount-row">
                        <p class="Discount"><fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></p>
                    </div>
                    <p class="Sold">Đã bán ${p.soldQuantity}</p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="WEB-INF/tags/footer.jsp" />
</body>
</html>