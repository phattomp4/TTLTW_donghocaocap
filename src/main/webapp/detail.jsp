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
        .Main-image-container img { max-width: 100%; height: auto; }
        .Body { display: flex; gap: 30px; margin: 20px auto; max-width: 1200px; }
        @media (max-width: 768px) { .Body { flex-direction: column; } }
    </style>
</head>
<body>

<jsp:include page="WEB-INF/tags/header.jsp"></jsp:include>
<c:set var="pName" value="${fn:toLowerCase(p.name)}" />
<c:set var="isAccessory" value="${fn:contains(pName, 'dây') or fn:contains(pName, 'hộp') or fn:contains(pName, 'khóa') or fn:contains(pName, 'phụ kiện')}" />
<div class="product-detail-container" style="margin-top: 5px; display: flex; gap: 30px; padding: 0 10%;">

    <div class="product-images" style="width: 50%;">
        <div class="main-image" style="margin-bottom: 10px;">
            <img src="${p.imageList.size() > 0 ? p.imageList[0] : p.imageUrl}"
                 id="mainImg" alt="${p.name}" style="width: 100%; border-radius: 8px;">
        </div>

        <div class="thumbnail-list" style="display: flex; gap: 10px; overflow-x: auto;">
            <c:forEach items="${p.imageList}" var="img">
                <img src="${img}"
                     onclick="document.getElementById('mainImg').src='${img}'"
                     style="width: 80px; height: 80px; object-fit: cover; cursor: pointer; border: 1px solid #ddd; border-radius: 4px;">
            </c:forEach>
        </div>
    </div>

    <div class="product-info" style="width: 50%;">
        <h1 style="font-size: 24px; margin-bottom: 10px;">${p.name}</h1>
        <p style="color: #666;">Mã SP: <strong>${p.sku}</strong> | Tình trạng: <strong>${p.stockQuantity > 0 ? 'Còn hàng' : 'Hết hàng'}</strong></p>

        <div class="price-box" style="margin: 20px 0;">
                <span style="color: #d0011b; font-size: 28px; font-weight: bold;">
                    <fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫"/>
                </span>
            <span style="text-decoration: line-through; color: #888; margin-left: 15px;">
                    <fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫"/>
                </span>
        </div>

        <div class="specs-table" style="margin-top: 40px;">
            <h3 style="border-bottom: 2px solid #ddd; padding-bottom: 10px;">THÔNG SỐ KỸ THUẬT</h3>
            <table style="width: 100%; border-collapse: collapse; margin-top: 15px;">

                <c:if test="${not empty p.specifications['Thương hiệu']}">
                <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Thương hiệu</td><td>${p.specifications['Thương hiệu']}</td></tr>
                </c:if>

                <c:if test="${not empty p.specifications['Thương hiệu']}">
                <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Xuất xứ</td><td>${p.specifications['Xuất xứ']}</td></tr>
                </c:if>

                <c:if test="${not empty p.specifications['Đối tượng']}">
                    <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Đối tượng</td><td>${p.specifications['Đối tượng']}</td></tr>
                </c:if>

                <c:if test="${not empty p.specifications['Chống nước']}">
                    <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Chống nước</td><td>${p.specifications['Chống nước']}</td></tr>
                </c:if>

                <c:if test="${not empty p.specifications['Loại máy']}">
                    <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Loại máy</td><td>${p.specifications['Loại máy']}</td></tr>
                </c:if>

                <c:if test="${not empty p.specifications['Chất liệu kính']}">
                    <tr style="border-bottom: 1px solid #eee;"><td style="padding: 10px; font-weight: bold;">Chất liệu kính</td><td>${p.specifications['Chất liệu kính']}</td></tr>
                </c:if>

            </table>
        </div>


        <div class="actions">
            <form id="productForm" action="${pageContext.request.contextPath}/add-to-cart" method="GET">
                <input type="hidden" name="pid" value="${p.id}">

                <div class="quantity-box">
                    <label>Số lượng:</label>
                    <input type="number" id="qtyInput" name="quantity" value="1" min="1"
                           style="width: 50px; text-align: center;"
                    ${p.stockQuantity <= 0 ? 'disabled' : ''}>
                </div>

                <c:choose>
                    <c:when test="${p.stockQuantity > 0}">
                        <div class="action-buttons" style="margin-top: 20px; display: flex; gap: 10px;">

                            <c:choose>
                                <c:when test="${sessionScope.acc != null && sessionScope.acc.role == 'Admin'}">
                                    <a href="admin/product-form?id=${p.id}" class="btn-buy-now"
                                       style="background: #343a40; color: white; border: none; padding: 10px 30px; cursor: pointer; text-decoration: none; display: flex; align-items: center; justify-content: center; gap: 10px; width: 100%;">
                                        <i class="fa-solid fa-screwdriver-wrench"></i> QUẢN LÝ / SỬA SẢN PHẨM NÀY
                                    </a>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" onclick="addToCartAjax()" class="btn-add-cart"
                                            style="background: #fff; border: 1px solid #d0011b; color: #d0011b; padding: 10px 20px; cursor: pointer;">
                                        <i class="fa-solid fa-cart-plus"></i> Thêm vào giỏ
                                    </button>

                                    <button type="submit" name="action" value="buynow" class="btn-buy-now"
                                            style="background: #d0011b; color: white; border: none; padding: 10px 30px; cursor: pointer;">
                                        Mua ngay
                                    </button>
                                </c:otherwise>
                            </c:choose>

                        </div>
                        <p style="color: green; margin-top: 10px; font-size: 14px;">
                            <i class="fa-solid fa-check-circle"></i> Còn lại ${p.stockQuantity} sản phẩm
                        </p>
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
    <p class="desc" >${p.description}</p></div>
<script>
    function addToCartAjax() {
        const pidInput = document.querySelector('input[name="pid"]');
        const pid = pidInput ? pidInput.value : "";

        if (!pid) {
            alert("Lỗi: Không tìm thấy ID sản phẩm. Vui lòng tải lại trang.");
            return;
        }
        const qtyInput = document.getElementById('qtyInput');
        const qty = qtyInput ? qtyInput.value : 1;

        fetch('${pageContext.request.contextPath}/add-to-cart?pid=' + pid + '&quantity=' + qty + '&ajax=true')
            .then(response => {
                if (!response.ok) {
                    throw new Error("Lỗi Server hoặc Đường dẫn sai");
                }
                return response.text();
            })
            .then(data => {
                const listCartCounts = document.querySelectorAll(".cart-count");
                listCartCounts.forEach(el => {
                    el.innerText = data;
                });

                const x = document.getElementById("toast");
                if (x) {
                    x.style.visibility = "visible";
                    setTimeout(function(){ x.style.visibility = "hidden"; }, 3000);
                }
            })
            .catch(error => {
                console.error('Lỗi:', error);
                alert("Không thể thêm vào giỏ. Vui lòng kiểm tra Console (F12) để biết chi tiết.");
            });
    }
</script>
<jsp:include page="WEB-INF/tags/footer.jsp"></jsp:include>
</body>
</html>