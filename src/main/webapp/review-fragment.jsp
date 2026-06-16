<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<c:set var="myReview" value="${null}" />
<c:forEach items="${listReviews}" var="r">
    <c:if test="${sessionScope.acc != null && sessionScope.acc.id == r.userId}">
        <c:set var="myReview" value="${r}" />
    </c:if>
</c:forEach>

<c:if test="${not empty sessionScope.acc}">
    <div style="background: #fdfdfd; padding: 20px; border-radius: 8px; margin-bottom: 30px; border: 1px solid #e0e0e0;">
        <h3 style="margin-top: 0; color: #1b6e76;">
            <c:choose>
                <c:when test="${not empty myReview}">Chỉnh sửa đánh giá của bạn</c:when>
                <c:otherwise>Viết đánh giá sản phẩm</c:otherwise>
            </c:choose>
        </h3>

        <div id="review-msg" style="display:none; padding: 10px; margin-bottom: 10px; border-radius: 4px; font-weight: bold; text-align: center;"></div>

        <form id="reviewForm" enctype="multipart/form-data">
            <input type="hidden" name="productId" value="${not empty pid ? pid : param.pid}">
            <input type="hidden" name="reviewId" value="${not empty myReview ? myReview.id : ''}">

            <div style="margin-bottom: 15px; display: flex; align-items: center; gap: 10px;">
                <label style="font-weight: bold;">Đánh giá sao:</label>
                <select name="rating" style="padding: 8px; border-radius: 4px; border: 1px solid #ccc; outline: none;">
                    <option value="5" ${myReview.rating == 5 ? 'selected' : ''}>5 Sao ⭐⭐⭐⭐⭐</option>
                    <option value="4" ${myReview.rating == 4 ? 'selected' : ''}>4 Sao ⭐⭐⭐⭐</option>
                    <option value="3" ${myReview.rating == 3 ? 'selected' : ''}>3 Sao ⭐⭐⭐</option>
                    <option value="2" ${myReview.rating == 2 ? 'selected' : ''}>2 Sao ⭐⭐</option>
                    <option value="1" ${myReview.rating == 1 ? 'selected' : ''}>1 Sao ⭐</option>
                </select>
            </div>

            <div style="margin-bottom: 15px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Nội dung:</label>
                <textarea name="comment" rows="3" style="width: 100%; padding: 10px; border-radius: 4px; border: 1px solid #ccc; font-family: inherit; box-sizing: border-box;" placeholder="Bạn cảm thấy sản phẩm này thế nào?" required>${myReview.comment}</textarea>
            </div>

            <div style="margin-bottom: 15px;">
                <label style="font-weight: bold; display: block; margin-bottom: 5px;">Hình ảnh (Tùy chọn):</label>
                <input type="file" name="reviewImage" accept="image/*" style="border: 1px solid #ccc; padding: 5px; width: 100%; border-radius: 4px; box-sizing: border-box;">
                <c:if test="${not empty myReview.imageUrl}">
                    <div style="margin-top: 10px;">
                        <span style="font-size: 13px; color: #666;">Ảnh cũ của bạn:</span>
                        <img src="${myReview.imageUrl}" style="height: 50px; border-radius: 4px; border: 1px solid #ddd; vertical-align: middle; margin-left: 10px;">
                    </div>
                </c:if>
            </div>

            <button type="button" onclick="submitReviewAjax()" style="background: #1b6e76; color: white; border: none; padding: 10px 20px; border-radius: 4px; font-weight: bold; cursor: pointer;">Gửi Đánh Giá</button>
        </form>
    </div>

    <script>
        function submitReviewAjax() {
            const form = document.getElementById('reviewForm');
            const formData = new FormData(form);
            const msgBox = document.getElementById('review-msg');

            fetch('${pageContext.request.contextPath}/submit-review', {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())
                .then(data => {
                    msgBox.style.display = 'block';
                    if (data.startsWith('success')) {
                        msgBox.style.backgroundColor = '#d4edda';
                        msgBox.style.color = '#155724';
                        msgBox.innerText = 'Đã lưu đánh giá thành công!';
                        setTimeout(() => window.location.reload(), 1500); // Reload để tải lại bình luận
                    } else {
                        msgBox.style.backgroundColor = '#f8d7da';
                        msgBox.style.color = '#721c24';
                        msgBox.innerText = data;
                    }
                })
                .catch(error => {
                    msgBox.style.display = 'block';
                    msgBox.style.backgroundColor = '#f8d7da';
                    msgBox.style.color = '#721c24';
                    msgBox.innerText = 'Lỗi kết nối máy chủ!';
                });
        }
    </script>
</c:if>


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
                <input type="hidden" name="pid" value="${not empty pid ? pid : param.pid}">
                <button type="submit" style="background: none; border: none; color: #dc3545; cursor: pointer;" title="Xóa đánh giá">
                    <i class="fa-solid fa-trash-can"></i>
                </button>
            </form>
        </c:if>
    </div>
</c:forEach>