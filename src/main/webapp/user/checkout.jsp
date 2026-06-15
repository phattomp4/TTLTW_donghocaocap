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
<jsp:include page="../WEB-INF/tags/header.jsp"/>

<div class="container" style="max-width: 1200px; margin: 0 auto; padding-top: 150px;">

    <c:if test="${not empty error}">
        <div class="alert alert-danger" style="background: #fff5f5; border: 1px solid #ffcccc; color: #d0011b; padding: 15px; border-radius: 8px; margin-bottom: 20px;">
            <i class="fa-solid fa-triangle-exclamation"></i> ${error}
        </div>
    </c:if>

    <form id="checkoutForm" action="checkout" method="POST" class="checkout-form" style="display: flex; gap: 30px; align-items: flex-start;">

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
                                     onclick="selectAddress('addr_${addr.id}')"
                                     style="border: 1px solid #eee; padding: 15px; border-radius: 10px; cursor: pointer; position: relative; transition: 0.3s;">

                                    <input type="radio" name="addressId" id="addr_${addr.id}" value="${addr.id}"
                                           data-name="<c:out value='${addr.name}'/>"
                                           data-phone="${addr.phone}"
                                           data-street="<c:out value='${addr.streetDetail}'/>"
                                           data-prov="${addr.province}"
                                           data-dist="${addr.district}"
                                           data-ward="${addr.ward}"
                                           onchange="updateGHNInfo(this)"
                                        ${addr.defaultAddress || status.first ? 'checked' : ''}
                                           style="position: absolute; right: 15px; top: 15px; accent-color: #d0011b;">

                                    <label for="addr_${addr.id}" style="cursor: pointer; display: block;">
                                        <div style="font-weight: bold; font-size: 16px; margin-bottom: 5px;">
                                            <c:out value="${addr.name}"/> | ${addr.phone}
                                            <c:if test="${addr.defaultAddress}">
                                                <span class="badge-default" style="background: #d0011b; color: white; font-size: 10px; padding: 2px 6px; border-radius: 4px; margin-left: 5px;">MẶC ĐỊNH</span>
                                            </c:if>
                                        </div>
                                        <div style="color: #777; font-size: 13px; line-height: 1.5;">
                                            <c:out value="${addr.streetDetail}"/>, ${addr.ward}<br>${addr.district}, ${addr.province}
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
                        <img src="${pageContext.request.contextPath}/assets/img/banners/vnPay.png" style="width: 70px; margin-right: 15px;" alt="VNPAY">
                        <div>
                            <div style="font-weight: bold;">Cổng thanh toán VNPAY</div>
                            <small style="color: #999;">ATM, QR Code, Visa, Master Card...</small>
                        </div>
                    </label>
                </div>

                <div class="payment-option disabled" style="display: flex; align-items: center; padding: 15px; border: 1px solid #f5f5f5; border-radius: 10px; opacity: 0.5; background: #fafafa;">
                    <input type="radio" name="paymentMethod" value="MOMO" id="momo" disabled style="margin-right: 15px;">
                    <label for="momo" style="display: flex; align-items: center; width: 100%;">
                        <img src="${pageContext.request.contextPath}/assets/img/banners/momo.png" style="width: 40px; margin-right: 15px;" alt="MoMo">
                        <div>
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
                        <div class="mini-item ghn-item-data"
                             data-name="<c:out value='${item.product.name}'/>"
                             data-code="${item.product.id}"
                             data-qty="${item.quantity}"
                             data-price="${item.product.currentPrice}"
                             style="display: flex; justify-content: space-between; margin-bottom: 15px; font-size: 14px;">
                            <span style="flex: 1; color: #555;"><strong>${item.quantity}x</strong> <c:out value="${item.product.name}"/></span>
                            <span style="font-weight: bold;"><fmt:formatNumber value="${item.totalPrice}" pattern="#,##0 ₫"/></span>
                        </div>
                    </c:forEach>
                </div>

                <div class="voucher-section" style="margin-bottom: 20px; padding-bottom: 15px; border-bottom: 1px solid #f0f0f0;">
                    <label style="display: block; font-weight: bold; font-size: 14px; margin-bottom: 8px; color: #333;">
                        <i class="fa-solid fa-ticket" style="color: #d0011b;"></i> VVP Voucher
                    </label>
                    <div style="display: flex; gap: 8px;">
                        <input type="text" id="voucherCode" name="voucherCode" placeholder="Nhập mã giảm giá..." value="${sessionScope.appliedVoucher.code}" style="flex: 1; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; outline: none;">
                        <button type="button" onclick="applyVoucher()" style="background: #1a1a1a; color: #c5a059; border: 1px solid #c5a059; padding: 0 15px; border-radius: 6px; font-weight: bold; cursor: pointer; font-size: 13px; transition: 0.3s;">
                            ÁP DỤNG
                        </button>
                    </div>

                    <div id="voucherResponse" style="font-size: 13px; margin-top: 6px; display: ${not empty voucherError || not empty voucherSuccess ? 'block' : 'none'}; color: ${not empty voucherError ? '#d0011b' : 'green'};">
                        <c:if test="${not empty voucherError}">
                            <i class="fa-solid fa-circle-xmark"></i> ${voucherError}
                        </c:if>
                        <c:if test="${not empty voucherSuccess}">
                            <i class="fa-solid fa-circle-check"></i> ${voucherSuccess}
                        </c:if>
                    </div>
                </div>

                <div class="summary-details" style="font-size: 15px;">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                        <span>Tạm tính</span>
                        <span><fmt:formatNumber value="${totalMoney}" pattern="#,##0 ₫"/></span>
                    </div>
                    
                    <c:if test="${discount > 0}">
                        <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                            <span>Giảm giá</span>
                            <span style="color: green;">- <fmt:formatNumber value="${discount}" pattern="#,##0 ₫"/></span>
                        </div>
                    </c:if>
                    
                    <div style="display: flex; justify-content: space-between; margin-bottom: 10px;">
                        <span>Phí vận chuyển</span>
                        <span id="shipping-fee" style="color: #d0011b; font-weight: bold;">Đang tính...</span>
                    </div>

                    <div style="display: flex; justify-content: space-between; margin-bottom: 15px; padding-bottom: 15px; border-bottom: 1px dashed #ccc;">
                        <span>Dự kiến giao</span>
                        <span id="lead-time" style="color: #1b6e76; font-weight: bold;">Đang tính toán...</span>
                    </div>
                    
                    <div style="display: flex; justify-content: space-between; font-size: 18px; font-weight: bold; color: #d0011b;">
                        <span>Tổng cộng</span>
                        <span id="final-total" data-subtotal="<fmt:formatNumber value='${totalMoney - (not empty discount ? discount : 0)}' pattern='0'/>">
                            <fmt:formatNumber value="${finalTotal}" pattern="#,##0 ₫"/>
                        </span>
                    </div>
                </div>

                <c:set var="totalOrderWeight" value="0" />
                <c:forEach items="${sessionScope.cart}" var="item">
                    <c:set var="totalOrderWeight" value="${totalOrderWeight + (300 * item.quantity)}" />
                </c:forEach>

                <input type="hidden" name="shippingFee" id="input-shipping-fee" value="0">
                <input type="hidden" name="finalTotal" id="input-final-total" value="${finalTotal}">
                <input type="hidden" id="input-total-weight" value="${totalOrderWeight}">
                <input type="hidden" id="hidden-district-id" value="">
                <input type="hidden" id="hidden-ward-code" value="">

                <div style="margin-top: 25px;">
                    <c:choose>
                        <c:when test="${not empty listAddress}">
                            <button type="submit" id="btn-checkout" class="btn-checkout" style="width: 100%; background: #1a1a1a; color: #c5a059; padding: 15px; border-radius: 8px; font-weight: bold; border: 1px solid #c5a059; cursor: pointer;">ĐẶT HÀNG NGAY</button>
                        </c:when>
                        <c:otherwise>
                            <button type="button" onclick="openModal()" class="btn-checkout" style="width: 100%; background: #999; color: white; padding: 15px; border-radius: 8px; font-weight: bold; border: none; cursor: pointer;">VUI LÒNG THÊM ĐỊA CHỈ</button>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </form>
