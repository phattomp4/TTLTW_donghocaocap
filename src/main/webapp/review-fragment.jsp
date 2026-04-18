<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:forEach items="${listReviews}" var="r">
    <div class="review-item position-relative" style="border-bottom: 1px solid #eee; padding: 20px 0; display: flex; gap: 15px;">
        <img src="${not empty r.userAvatar ? r.userAvatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}"
             class="review-avatar" style="width: 45px; height: 45px; border-radius: 50%; object-fit: cover; border: 1px solid #ddd;" alt="Avatar">

        <div style="flex: 1;">
            <div class="review-user" style="font-weight: bold; color: #333; margin-bottom: 2px;">${r.username}</div>
            <div class="review-stars" style="color: #ffc107; font-size: 13px; margin-bottom: 5px;">
                <c:forEach begin="1" end="5" var="i">
                    <i class="fa-${i <= r.rating ? 'solid' : 'regular'} fa-star"></i>
                </c:forEach>
            </div>
            <div class="review-date" style="font-size: 12px; color: #999;">
                <fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm"/> | Đã mua sản phẩm này
            </div>
            <div style="font-size: 14.5px; color: #333; line-height: 1.6; margin-top: 10px;">
                    ${r.comment}
            </div>
            <c:if test="${not empty r.imageUrl}">
                <img src="${r.imageUrl}" class="review-img" onclick="window.open(this.src, '_blank')" title="Click để phóng to" style="max-width: 120px; border-radius: 4px; margin-top: 10px; border: 1px solid #ddd; cursor: zoom-in;">
            </c:if>
        </div>

        <c:if test="${sessionScope.acc != null && sessionScope.acc.id == r.userId}">
            <form action="${pageContext.request.contextPath}/delete-review" method="POST" style="position: absolute; top: 20px; right: 0;" onsubmit="return confirm('Bạn có chắc muốn xóa đánh giá này?')">
                <input type="hidden" name="rid" value="${r.id}">
                <input type="hidden" name="pid" value="${pid}">
                <button type="submit" style="background: none; border: none; color: #dc3545; cursor: pointer;" title="Xóa đánh giá">
                    <i class="fa-solid fa-trash-can"></i>
                </button>
            </form>
        </c:if>
    </div>
</c:forEach>