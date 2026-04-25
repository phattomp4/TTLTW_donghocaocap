<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ page isELIgnored="false" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${p.name} - Chi tiết sản phẩm</title>

    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/TrangChiTietSanPham.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">

    <style>
        .star-rating { display: flex; flex-direction: row-reverse; justify-content: flex-end; gap: 5px;}
        .star-rating input { display: none; }
        .star-rating label { font-size: 25px; color: #ccc; cursor: pointer; transition: 0.2s;}
        .star-rating input:checked ~ label,
        .star-rating label:hover,
        .star-rating label:hover ~ label { color: #ffc107; }

        .review-item { border-bottom: 1px solid #eee; padding: 20px 0; display: flex; gap: 15px;}
        .review-avatar { width: 45px; height: 45px; border-radius: 50%; object-fit: cover; border: 1px solid #ddd;}
        .review-user { font-weight: bold; color: #333; margin-bottom: 2px; }
        .review-date { font-size: 12px; color: #999; }
        .review-stars { color: #ffc107; font-size: 13px; margin-bottom: 5px; }
        .review-img { max-width: 120px; border-radius: 4px; margin-top: 10px; border: 1px solid #ddd; cursor: zoom-in;}
    </style>
</head>
<body>

<jsp:include page="WEB-INF/tags/header.jsp"></jsp:include>
<c:set var="pName" value="${fn:toLowerCase(p.name)}" />
<c:set var="isAccessory" value="${fn:contains(pName, 'dây') or fn:contains(pName, 'hộp') or fn:contains(pName, 'khóa') or fn:contains(pName, 'phụ kiện')}" />

<div class="product-detail-container" style="margin-top: 5px; display: flex; gap: 30px; padding: 0 10%;">

    <div class="product-images" style="width: 50%;">
        <div class="main-image" style="margin-bottom: 10px;">
            <img src="${p.imageList.size() > 0 ? p.imageList[0] : p.imageUrl}" id="mainImg" alt="${p.name}" style="width: 100%; border-radius: 8px;">
        </div>
        <div class="thumbnail-list" style="display: flex; gap: 10px; overflow-x: auto;">
            <c:forEach items="${p.imageList}" var="img">
                <img src="${img}" onclick="document.getElementById('mainImg').src='${img}'" style="width: 80px; height: 80px; object-fit: cover; cursor: pointer; border: 1px solid #ddd; border-radius: 4px;">
            </c:forEach>
        </div>
    </div>

    <div class="product-info" style="width: 50%;">
        <h1 style="font-size: 24px; margin-bottom: 10px;">${p.name}</h1>
        <p style="color: #666;">Mã SP: <strong>${p.sku}</strong> | Tình trạng: <strong>${p.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}</strong></p>

        <div class="price-box" style="margin: 20px 0;">
            <span style="color: #d0011b; font-size: 28px; font-weight: bold;"><fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫"/></span>
            <span style="text-decoration: line-through; color: #888; margin-left: 15px;"><fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫"/></span>
        </div>

        <div class="specs-table" style="margin-top: 40px;">
            <h3 style="border-bottom: 2px solid #ddd; padding-bottom: 10px;">THÔNG SỐ KỸ THUẬT</h3>
            <table style="width: 100%; border-collapse: collapse; margin-top: 15px;">
                <c:if test="${not empty p.specifications['Thương hiệu']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Thương hiệu</td><td>${p.specifications['Thương hiệu']}</td></tr></c:if>
                <c:if test="${not empty p.specifications['Xuất xứ']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Xuất xứ</td><td>${p.specifications['Xuất xứ']}</td></tr></c:if>
                <c:if test="${not empty p.specifications['Đối tượng']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Đối tượng</td><td>${p.specifications['Đối tượng']}</td></tr></c:if>
                <c:if test="${not empty p.specifications['Chống nước']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Chống nước</td><td>${p.specifications['Chống nước']}</td></tr></c:if>
                <c:if test="${not empty p.specifications['Loại máy']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Loại máy</td><td>${p.specifications['Loại máy']}</td></tr></c:if>
                <c:if test="${not empty p.specifications['Chất liệu kính']}"><tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Chất liệu kính</td><td>${p.specifications['Chất liệu kính']}</td></tr></c:if>
            </table>
        </div>

        <div class="actions">
            <form id="productForm" action="${pageContext.request.contextPath}/add-to-cart" method="GET">
                <input type="hidden" name="pid" value="${p.id}">
                <div class="quantity-box">
                    <label>Số lượng:</label>
                    <input type="number" id="qtyInput" name="quantity" value="1" min="1" style="width: 50px; text-align: center;" ${p.stockQuantity <= 0 ? 'disabled' : ''}>
                </div>

                <c:choose>
                    <c:when test="${p.stockQuantity > 0}">
                        <div class="action-buttons" style="margin-top: 20px; display: flex; gap: 10px; flex-wrap: wrap;">
                            <c:choose>
                                <c:when test="${sessionScope.acc != null && sessionScope.acc.role == 'Admin'}">
                                    <a href="admin/product-form?id=${p.id}" class="btn-buy-now" style="background: #343a40; color: white; border: none; padding: 10px 30px; text-decoration: none; display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%;">
                                        <i class="fa-solid fa-screwdriver-wrench"></i> QUẢN LÝ / SỬA SẢN PHẨM NÀY
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" onclick="addToCartAjax()" class="btn-add-cart" style="background: #fff; border: 1px solid #d0011b; color: #d0011b; padding: 10px 20px; cursor: pointer;"><i class="fa-solid fa-cart-plus"></i> Thêm vào giỏ</button>
                                    <button type="submit" name="action" value="buynow" class="btn-buy-now" style="background: #d0011b; color: white; border: none; padding: 10px 30px; cursor: pointer;">Mua ngay</button>

                                    <button type="button" id="btnFavorite" onclick="toggleFavoriteAjax(${p.id})" style="background: #fff; border: 1px solid #ccc; padding: 10px 15px; border-radius: 4px; cursor: pointer; color: ${isFavorite ? '#d0011b' : '#333'};">
                                        <i id="heartIcon" class="fa-${isFavorite ? 'solid' : 'regular'} fa-heart"></i>
                                        <span id="favText">${isFavorite ? 'Đã yêu thích' : 'Lưu yêu thích'}</span>
                                    </button>

                                </c:otherwise>
                            </c:choose>
                        </div>
                        <p style="color: green; margin-top: 10px; font-size: 14px;"><i class="fa-solid fa-check-circle"></i> Còn lại ${p.stockQuantity} sản phẩm</p>
                    </c:when>
                    <c:otherwise>
                        <div class="out-of-stock-alert" style="margin-top: 20px; padding: 15px; background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; border-radius: 5px;">
                            <strong style="font-size: 16px;"><i class="fa-solid fa-circle-exclamation"></i> Sản phẩm tạm hết hàng</strong>
                            <p style="margin: 5px 0 0 0; font-size: 14px;">Vui lòng quay lại sau hoặc liên hệ cửa hàng để được hỗ trợ.</p>
                        </div>
                    </c:otherwise>
                </c:choose>
            </form>
            <div id="toast" style="visibility: hidden; min-width: 250px; margin-left: -125px; background-color: #333; color: #fff; text-align: center; border-radius: 2px; padding: 16px; position: fixed; z-index: 1000; left: 50%; bottom: 30px; font-size: 17px;">
                <i class="fa-solid fa-check"></i> Đã thêm sản phẩm vào giỏ!
            </div>
        </div>
    </div>
</div>

<div class="description-product" style="padding:5px 85px 5px 85px;">
    <h3 style="border-bottom: 2px solid #ddd; padding-bottom: 10px;">MÔ TẢ CHI TIẾT</h3>
    <p class="desc">${p.description}</p>
</div>

<div class="container" style="max-width: 1200px; margin: 50px auto;">
    <h3 style="color: #1b6e76; font-weight: 800; border-bottom: 2px solid #eaeaea; padding-bottom: 10px; margin-bottom: 25px;">ĐÁNH GIÁ SẢN PHẨM</h3>
    <div style="background: #fff; border: 1px solid #ddd; padding: 15px; margin-bottom: 25px; border-radius: 5px; display: flex; gap: 10px; flex-wrap: wrap;">
        <span style="font-weight: bold; margin-right: 15px; display: flex; align-items: center;">Lọc theo:</span>
        <button class="filter-btn active" onclick="applyFilter(0, false, this)">Tất cả</button>
        <button class="filter-btn" onclick="applyFilter(5, false, this)">5 Sao</button>
        <button class="filter-btn" onclick="applyFilter(4, false, this)">4 Sao</button>
        <button class="filter-btn" onclick="applyFilter(3, false, this)">3 Sao</button>
        <button class="filter-btn" onclick="applyFilter(2, false, this)">2 Sao</button>
        <button class="filter-btn" onclick="applyFilter(1, false, this)">1 Sao</button>
        <button class="filter-btn" onclick="applyFilter(0, true, this)">Có hình ảnh</button>
    </div>

    <style>
        .filter-btn { border: 1px solid #ccc; background: #fff; padding: 5px 15px; border-radius: 20px; cursor: pointer; transition: 0.2s;}
        .filter-btn:hover { border-color: #1b6e76; color: #1b6e76;}
        .filter-btn.active { border-color: #1b6e76; color: #1b6e76; font-weight: bold; background: #e9fcfe;}
    </style>
    <c:if test="${!canReview}">
        <div style="background: #e9f7fd; color: #31708f; padding: 15px; border-radius: 5px; border: 1px solid #bce8f1; margin-bottom: 30px;">
            <i class="fa-solid fa-circle-info"></i> Chỉ những khách hàng đã mua và nhận sản phẩm này mới có quyền viết đánh giá.
        </div>
    </c:if>

    <c:if test="${canReview}">
        <div id="reviewFormContainer" style="background: #f8f9fa; border-radius: 8px; border: 1px solid #eee; padding: 25px; margin-bottom: 40px;">
            <h5 style="margin-top: 0; margin-bottom: 15px; font-weight: bold;">
                    ${myReview != null ? 'Chỉnh sửa đánh giá của bạn' : 'Viết đánh giá của bạn'}
            </h5>

            <form id="reviewForm" onsubmit="submitReviewAjax(event)" enctype="multipart/form-data">
                <input type="hidden" name="productId" value="${p.id}">

                <c:if test="${myReview != null}">
                    <input type="hidden" name="reviewId" value="${myReview.id}">
                </c:if>

                <div style="margin-bottom: 15px; display: flex; align-items: center; justify-content: space-between;">
                    <label style="font-weight: 600; display: block; margin-bottom: 5px;">Chất lượng sản phẩm: <span style="color: #d0011b; font-weight: bold;">*</span></label>
                    <div style="display: flex; align-items: center; gap: 15px;">
                        <div class="star-rating" id="starRatingContainer">
                            <input type="radio" id="star5" name="rating" value="5"/><label for="star5"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" id="star4" name="rating" value="4"/><label for="star4"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" id="star3" name="rating" value="3"/><label for="star3"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" id="star2" name="rating" value="2"/><label for="star2"><i class="fa-solid fa-star"></i></label>
                            <input type="radio" id="star1" name="rating" value="1"/><label for="star1"><i class="fa-solid fa-star"></i></label>
                        </div>
                        <button type="button" onclick="clearRating()" style="background: none; border: none; font-size: 12px; color: #dc3545; cursor: pointer; text-decoration: underline;">Bỏ chọn</button>
                    </div>
                </div>

                <div style="margin-bottom: 15px;">
                    <textarea name="comment" rows="3" placeholder="Chia sẻ trải nghiệm của bạn về sản phẩm..." required style="width: 100%; padding: 12px; border-radius: 5px; border: 1px solid #ccc; outline: none; resize: vertical;">${myReview != null ? myReview.comment : ''}</textarea>
                </div>

                <c:if test="${myReview != null && not empty myReview.imageUrl}">
                    <div style="margin-bottom: 10px;">
                        <span style="font-size: 12px; color: #666;">Ảnh hiện tại:</span><br>
                        <img src="${myReview.imageUrl}" style="height: 60px; border-radius: 5px; border: 1px solid #ddd;">
                    </div>
                </c:if>

                <div style="margin-bottom: 20px;">
                    <label style="cursor: pointer; background: #fff; border: 1px solid #ccc; padding: 8px 15px; border-radius: 5px; font-size: 14px;">
                        <i class="fa-solid fa-camera" style="color: #666;"></i> Đính kèm ảnh thực tế
                        <input type="file" name="reviewImage" accept="image/*" style="display: none;" onchange="previewImage(this)">
                    </label>
                    <div style="margin-top: 10px;">
                        <img id="imgPreview" src="" style="max-height: 80px; display: none; border-radius: 5px; border: 1px solid #ddd;">
                    </div>
                </div>

                <button type="submit" id="btnSubmitReview" style="background-color: #1b6e76; color: white; border: none; padding: 10px 25px; border-radius: 5px; font-weight: bold; cursor: pointer;">
                        ${myReview != null ? 'Cập Nhật Đánh Giá' : 'Gửi Đánh Giá'}
                </button>
            </form>
        </div>

        <c:if test="${myReview != null}">
            <script>
                document.addEventListener('DOMContentLoaded', function() {
                    const oldRating = ${myReview.rating};
                    if(oldRating > 0) {
                        document.getElementById('star' + oldRating).checked = true;
                    }
                });
            </script>
        </c:if>
    </c:if>

    <div class="review-list" id="reviewContainer">
        <c:if test="${empty listReviews}">
            <div id="emptyReviewMsg" style="text-align: center; padding: 40px 0; color: #999;">
                <i class="fa-regular fa-comment-dots" style="font-size: 40px; margin-bottom: 15px;"></i>
                <p>Chưa có đánh giá nào cho sản phẩm này.<br>Hãy trở thành người đầu tiên đánh giá nhé!</p>
            </div>
        </c:if>

        <c:forEach items="${listReviews}" var="r">
            <div class="review-item position-relative">
                <img src="${not empty r.userAvatar ? r.userAvatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}" class="review-avatar" alt="Avatar">

                <div style="flex: 1;">
                    <div class="review-user">${r.username}</div>

                    <div class="review-stars">
                        <c:choose>
                            <c:when test="${r.rating > 0}">
                                <c:forEach begin="1" end="5" var="i"><i class="fa-${i <= r.rating ? 'solid' : 'regular'} fa-star"></i></c:forEach>
                            </c:when>
                            <c:otherwise>
                                <span style="color: #999; font-style: italic;">Chưa đánh giá sao</span>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="review-date"><fmt:formatDate value="${r.createdAt}" pattern="dd/MM/yyyy HH:mm"/> | Đã mua sản phẩm này</div>
                    <div style="font-size: 14.5px; color: #333; line-height: 1.6; margin-top: 10px;">${r.comment}</div>

                    <c:if test="${not empty r.imageUrl}">
                        <img src="${r.imageUrl}" class="review-img" onclick="window.open(this.src, '_blank')" title="Click để phóng to">
                    </c:if>

                    <div style="margin-top: 15px; display: flex; gap: 15px; align-items: center;">
                        <button type="button" onclick="likeReview(${r.id}, this)" style="background:none; border:none; color:#666; cursor:pointer; font-size:13px; padding:0;">
                            <i class="fa-regular fa-thumbs-up"></i> Hữu ích (<span class="like-count">${r.likes}</span>)
                        </button>
                        <button type="button" onclick="toggleReplyForm(${r.id})" style="background:none; border:none; color:#666; cursor:pointer; font-size:13px; padding:0;">
                            <i class="fa-regular fa-comment"></i> Trả lời
                        </button>
                    </div>

                    <div id="repliesContainer_${r.id}">
                        <c:forEach items="${r.replies}" var="rep">
                            <div style="display:flex; gap:10px; margin-top:15px; background:#f8f9fa; border-left: 3px solid #1b6e76; padding:10px; border-radius:4px;">
                                <img src="${not empty rep.userAvatar ? rep.userAvatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}" style="width:30px; height:30px; border-radius:50%; object-fit:cover;">
                                <div style="flex: 1;">
                                    <div style="font-weight:bold; font-size:13px; color:#333;">
                                            ${rep.username}
                                        <c:if test="${rep.role == 'Admin'}"><span style="background:#d0011b; color:#fff; font-size:10px; padding:2px 5px; border-radius:3px; margin-left:5px;">Quản trị viên</span></c:if>
                                    </div>
                                    <div style="font-size:13.5px; color:#555; margin-top:3px;">${rep.replyText}</div>

                                    <div style="margin-top: 5px; display: flex; gap: 15px; align-items: center;">
                                        <button type="button" onclick="likeReply(${rep.replyId}, this)" style="background:none; border:none; color:#888; cursor:pointer; font-size:11px; padding:0;">
                                            <i class="fa-regular fa-thumbs-up"></i> Hữu ích (<span class="like-count">${rep.likes}</span>)
                                        </button>
                                        <button type="button" onclick="replyToUser(${r.id}, '${rep.username}')" style="background:none; border:none; color:#888; cursor:pointer; font-size:11px; padding:0;">
                                            <i class="fa-solid fa-reply"></i> Phản hồi
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <div id="replyFormBox_${r.id}" style="display:none; margin-top:15px; display:flex; gap:10px;">
                        <input type="text" id="replyInput_${r.id}" placeholder="Viết câu trả lời..." style="flex:1; padding:8px 12px; border:1px solid #ddd; border-radius:4px; outline:none;">
                        <button type="button" onclick="submitReply(${r.id})" style="background:#1b6e76; color:#fff; border:none; padding:0 15px; border-radius:4px; cursor:pointer;">Gửi</button>
                    </div>
                </div>

                <c:if test="${sessionScope.acc != null && sessionScope.acc.id == r.userId}">
                    <form action="delete-review" method="POST" class="position-absolute top-0 end-0" onsubmit="return confirm('Bạn có chắc muốn xóa đánh giá này?')">
                        <input type="hidden" name="rid" value="${r.id}">
                        <input type="hidden" name="pid" value="${p.id}">
                        <button type="submit" class="btn btn-link text-danger p-0" title="Xóa đánh giá" style="background:none; border:none; cursor:pointer;">
                            <i class="fa-solid fa-trash-can" style="color: #dc3545;"></i>
                        </button>
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>

    <div class="text-center mt-4" id="loadMoreSection" style="text-align: center; margin-top: 20px;">
        <c:if test="${totalReviews > 10}">
            <button id="btnLoadMore" style="background: none; border: 1px solid #6c757d; color: #6c757d; padding: 8px 20px; border-radius: 4px; cursor: pointer;" onclick="loadMoreReviews(${p.id})">
                Xem thêm đánh giá (<span id="remainingCount">${totalReviews - 10}</span>)
            </button>
        </c:if>
    </div>

</div>

<script>
    function addToCartAjax() {
        const pidInput = document.querySelector('input[name="pid"]');
        const pid = pidInput ? pidInput.value : "";
        if (!pid) return alert("Lỗi: Không tìm thấy ID sản phẩm.");
        const qtyInput = document.getElementById('qtyInput');
        const qty = qtyInput ? qtyInput.value : 1;

        fetch('${pageContext.request.contextPath}/add-to-cart?pid=' + pid + '&quantity=' + qty + '&ajax=true')
            .then(response => {
                if (!response.ok) throw new Error("Lỗi Server");
                return response.text();
            })
            .then(data => {
                document.querySelectorAll(".cart-count").forEach(el => el.innerText = data);
                const toast = document.getElementById("toast");
                if (toast) {
                    toast.style.visibility = "visible";
                    setTimeout(function(){ toast.style.visibility = "hidden"; }, 3000);
                }
            })
            .catch(error => alert("Không thể thêm vào giỏ. Vui lòng kiểm tra Console."));
    }
    
    function toggleFavoriteAjax(pid) {
        fetch('${pageContext.request.contextPath}/toggle-favorite', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'pid=' + pid
        })
            .then(res => res.text())
            .then(data => {
                if (data === "unauthorized") {
                    alert("Vui lòng đăng nhập để sử dụng tính năng yêu thích!");
                    window.location.href = "login.jsp"; // Chuyển hướng tới trang đăng nhập
                    return;
                }

                const icon = document.getElementById("heartIcon");
                const btn = document.getElementById("btnFavorite");
                const text = document.getElementById("favText");

                if (data === "added") {
                    icon.classList.remove("fa-regular");
                    icon.classList.add("fa-solid");
                    btn.style.color = "#d0011b";
                    text.innerText = "Đã yêu thích";
                } else if (data === "removed") {
                    icon.classList.remove("fa-solid");
                    icon.classList.add("fa-regular");
                    btn.style.color = "#333";
                    text.innerText = "Lưu yêu thích";
                }
            });
    }

    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('imgPreview').src = e.target.result;
                document.getElementById('imgPreview').style.display = 'block';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }

    function clearRating() {
        const radios = document.querySelectorAll('input[name="rating"]');
        for (let i = 0; i < radios.length; i++) radios[i].checked = false;
    }

    function submitReviewAjax(event) {
        event.preventDefault();
        const form = document.getElementById('reviewForm');
        const formData = new FormData(form);
        const btnSubmit = document.getElementById('btnSubmitReview');

        const checkedStar = form.querySelector('input[name="rating"]:checked');
        const ratingVal = checkedStar ? checkedStar.value : "0";

        if (ratingVal === "0") {
            alert("Vui lòng chọn số sao để đánh giá sản phẩm!");
            return;
        }

        formData.set('rating', ratingVal);

        btnSubmit.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang gửi...';
        btnSubmit.disabled = true;

        fetch('${pageContext.request.contextPath}/submit-review', { method: 'POST', body: formData })
            .then(response => response.text())
            .then(data => {
                const result = data.trim();
                if (result.startsWith('success')) {
                    const uploadedImgUrl = result.split('|')[1];
                    const emptyMsg = document.getElementById('emptyReviewMsg');
                    if(emptyMsg) emptyMsg.style.display = 'none';

                    appendNewReviewToUI(ratingVal, formData.get('comment'), uploadedImgUrl);

                    form.reset();
                    clearRating();
                    document.getElementById('imgPreview').src = '';
                    document.getElementById('imgPreview').style.display = 'none';

                } else {
                    alert("Lỗi: " + result);
                }
            })
            .catch(error => alert("Đã xảy ra lỗi mạng. Vui lòng thử lại!"))
            .finally(() => {
                if (btnSubmit) {
                    btnSubmit.innerHTML = 'Gửi Đánh Giá';
                    btnSubmit.disabled = false;
                }
            });
    }

    function appendNewReviewToUI(ratingValue, comment, imgUrl) {
        const reviewContainer = document.getElementById('reviewContainer');
        const username = '${sessionScope.acc.username}';
        const avatar = '${not empty sessionScope.acc.avatar ? sessionScope.acc.avatar : "https://cdn-icons-png.flaticon.com/512/149/149071.png"}';
        const rating = parseInt(ratingValue) || 0;

        let starsHtml = '';
        if (rating > 0) {
            for(let i=1; i<=5; i++) starsHtml += `<i class="fa-${i <= rating ? 'solid' : 'regular'} fa-star"></i> `;
        } else {
            starsHtml = `<span style="color: #999; font-style: italic;">Chưa đánh giá sao</span>`;
        }

        let imgHtml = '';
        if (imgUrl && imgUrl.trim() !== '') {
            imgHtml = `<img src="` + imgUrl.trim() + `" style="max-width: 120px; border-radius: 4px; margin-top: 10px; border: 1px solid #ddd; cursor: zoom-in;" onclick="window.open(this.src, '_blank')">`;
        }

        const newReviewHtml = `
            <div class="review-item position-relative" style="border-bottom: 1px solid #eee; padding: 20px 0; display: flex; gap: 15px; background: #fffdf5; animation: fadeIn 1s;">
                <img src="` + avatar + `" class="review-avatar" style="width: 45px; height: 45px; border-radius: 50%; object-fit: cover;">
                <div style="flex: 1;">
                    <div class="review-user" style="font-weight: bold; color: #333;">` + username + ` <span style="color:red; font-size:12px;">(Mới)</span></div>
                    <div class="review-stars" style="color: #ffc107; font-size: 13px;">` + starsHtml + `</div>
                    <div style="font-size: 14.5px; color: #333; line-height: 1.6; margin-top: 10px;">` + comment + `</div>
                    ` + imgHtml + `
                </div>
            </div>`;

        if (reviewContainer) reviewContainer.insertAdjacentHTML('afterbegin', newReviewHtml);
    }

    let currentOffset = 10;
    function loadMoreReviews(productId) {
        const btn = document.getElementById('btnLoadMore');
        btn.innerHTML = '<i class="fa-solid fa-spinner fa-spin"></i> Đang tải...';

        fetch(`load-more-reviews?pid=${productId}&offset=${currentOffset}`)
            .then(response => response.text())
            .then(html => {
                if (html.trim() !== "") {
                    document.getElementById('reviewContainer').insertAdjacentHTML('beforeend', html);
                    currentOffset += 10;
                    let total = ${totalReviews};
                    let remaining = total - currentOffset;
                    if (remaining <= 0) {
                        document.getElementById('loadMoreSection').style.display = 'none';
                    } else {
                        document.getElementById('remainingCount').innerText = remaining;
                        btn.innerText = 'Xem thêm đánh giá';
                    }
                }
            });
    }

    function likeReview(reviewId, btnElement) {
        fetch('${pageContext.request.contextPath}/like-review', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'rid=' + reviewId
        })
            .then(res => res.text())
            .then(newLikes => {
                btnElement.innerHTML = `<i class="fa-solid fa-thumbs-up" style="color:#007bff;"></i> Hữu ích (` + newLikes.trim() + `)`;
                btnElement.style.color = '#007bff';
                btnElement.disabled = true;
            });
    }

    function likeReply(replyId, btnElement) {
        fetch('${pageContext.request.contextPath}/like-reply', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'replyId=' + replyId
        })
            .then(res => res.text())
            .then(newLikes => {
                btnElement.innerHTML = `<i class="fa-solid fa-thumbs-up" style="color:#007bff;"></i> Hữu ích (` + newLikes.trim() + `)`;
                btnElement.style.color = '#007bff';
                btnElement.disabled = true;
            });
    }

    function toggleReplyForm(reviewId) {
        const form = document.getElementById('replyFormBox_' + reviewId);
        form.style.display = form.style.display === 'none' ? 'flex' : 'none';
    }

    function replyToUser(reviewId, targetUsername) {
        const formBox = document.getElementById('replyFormBox_' + reviewId);
        formBox.style.display = 'flex';

        const input = document.getElementById('replyInput_' + reviewId);
        input.value = '@' + targetUsername + ' ';
        input.focus();
    }

    function submitReply(reviewId) {
        const input = document.getElementById('replyInput_' + reviewId);
        const text = input.value.trim();
        if(!text) return alert("Vui lòng nhập nội dung trả lời.");

        fetch('${pageContext.request.contextPath}/reply-review', {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: 'rid=' + reviewId + '&text=' + encodeURIComponent(text)
        })
            .then(res => res.text())
            .then(data => {
                const result = data.trim();
                if(result.startsWith("success")) {
                    const parts = result.split('|');
                    const username = parts[1];
                    const avatar = parts[2];
                    const role = parts[3];

                    const roleBadge = (role === 'Admin') ? `<span style="background:#d0011b; color:#fff; font-size:10px; padding:2px 5px; border-radius:3px; margin-left:5px;">Quản trị viên</span>` : '';

                    let formattedText = text.replace(/(@\S+)/g, '<b style="color:#1b6e76;">$1</b>');

                    const replyHtml = `
                    <div style="display:flex; gap:10px; margin-top:15px; background:#f1f1f1; padding:10px; border-radius:5px; animation: fadeIn 0.5s;">
                        <img src="`+avatar+`" style="width:30px; height:30px; border-radius:50%; object-fit:cover;">
                        <div style="flex: 1;">
                            <div style="font-weight:bold; font-size:13px; color:#333;">`+username+roleBadge+`</div>
                            <div style="font-size:13.5px; color:#555; margin-top:3px;">`+formattedText+`</div>
                        </div>
                    </div>`;

                    document.getElementById('repliesContainer_' + reviewId).insertAdjacentHTML('beforeend', replyHtml);
                    input.value = '';
                    toggleReplyForm(reviewId);
                } else {
                    alert(result.split('|')[1]);
                }
            });
    }

    let currentFilterStar = 0;
    let currentFilterImage = false;

    function applyFilter(star, hasImage, btnElement) {
        document.querySelectorAll('.filter-btn').forEach(btn => btn.classList.remove('active'));
        btnElement.classList.add('active');
        currentFilterStar = star;
        currentFilterImage = hasImage;
        currentOffset = 0;

        document.getElementById('reviewContainer').innerHTML = '<div style="text-align:center; padding: 30px;"><i class="fa-solid fa-spinner fa-spin fa-2x text-muted"></i></div>';

        fetch(`load-more-reviews?pid=${p.id}&offset=0&star=`+star+`&hasImage=`+hasImage)
            .then(response => response.text())
            .then(html => {
                if (html.trim() !== "") {
                    document.getElementById('reviewContainer').innerHTML = html;
                    currentOffset = 10;
                } else {
                    document.getElementById('reviewContainer').innerHTML = '<div style="text-align: center; padding: 40px 0; color: #999;"><p>Không có đánh giá nào phù hợp với bộ lọc.</p></div>';
                }
            });
    }
</script>

<jsp:include page="WEB-INF/tags/footer.jsp"></jsp:include>
</body>
</html>