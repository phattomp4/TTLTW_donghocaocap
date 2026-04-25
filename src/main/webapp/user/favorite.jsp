<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Sản phẩm yêu thích</title>
</head>
<body>
<jsp:include page="../WEB-INF/tags/header.jsp"/>

<div class="container" style="max-width: 1200px; margin: 40px auto; min-height: 50vh;">
    <h2 style="border-bottom: 2px solid #1b6e76; padding-bottom: 10px; color: #1b6e76;">Sản Phẩm Bạn Yêu Thích <i class="fa-solid fa-heart"></i></h2>

    <c:if test="${empty listFavorites}">
        <div style="text-align: center; padding: 50px; color: #777;">
            <i class="fa-regular fa-heart" style="font-size: 50px; margin-bottom: 15px;"></i>
            <p>Danh sách yêu thích của bạn đang trống.</p>
            <a href="${pageContext.request.contextPath}/home" style="padding: 10px 20px; background: #1b6e76; color: white; text-decoration: none; border-radius: 5px;">Khám phá ngay</a>
        </div>
    </c:if>

    <div style="display: grid; grid-template-columns: repeat(auto-fill, minmax(220px, 1fr)); gap: 20px; margin-top: 20px;">
        <c:forEach items="${listFavorites}" var="p">
            <div style="border: 1px solid #eee; padding: 15px; border-radius: 8px; text-align: center; position: relative;">
                <button onclick="toggleFavoriteAjax(${p.id}); this.parentElement.style.display='none';" style="position: absolute; top: 10px; right: 10px; background: #fff; border: 1px solid #ddd; border-radius: 50%; width: 30px; height: 30px; cursor: pointer; color: red;">
                    <i class="fa-solid fa-xmark"></i>
                </button>

                <a href="detail?pid=${p.id}">
                    <img src="${p.imageUrl}" style="width: 100%; height: 200px; object-fit: cover; border-radius: 5px;">
                </a>
                <h4 style="font-size: 15px; margin: 10px 0; height: 40px; overflow: hidden;"><a href="detail?pid=${p.id}" style="text-decoration: none; color: #333;">${p.name}</a></h4>
                <div style="color: #d0011b; font-weight: bold;">
                    <fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫"/>
                </div>

                <form action="add-to-cart" method="GET" style="margin-top: 15px;">
                    <input type="hidden" name="pid" value="${p.id}">
                    <input type="hidden" name="quantity" value="1">
                    <button type="submit" style="width: 100%; padding: 8px; background: #333; color: #fff; border: none; border-radius: 4px; cursor: pointer;">
                        Thêm vào giỏ
                    </button>
                </form>
            </div>
        </c:forEach>
    </div>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp"/>
</body>
</html>