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

<div class="container" style="max-width: 1200px; margin: 0 auto; padding-top: 150px;">
    <%-- Hiển thị thông báo lỗi từ Servlet nếu có --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger" style="background: #fff5f5; border: 1px solid #ffcccc; color: #d0011b; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
            <i class="fa-solid fa-triangle-exclamation"></i> ${error}
        </div>
    </c:if>

    <form action="checkout" method="POST" class="checkout-form" style="display: flex; gap: 30px; align-items: flex-start;">

        <div class="checkout-left-col" style="flex: 2;">

            <div class="checkout-section">
                <h3><i class="fa-solid fa-location-dot"></i> Địa chỉ nhận hàng</h3>

                <c:choose>
                    <c:when test="${empty listAddress}">
                        <div class="address-empty-alert" style="text-align: center; border: 2px dashed #ddd; padding: 30px; border-radius: 12px;">
                            <p style="color: #666; margin-bottom: 15px;">Bạn chưa có địa chỉ nhận hàng nào trong danh sách.</p>
                            <button type="button" onclick="openModal()" class="btn-add-first" style="background: #d0011b; color: white; border: none; padding: 10px 25px; border-radius: 6px; cursor: pointer; font-weight: bold;">
                                + THÊM ĐỊA CHỈ MỚI
                            </button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="address-list" style="display: grid; grid-template-columns: 1fr 1fr; gap: 15px;">
                            <c:forEach items="${listAddress}" var="addr" varStatus="status">
                                <div class="address-item ${addr.defaultAddress ? 'active' : ''}"
                                     style="border: 1px solid #eee; padding: 15px; border-radius: 10px; cursor: pointer; position: relative; transition: 0.3s;"
                                     onclick="selectAddress('addr_${addr.id}')">

                                    <input type="radio" name="addressId" id="addr_${addr.id}" value="${addr.id}"
                                        ${addr.defaultAddress || status.first ? 'checked' : ''}
                                           style="position: absolute; right: 15px; top: 15px; accent-color: #d0011b;">

                                    <label for="addr_${addr.id}" style="cursor: pointer; display: block;">
                                        <div style="font-weight: bold; font-size: 16px; margin-bottom: 5px;">
                                                ${addr.name} | ${addr.phone}
                                            <c:if test="${addr.defaultAddress}">
                                                <span class="badge-default" style="background: #d0011b; color: white; font-size: 10px; padding: 2px 6px; border-radius: 4px; margin-left: 5px;">MẶC ĐỊNH</span>
                                            </c:if>
                                        </div>
                                        <div style="color: #777; font-size: 13px; line-height: 1.5;">
                                                ${addr.address}<br>
                                                ${addr.city}
                                        </div>
                                    </label>
                                </div>
                            </c:forEach>
                        </div>
                        <div style="margin-top: 15px; text-align: right;">
                            <a href="javascript:void(0)" onclick="openModal()" style="color: #007bff; text-decoration: none; font-size: 14px; font-weight: bold;">
                                <i class="fa-solid fa-circle-plus"></i> Thêm địa chỉ nhận hàng khác
                            </a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>

            <div class="payment-section" style="margin-top: 30px;">
                <h3><i class="fa-regular fa-credit-card"></i> Phương thức thanh toán</h3>

                <div class="payment-option" onclick="selectPayment('cod')" style="display: flex; align-items: center; padding: 15px; border: 1px solid #eee; border-radius: 10px; margin-bottom: 12px; cursor: pointer;">
                    <input type="radio" name="paymentMethod" value="COD" id="cod" checked style="margin-right: 15px; accent-color: #d0011b;">
                    <label for="cod" style="display: flex; align-items: center; cursor: pointer; width: 100%;">
                        <img src="https://cdn-icons-png.flaticon.com/512/6491/6491490.png" style="width: 45px; margin-right: 15px;" alt="COD">
                        <div>
                            <div style="font-weight: bold;">Thanh toán khi nhận hàng (COD)</div>
                            <small style="color: #999;">Nhận hàng rồi mới trả tiền mặt</small>
                        </div>
                    </label>
                </div>

                <div class="payment-option" onclick="selectPayment('vnpay')" style="display: flex; align-items: center; padding: 15px; border: 1px solid #eee; border-radius: 10px; margin-bottom: 12px; cursor: pointer;">
                    <input type="radio" name="paymentMethod" value="VNPAY" id="vnpay" style="margin-right: 15px; accent-color: #d0011b;">
                    <label for="vnpay" style="display: flex; align-items: center; cursor: pointer; width: 100%;">
                        <img src="https://sandbox.vnpayment.vn/paymentv2/Images/cart/vnpay_logo.png" style="width: 70px; margin-right: 15px;" alt="VNPAY">
                        <div>
                            <div style="font-weight: bold;">Cổng thanh toán VNPAY</div>
                            <small style="color: #999;">ATM, QR Code, Visa, Master Card...</small>
                        </div>
                    </label>
                </div>

                <div class="payment-option disabled" style="display: flex; align-items: center; padding: 15px; border: 1px solid #f5f5f5; border-radius: 10px; opacity: 0.5; background: #fafafa;">
                    <input type="radio" name="paymentMethod" value="MOMO" id="momo" disabled style="margin-right: 15px;">
                    <label for="momo" style="display: flex; align-items: center; width: 100%;">
                        <img src="https://upload.wikimedia.org/wikipedia/vi/f/fe/MoMo_Logo.png" style="width: 40px; margin-right: 15px;" alt="MoMo">
                        <div>
