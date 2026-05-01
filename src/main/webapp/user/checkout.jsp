<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh Toán | VVP Store</title>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/ThanhToan.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />

<form action="checkout" method="POST">

    <div class="checkout-left-col">
        <div class="checkout-section">
            <h3><i class="fa-solid fa-location-dot"></i> Địa chỉ nhận hàng</h3>

            <c:if test="${empty listAddress}">
                <div style="text-align: center; padding: 20px; border: 1px dashed #d0011b; background: #fff5f5; border-radius: 5px;">
                    <p style="color: #d0011b; margin-bottom: 15px;">Bạn chưa có địa chỉ nhận hàng nào!</p>
                    <button type="button" onclick="document.getElementById('modalAddAddr').style.display='flex'"
                            style="background: #d0011b; color: white; border: none; padding: 10px 20px; border-radius: 4px; cursor: pointer; font-weight: bold;">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ mới
                    </button>
                </div>
            </c:if>

            <c:if test="${not empty listAddress}">

                <div class="address-list" style="margin-bottom: 15px;">
                    <c:forEach items="${listAddress}" var="addr" varStatus="status">
                        <div class="address-item"
                             style="border: 1px solid #ddd; padding: 10px; margin-bottom: 10px; border-radius: 5px; display: flex; align-items: center; gap: 10px; cursor: pointer;"
                             onclick="document.getElementById('addr_${addr.id}').checked = true;">

                            <input type="radio"
                                   name="addressId"
                                   id="addr_${addr.id}"
                                   value="${addr.id}"
                                ${addr.defaultAddress || status.first ? 'checked' : ''}
                                   style="cursor: pointer;">

                            <label for="addr_${addr.id}" style="cursor: pointer; width: 100%;">
                                <div style="font-weight: bold;">
                                        ${addr.name} (${addr.phone})
                                    <c:if test="${addr.defaultAddress}">
                                        <span style="color: #d0011b; font-size: 11px; border: 1px solid #d0011b; padding: 1px 4px; border-radius: 3px; margin-left: 5px;">Mặc định</span>
                                    </c:if>
                                </div>
                                <div style="color: #666; font-size: 13px;">
                                        ${addr.address} <c:if test="${not empty addr.city}">, ${addr.city}</c:if>
                                </div>
                            </label>
                        </div>
                    </c:forEach>
                </div>
                <div style="text-align: right;">
                    <a href="javascript:void(0)" onclick="document.getElementById('modalAddAddr').style.display='flex'" style="color: #007bff; font-size: 13px; text-decoration: none;">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ khác
                    </a>
                </div>
            </c:if>
        </div>

        <div class="payment-section">
            <h3><i class="fa-regular fa-credit-card"></i> Phương thức thanh toán</h3>
            <div>
                <input type="radio" name="paymentMethod" value="COD" id="cod" checked>
                <label for="cod">
                    <i class="fa-solid fa-truck-fast" style="color: #1b6e76; margin-right: 5px;"></i>
                    Thanh toán khi nhận hàng (COD)
                </label>
            </div>
            <div>
                <input type="radio" name="paymentMethod" value="Banking" id="banking">
                <label for="banking">
                    <i class="fa-solid fa-building-columns" style="color: #1b6e76; margin-right: 5px;"></i>
                    Chuyển khoản ngân hàng (QR Code)
                </label>
            </div>
        </div>
    </div>

    <div class="checkout-right-col">
        <div class="checkout-summary">
            <h3>Đơn hàng (${sessionScope.cart.size()} sản phẩm)</h3>

            <div class="mini-product-list">
                <c:forEach items="${sessionScope.cart}" var="item">
                    <div class="mini-item">
                        <div style="display: flex; justify-content: space-between; width: 100%;">
                            <span>
                                <b>${item.quantity}x</b> ${item.product.name}
                            </span>
                            <span style="font-weight: bold; color: #333;">
                                <fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="₫"/>
                            </span>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <p>Tạm tính: <span><fmt:formatNumber value="${totalMoney}" type="currency" currencySymbol="₫"/></span></p>

            <c:if test="${discount > 0}">
                <p>Giảm giá: <span style="color: green;">- <fmt:formatNumber value="${discount}" type="currency" currencySymbol="₫"/></span></p>
            </c:if>

            <p>Phí vận chuyển: <span style="color: green;">Miễn phí</span></p>

            <p style="border-top: 1px dashed #ccc; padding-top: 10px; margin-top: 10px; font-size: 18px; font-weight: bold; color: #d0011b;">
                Tổng cộng: <span><fmt:formatNumber value="${finalTotal}" type="currency" currencySymbol="₫"/></span>
            </p>

            <c:choose>
                <c:when test="${not empty listAddress}">
                    <button type="submit" class="btn-checkout">ĐẶT HÀNG NGAY</button>
                </c:when>

                <c:otherwise>
                    <button type="button" onclick="document.getElementById('modalAddAddr').style.display='flex'"
                            class="btn-checkout" style="background: #999; cursor: pointer;">
                        VUI LÒNG THÊM ĐỊA CHỈ
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>

</form>

<jsp:include page="../WEB-INF/tags/footer.jsp" />
<div id="modalAddAddr" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); justify-content: center; align-items: center; z-index: 1000;">
    <div style="background: white; padding: 25px; border-radius: 8px; width: 400px; box-shadow: 0 5px 15px rgba(0,0,0,0.3);">
        <h3 style="margin-top: 0; color: #333;">Thêm địa chỉ giao hàng</h3>

        <form action="checkout-address" method="POST">
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px;">Họ tên người nhận:</label>
                <input type="text" name="new_name" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px;">Số điện thoại:</label>
                <input type="text" name="new_phone" required style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;">
            </div>
            <div style="margin-bottom: 15px;">
                <label style="display: block; margin-bottom: 5px;">Địa chỉ chi tiết (Số nhà, Phường/Xã...):</label>
                <textarea name="new_address" required rows="3" style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px;"></textarea>
            </div>

            <div style="text-align: right; margin-top: 20px;">
                <button type="button" onclick="document.getElementById('modalAddAddr').style.display='none'"
                        style="background: #ccc; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; margin-right: 10px;">Hủy</button>
                <button type="submit"
                        style="background: #28a745; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer;">Lưu địa chỉ</button>
            </div>
        </form>
    </div>
</div>
</body>
</html>