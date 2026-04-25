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
                <div class="address-empty-alert">
                    <p>Bạn chưa có địa chỉ nhận hàng nào!</p>
                    <button type="button" onclick="document.getElementById('modalAddAddr').style.display='flex'" class="btn-add-first">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ mới
                    </button>
                </div>
            </c:if>

            <c:if test="${not empty listAddress}">
                <div class="address-list">
                    <c:forEach items="${listAddress}" var="addr" varStatus="status">
                        <div class="address-item ${addr.defaultAddress ? 'active' : ''}"
                             onclick="document.getElementById('addr_${addr.id}').checked = true;">
                            <input type="radio" name="addressId" id="addr_${addr.id}" value="${addr.id}"
                                ${addr.defaultAddress || status.first ? 'checked' : ''}>
                            <label for="addr_${addr.id}">
                                <strong>${addr.name} (${addr.phone})</strong>
                                <c:if test="${addr.defaultAddress}">
                                    <span class="badge-default">Mặc định</span>
                                </c:if>
                                <p>${addr.address} ${not empty addr.city ? ', '.concat(addr.city) : ''}</p>
                            </label>
                        </div>
                    </c:forEach>
                </div>
                <div class="add-more-address">
                    <a href="javascript:void(0)" onclick="document.getElementById('modalAddAddr').style.display='flex'">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ khác
                    </a>
                </div>
            </c:if>
        </div>

        <div class="payment-section" style="margin-top: 30px;">
            <h3><i class="fa-regular fa-credit-card"></i> Phương thức thanh toán</h3>

            <div class="payment-option" onclick="document.getElementById('cod').checked = true;">
                <input type="radio" name="paymentMethod" value="COD" id="cod" checked>
                <label for="cod" class="payment-label">
                    <img src="https://cdn-icons-png.flaticon.com/512/6491/6491490.png" class="payment-logo" alt="COD">
                    <div class="payment-text">
                        <div class="p-title">Thanh toán khi nhận hàng (COD)</div>
                        <div class="p-desc">Thanh toán tiền mặt khi shipper giao hàng</div>
                    </div>
                </label>
            </div>

            <div class="payment-option" onclick="document.getElementById('vnpay').checked = true;">
                <input type="radio" name="paymentMethod" value="VNPAY" id="vnpay">
                <label for="vnpay" class="payment-label">
                    <img src="https://sandbox.vnpayment.vn/paymentv2/Images/cart/vnpay_logo.png" class="payment-logo" alt="VNPAY">
                    <div class="payment-text">
                        <div class="p-title">Cổng thanh toán VNPAY</div>
                        <div class="p-desc">Thẻ ATM, QR Code, Visa/Master Card</div>
                    </div>
                </label>
            </div>

            <div class="payment-option disabled">
                <input type="radio" name="paymentMethod" value="MOMO" id="momo" disabled>
                <label for="momo" class="payment-label">
                    <img src="https://upload.wikimedia.org/wikipedia/vi/f/fe/MoMo_Logo.png" class="payment-logo" alt="MoMo">
                    <div class="payment-text">
                        <div class="p-title">Ví MoMo (Đang bảo trì)</div>
                    </div>
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
                        <span class="p-name"><b>${item.quantity}x</b> ${item.product.name}</span>
                        <span class="p-price"><fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="₫"/></span>
                    </div>
                </c:forEach>
            </div>

            <div class="summary-details">
                <p>Tạm tính: <span><fmt:formatNumber value="${totalMoney}" type="currency" currencySymbol="₫"/></span></p>
                <p>Phí vận chuyển: <span class="free-shipping">Miễn phí</span></p>
                <div class="total-row">
                    <span>Tổng cộng:</span>
                    <span class="total-price"><fmt:formatNumber value="${totalMoney}" type="currency" currencySymbol="₫"/></span>
                </div>
            </div>

            <input type="hidden" name="totalAmount" value="${totalMoney}">

            <c:choose>
                <c:when test="${not empty listAddress}">
                    <button type="submit" class="btn-checkout">XÁC NHẬN ĐẶT HÀNG</button>
                </c:when>
                <c:otherwise>
                    <button type="button" onclick="document.getElementById('modalAddAddr').style.display='flex'" class="btn-checkout btn-disabled">
                        VUI LÒNG THÊM ĐỊA CHỈ
                    </button>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</form>

<jsp:include page="../WEB-INF/tags/footer.jsp" />

<div id="modalAddAddr" class="modal-overlay">
    <div class="modal-box">
        <h3>Thêm địa chỉ giao hàng</h3>

        <form action="checkout-address" method="POST">
            <div class="form-group">
                <label>Họ tên người nhận:</label>
                <input type="text" name="new_name" required>
            </div>

            <div class="form-group">
                <label>Số điện thoại:</label>
                <input type="text" name="new_phone" required>
            </div>

            <div class="form-row">
                <div class="form-col">
                    <label>Tỉnh/Thành phố:</label>
                    <input type="text" name="provinceName" required>
                </div>
                <div class="form-col">
                    <label>Quận/Huyện:</label>
                    <input type="text" name="districtName" required>
                </div>
            </div>

            <div class="form-group">
                <label>Phường/Xã:</label>
                <input type="text" name="wardName" required>
            </div>

            <div class="form-group">
                <label>Số nhà, tên đường:</label>
                <input type="text" name="streetDetail" required>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn-base btn-cancel"
                        onclick="document.getElementById('modalAddAddr').style.display='none'">
                    Hủy
                </button>
                <button type="submit" class="btn-base btn-submit">
                    LƯU ĐỊA CHỈ
                </button>
            </div>
        </form>
    </div>
</div>
</body>
</html>