</div>

<jsp:include page="../WEB-INF/tags/footer.jsp"/>

<div id="modalAddAddr" class="modal-overlay" style="display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); backdrop-filter: blur(4px); justify-content: center; align-items: center; z-index: 10000;">
    <div class="modal-box" style="background: white; padding: 35px; border-radius: 15px; width: 500px; box-shadow: 0 200px 50px rgba(0,0,0,0.2);">
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
                    <select id="province" name="provinceName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; outline: none;">
                        <option value="">Chọn Tỉnh/Thành</option>
                    </select>
                </div>
                <div class="form-col" style="flex: 1;">
                    <label style="display: block; font-weight: 600; margin-bottom: 5px;">Quận/Huyện:</label>
                    <select id="district" name="districtName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; outline: none;">
                        <option value="">Chọn Quận/Huyện</option>
                    </select>
                </div>
            </div>
            <div class="form-group" style="margin-bottom: 15px;">
                <label style="display: block; font-weight: 600; margin-bottom: 5px;">Phường/Xã:</label>
                <select id="ward" name="wardName" required style="width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; outline: none;">
                    <option value="">Chọn Phường/Xã</option>
                </select>
            </div>

            <div class="modal-footer" style="text-align: right; border-top: 1px solid #eee; padding-top: 20px;">
                <button type="button" onclick="closeModal()" style="padding: 10px 25px; border-radius: 6px; border: 1px solid #ccc; background: #f8f8f8; cursor: pointer; margin-right: 10px; font-weight: bold;">HỦY</button>
                <button type="submit" style="padding: 10px 25px; border-radius: 6px; border: none; background: #c5a059; color: white; cursor: pointer; font-weight: bold; box-shadow: 0 4px 10px rgba(197, 160, 89, 0.3);">LƯU ĐỊA CHỈ</button>
            </div>
        </form>
    </div>