<%--                            Giả lập MoMo--%>
                            <div style="font-weight: bold;">Ví MoMo (Đang bảo trì)</div>
                        </div>
                    </label>
                </div>
            </div>
        </div>

        <div class="checkout-right-col" style="flex: 1; position: sticky; top: 150px;">
            <div class="checkout-summary" style="background: white; padding: 25px; border-radius: 12px; box-shadow: 0 4px 20px rgba(0,0,0,0.08); border-top: 4px solid #d0011b;">
                <h3 style="border: none; padding: 0; margin-bottom: 20px;">Tóm tắt đơn hàng</h3>

                <div class="mini-product-list" style="max-height: 300px; overflow-y: auto; margin-bottom: 20px; border-bottom: 1px solid #f0f0f0;">
                    <c:forEach items="${sessionScope.cart}" var="item">
                        <div class="mini-item" style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 14px;">
                            <span style="flex: 1; color: #555;">
                                <strong>${item.quantity}x</strong> ${item.product.name}
                            </span>
                            <span style="font-weight: bold;">
                                <fmt:formatNumber value="${item.totalPrice}" type="currency" currencySymbol="₫"/>
                            </span>
                        </div>
                    </c:forEach>
                </div>

                <div class="summary-details" style="font-size: 15px;">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                        <span>Tạm tính</span>
                        <span><fmt:formatNumber value="${totalMoney}" type="currency" currencySymbol="₫"/></span>
                    </div>
                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                        <span>Phí vận chuyển</span>
                        <span style="color: #28a745; font-weight: bold;">Miễn phí</span>
                    </div>
                    <div class="total-row" style="display: flex; justify-content: space-between; margin-top: 20px; padding-top: 15px; border-top: 2px dashed #eee; font-weight: bold;">
                        <span style="font-size: 18px;">Tổng cộng</span>
                        <span style="font-size: 22px; color: #d0011b;">
                            <fmt:formatNumber value="${totalMoney}" type="currency" currencySymbol="₫"/>
                        </span>
                    </div>
                </div>

                <input type="hidden" name="totalAmount" value="${totalMoney}">

                <c:choose>
                    <c:when test="${not empty listAddress}">
                        <button type="submit" class="btn-checkout" style="width: 100%; background: #1a1a1a; color: #c5a059; border: 1px solid #c5a059; padding: 15px; border-radius: 8px; font-weight: bold; font-size: 16px; margin-top: 25px; cursor: pointer; text-transform: uppercase; transition: 0.3s;">
                            XÁC NHẬN ĐẶT HÀNG
                        </button>
                    </c:when>
                    <c:otherwise>
                        <button type="button" onclick="openModal()" class="btn-checkout" style="width: 100%; background: #999; color: white; border: none; padding: 15px; border-radius: 8px; font-weight: bold; margin-top: 25px; cursor: pointer;">
                            VUI LÒNG THÊM ĐỊA CHỈ
                        </button>
                    </c:otherwise>
                </c:choose>
                <div style="text-align: center; margin-top: 15px;">
                    <img src="https://sandbox.vnpayment.vn/paymentv2/Images/cart/vnpay_logo.png" style="height: 20px; opacity: 0.5;" alt="Secure Payment">
                    <p style="font-size: 11px; color: #999; margin-top: 5px;">Thông tin của bạn được bảo mật tuyệt đối</p>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />

<div id="modalAddAddr" class="modal-overlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); backdrop-filter: blur(4px); justify-content: center; align-items: center; z-index: 10000;">
    <div class="modal-box" style="background: white; padding: 35px; border-radius: 15px; width: 500px; box-shadow: 0 20px 50px rgba(0,0,0,0.2);">
        <h3 style="margin-top: 0; border-bottom: 1px solid #eee; padding-bottom: 15px; color: #333;">Thêm địa chỉ giao hàng mới</h3>

        <form action="checkout-address" method="POST">
            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; font-weight: 600; margin-bottom: 5px;">Họ tên người nhận:</label>
                <input type="text" name="new_name" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; font-weight: 600; margin-bottom: 5px;">Số điện thoại:</label>
                <input type="text" name="new_phone" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div class="form-row" style="display: flex; gap: 15px; margin-bottom: 15px;">
                <div class="form-col" style="flex: 1;">
                    <label style="display: block; font-weight: 600; margin-bottom: 5px;">Tỉnh/Thành phố:</label>
                    <input type="text" name="provinceName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
                </div>
                <div class="form-col" style="flex: 1;">
                    <label style="display: block; font-weight: 600; margin-bottom: 5px;">Quận/Huyện:</label>
                    <input type="text" name="districtName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
                </div>
            </div>

            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; font-weight: 600; margin-bottom: 5px;">Phường/Xã:</label>
                <input type="text" name="wardName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div class="form-group" style="margin-bottom: 20px;">
                <label style="display: block; font-weight: 600; margin-bottom: 5px;">Số nhà, tên đường:</label>
                <input type="text" name="streetDetail" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px;">
            </div>

            <div class="modal-footer" style="text-align: right; border-top: 1px solid #eee; padding-top: 20px;">
                <button type="button" onclick="closeModal()" style="padding: 10px 25px; border-radius: 6px; border: 1px solid #ccc; background: #f8f8f8; cursor: pointer; margin-right: 10px; font-weight: bold;">HỦY</button>
                <button type="submit" style="padding: 10px 25px; border-radius: 6px; border: none; background: #c5a059; color: white; cursor: pointer; font-weight: bold; box-shadow: 0 4px 10px rgba(197, 160, 89, 0.3);">LƯU ĐỊA CHỈ</button>
            </div>
        </form>
    </div>
</div>

<script>
    function openModal() {
        document.getElementById('modalAddAddr').style.display = 'flex';
    }
    function closeModal() {
        document.getElementById('modalAddAddr').style.display = 'none';
    }
    function selectAddress(id) {
        document.getElementById(id).checked = true;
    }
    function selectPayment(id) {
        document.getElementById(id).checked = true;
    }
</script>

</body>
</html>