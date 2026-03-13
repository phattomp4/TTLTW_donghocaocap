<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach items="${listSearch}" var="p">
    <a href="detail?pid=${p.id}" class="live-search-item">
        <div class="live-search-img">
            <img src="${p.imageUrl}" alt="${p.name}">
        </div>
        <div class="live-search-info">
            <p class="live-name">${p.name}</p>
            <p class="live-price">
                <fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
            </p>
        </div>
    </a>
</c:forEach>