</div>

<script src="${pageContext.request.contextPath}/assets/js/index.js?v=3"></script>
<script src="${pageContext.request.contextPath}/assets/js/addressesProfile.js?v=3"></script>

<script>
    function openModal() { document.getElementById("modalAddAddr").style.display = "flex"; }
    function closeModal() { document.getElementById("modalAddAddr").style.display = "none"; }
    function selectAddress(id) {
        document.getElementById(id).checked = true;
        updateGHNInfo(document.getElementById(id));
    }

    function selectPayment(id) {
        document.getElementById(id).checked = true;
        const checkedAddress = document.querySelector('input[name="addressId"]:checked');
        if (checkedAddress) updateGHNInfo(checkedAddress);
    }

    // TÍCH HỢP HÀM APPLY VOUCHER
    function applyVoucher() {
        const codeInput = document.getElementById("voucherCode");
        const responseDiv = document.getElementById("voucherResponse");

        if (!codeInput) return;
        const code = codeInput.value.trim();

        if (code === "") {
            responseDiv.style.display = "block";
            responseDiv.style.color = "#d0011b";
            responseDiv.innerHTML = "<i class='fa-solid fa-circle-xmark'></i> Vui lòng nhập mã voucher!";
            return;
        }

        const params = new URLSearchParams();
        params.append('action', 'apply_voucher');
        params.append('voucherCode', code);

        fetch('${pageContext.request.contextPath}/checkout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: params.toString()
        })
            .then(response => {
                window.location.reload();
            })
            .catch(error => {
                console.error("Lỗi áp dụng voucher:", error);
                responseDiv.style.display = "block";
                responseDiv.style.color = "#d0011b";
                responseDiv.innerHTML = "<i class='fa-solid fa-circle-xmark'></i> Có lỗi hệ thống kết nối, vui lòng thử lại!";
            });
    }

    // TÍCH HỢP API GIAO HÀNG NHANH
    const GHN_TOKEN = "c48bc84b-5c05-11f1-a973-aee5264794df";
    const GHN_SHOP_ID = 200546;
    const SHOP_DISTRICT_ID = 3695;
    const SHOP_WARD_CODE = "90753";


    const orderItemsGHN = [];
    document.addEventListener('DOMContentLoaded', () => {
        document.querySelectorAll('.ghn-item-data').forEach(itemEl => {
            orderItemsGHN.push({
                "name": itemEl.getAttribute('data-name'),
                "code": itemEl.getAttribute('data-code'),
                "quantity": parseInt(itemEl.getAttribute('data-qty')),
                "price": parseInt(itemEl.getAttribute('data-price')),
                "length": 15, "width": 15, "height": 15, "weight": 300,
                "category": { "level1": "Đồng hồ" }
            });
        });

        const defaultAddress = document.querySelector('input[name="addressId"]:checked');
        if(defaultAddress) {
            updateGHNInfo(defaultAddress);
        }
    });

    const formatCurrency = (amount) => amount.toLocaleString('vi-VN') + ' ₫';

    const normalizeStr = (str) => {
        if(!str) return "";
        return str.toLowerCase()
            .replace(/(thành phố|tỉnh|quận|huyện|phường|xã|thị xã|thị trấn)/g, '')
            .replace(/[\s-]/g, '')
            .trim();
    };

    async function resolveLocationIDs(provName, distName, wardName) {
        try {

            let pRes = await fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/province", { headers: { "Token": GHN_TOKEN } });
            let pData = await pRes.json();
            let prov = pData.data.find(p => normalizeStr(p.ProvinceName) === normalizeStr(provName) || normalizeStr(p.ProvinceName).includes(normalizeStr(provName)));
            if (!prov) return null;

            let dRes = await fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/district", {
                method: "POST", headers: { "Content-Type": "application/json", "Token": GHN_TOKEN },
                body: JSON.stringify({ "province_id": prov.ProvinceID })
            });
            let dData = await dRes.json();
            let dist = dData.data.find(d => normalizeStr(d.DistrictName) === normalizeStr(distName) || normalizeStr(d.DistrictName).includes(normalizeStr(distName)));
            if (!dist) return null;

            let wRes = await fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/master-data/ward?district_id=" + dist.DistrictID, { headers: { "Token": GHN_TOKEN } });
            let wData = await wRes.json();
            let ward = wData.data.find(w => normalizeStr(w.WardName) === normalizeStr(wardName));

            return { district_id: dist.DistrictID, ward_code: ward ? ward.WardCode : null };
        } catch (e) {
            console.error("Lỗi đồng bộ mã địa chỉ: ", e);
            return null;
        }
    }

    async function updateGHNInfo(radioElement) {
        let feeDisplay = document.getElementById('shipping-fee');
        if(!feeDisplay) return;

        feeDisplay.innerText = "Đang đồng bộ...";

        let provName = radioElement.getAttribute('data-prov');
        let distName = radioElement.getAttribute('data-dist');
        let wardName = radioElement.getAttribute('data-ward');

        let locationIds = await resolveLocationIDs(provName, distName, wardName);

        if (!locationIds || !locationIds.district_id || !locationIds.ward_code) {
            feeDisplay.innerText = formatCurrency(30000);
            updateFinalTotal(30000);
            return;
        }

        document.getElementById('hidden-district-id').value = locationIds.district_id;
        document.getElementById('hidden-ward-code').value = locationIds.ward_code;

        const dynamicWeight = parseInt(document.getElementById('input-total-weight').value) || 500;
        const reqHeaders = { "Content-Type": "application/json", "Token": GHN_TOKEN, "ShopId": GHN_SHOP_ID };

        let feePromise = fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee", {
            method: "POST", headers: reqHeaders,
            body: JSON.stringify({
                "service_type_id": 2,
                "to_district_id": parseInt(locationIds.district_id),
                "to_ward_code": String(locationIds.ward_code),
                "weight": dynamicWeight,
                "length": 15, "width": 15, "height": 15
            })
        }).then(res => res.json());

        let leadtimePromise = fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/leadtime", {
            method: "POST", headers: reqHeaders,
            body: JSON.stringify({
                "from_district_id": SHOP_DISTRICT_ID,
                "from_ward_code": SHOP_WARD_CODE,
                "to_district_id": parseInt(locationIds.district_id),
                "to_ward_code": String(locationIds.ward_code),
                "service_id": 53320
            })
        }).then(res => res.json());

        Promise.all([feePromise, leadtimePromise]).then(([feeData, leadData]) => {
            if (feeData.code === 200) updateFinalTotal(feeData.data.total);
            else updateFinalTotal(30000);

            if (leadData.code === 200 && leadData.data.leadtime) {
                let leadDate = new Date(leadData.data.leadtime * 1000);
                document.getElementById('lead-time').innerText = "Ngày " + leadDate.toLocaleDateString('vi-VN', { day: '2-digit', month: '2-digit', year: 'numeric' });
            } else {
                document.getElementById('lead-time').innerText = "Chưa xác định";
            }
        }).catch(() => {
            updateFinalTotal(30000);
        });
    }

    function updateFinalTotal(fee) {
        const feeElement = document.getElementById('shipping-fee');
        const inputFeeElement = document.getElementById('input-shipping-fee');
        const finalTotalElement = document.getElementById('final-total');
        const inputFinalTotalElement = document.getElementById('input-final-total');

        if(feeElement) feeElement.innerText = formatCurrency(fee);
        if(inputFeeElement) inputFeeElement.value = fee;

        if(finalTotalElement && inputFinalTotalElement) {
            let rawSubtotal = finalTotalElement.getAttribute('data-subtotal') || "0";
            const subTotal = parseInt(parseFloat(rawSubtotal)) || 0;
            const newTotal = subTotal + fee;

            finalTotalElement.innerText = formatCurrency(newTotal);
            inputFinalTotalElement.value = newTotal;
        }
    }

    const checkoutForm = document.getElementById('checkoutForm');
    if(checkoutForm) {
        checkoutForm.addEventListener('submit', async function(e) {
            e.preventDefault();

            const checkedRadio = document.querySelector('input[name="addressId"]:checked');
            if(!checkedRadio) {
                alert("Vui lòng chọn địa chỉ giao hàng!");
                return;
            }

            const btn = document.getElementById('btn-checkout');
            if(btn) {
                btn.innerText = "ĐANG TẠO ĐƠN GHN...";
                btn.disabled = true;
            }

            let targetDistrictId = parseInt(document.getElementById('hidden-district-id').value) || null;
            let targetWardCode = document.getElementById('hidden-ward-code').value || "";

            const paymentMethod = document.querySelector('input[name="paymentMethod"]:checked').value;
            const dynamicWeight = parseInt(document.getElementById('input-total-weight').value) || 500;

            let rawFinalTotal = document.getElementById('input-final-total').value || "0";

            const totalAmount = parseInt(parseFloat(rawFinalTotal)) || 0;
            const codAmount = (paymentMethod === 'VNPAY') ? 0 : totalAmount;

            const payload = {
                "payment_type_id": (paymentMethod === 'VNPAY') ? 1 : 2,
                "note": "Đơn hàng từ VVP Store",
                "required_note": "CHOXEMHANGKHONGTHU",
                "client_order_code": "VVP_" + new Date().getTime(),
                "to_name": checkedRadio.getAttribute('data-name'),
                "to_phone": checkedRadio.getAttribute('data-phone'),
                "to_address": checkedRadio.getAttribute('data-street'),
                "to_ward_name": checkedRadio.getAttribute('data-ward'),
                "to_district_name": checkedRadio.getAttribute('data-dist'),
                "to_province_name": checkedRadio.getAttribute('data-prov'),
                "to_ward_code": targetWardCode,
                "to_district_id": targetDistrictId,
                "cod_amount": codAmount,
                "weight": dynamicWeight,
                "length": 15, "width": 15, "height": 15,
                "service_type_id": 2,
                "items": orderItemsGHN
            };

            if(!targetDistrictId || !targetWardCode) {
                this.submit();
                return;
            }

            try {
                let res = await fetch("https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create", {
                    method: "POST",
                    headers: { "Content-Type": "application/json", "Token": GHN_TOKEN, "ShopId": GHN_SHOP_ID },
                    body: JSON.stringify(payload)
                });
                let data = await res.json();

                if(data.code === 200) {
                    let inputGhn = document.createElement("input");
                    inputGhn.type = "hidden";
                    inputGhn.name = "ghn_order_code";
                    inputGhn.value = data.data.order_code;
                    this.appendChild(inputGhn);
                    this.submit();
                } else {
                    alert("Hệ thống GHN từ chối tạo đơn: " + data.message);
                    if(btn) {
                        btn.innerText = "ĐẶT HÀNG NGAY";
                        btn.disabled = false;
                    }
                }
            } catch(err) {
                alert("Lỗi kết nối tới Giao Hàng Nhanh!");
                if(btn) {
                    btn.innerText = "ĐẶT HÀNG NGAY";
                    btn.disabled = false;
                }
            }
        });
    }
</script>
</body>
</html>