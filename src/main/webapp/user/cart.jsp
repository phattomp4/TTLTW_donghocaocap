<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng | VVP Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/GioHang.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        .cart-table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        .cart-table th, .cart-table td { padding: 15px; text-align: center; border-bottom: 1px solid #ddd; }
        .cart-img { width: 80px; height: 80px; object-fit: cover; border: 1px solid #eee; transition: 0.3s; border-radius: 4px; }
        .cart-img:hover { transform: scale(1.05); }
        .btn-qty { padding: 5px 10px; border: 1px solid #ccc; background: #fff; cursor: pointer; text-decoration: none; color: #333; display: inline-block;}
        .btn-qty:hover { background: #eee; }
        .btn-delete { color: #d0011b; cursor: pointer; text-decoration: none; font-weight: bold; background: none; border: none; font-size: 14px;}
        .btn-delete:hover { color: #a80015; text-decoration: underline; }
        .cart-summary { margin-top: 30px; text-align: right; }
        .checkout-btn { background: #d0011b; color: #fff; padding: 12px 25px; text-decoration: none; display: inline-block; margin-top: 10px; border-radius: 4px; font-weight: bold; border: none; cursor: pointer;}
        .checkout-btn:hover { background: #a80015; }
        .btn-empty-cart:hover { background-color: #14555b !important; }
        .item-check, #checkAll { width: 18px; height: 18px; cursor: pointer; accent-color: #1b6e76; }
        .loading-overlay { opacity: 0.5; pointer-events: none; transition: 0.2s; }
    </style>
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />
<script src="${pageContext.request.contextPath}/assets/js/index.js"></script>

<c:if test="${not empty cartErrorMsg}">
    <script>
        alert('${cartErrorMsg}');
    </script>
</c:if>

<div class="container" style="margin-top: 5px; min-height: 500px; max-width: 1200px; margin-left: auto; margin-right: auto; padding: 20px;">

    <h3 style="font-size: 1.5em; font-weight: bold; border-bottom: 2px solid #1b6e76; padding-bottom: 10px; color: #1b6e76; margin-bottom: 40px;">
        Giỏ Hàng Của Bạn <i class="fa-solid fa-cart-shopping"></i>
    </h3>

    <c:if test="${not empty error}">
        <div style="background-color: #f8d7da; color: #721c24; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #f5c6cb; font-weight: bold;">
            <i class="fa-solid fa-circle-exclamation"></i> ${error}
        </div>
    </c:if>

    <%-- Kiểm tra nếu chưa có giỏ hàng thì hiển thị giao diện trống --%>
    <c:if test="${empty sessionScope.cart}">
        <div style="text-align: center; padding: 50px 0;">
            <i class="fa-solid fa-cart-shopping" style="font-size: 80px; color: #888; margin-bottom: 20px;"></i>
            <p style="color: #666; font-size: 16px; margin-bottom: 20px;">Giỏ hàng của bạn đang trống.</p>

            <a href="${pageContext.request.contextPath}/home" class="btn-empty-cart" style="display: inline-block; background-color: #1b6e76; color: white; padding: 12px 30px; border-radius: 5px; text-decoration: none; font-weight: bold; transition: background 0.3s;">
                Tiếp tục mua sắm
            </a>
        </div>
    </c:if>

    <%-- Kiểm tra nếu có giỏ hàng thì hiển thị danh sách --%>
    <c:if test="${not empty sessionScope.cart}">
        <div id="cart-content-wrapper">
            <table class="cart-table" id="cartTable">
                <thead>
                <tr style="background: #f9f9f9;">
                    <th style="width: 50px;">
                        <input type="checkbox" id="checkAll" onchange="toggleAll(this)">
                    </th>
                    <th>Sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody id="cartTbody">
                <c:forEach items="${sessionScope.cart}" var="item">
                    <tr>
                        <td>
                            <input type="checkbox" class="item-check"
                                   value="${item.product.id}"
                                   data-price="<fmt:formatNumber value='${item.totalPrice}' pattern='0' groupingUsed='false'/>"
                                   onchange="updateTotal()">
                        </td>
                        <td style="text-align: left; display: flex; align-items: center; gap: 15px;">
                            <a href="${pageContext.request.contextPath}/detail?pid=${item.product.id}">
                                <img src="${item.product.imageUrl}" class="cart-img" alt="<c:out value='${item.product.name}'/>">
                            </a>
                            <div>
                                <a href="${pageContext.request.contextPath}/detail?pid=${item.product.id}" style="text-decoration: none; color: #333;">
                                    <b style="font-size: 15px;"><c:out value="${item.product.name}"/></b>
                                </a>
                                <p style="color: #888; font-size: 13px; margin: 0;">Mã: #${item.product.id}</p>
                            </div>
                        </td>
                        <td><fmt:formatNumber value="${item.product.currentPrice}" pattern="#,##0 ₫"/></td>
                        <td>
                            <button type="button" class="btn-qty" onclick="updateCartItemAJAX(${item.product.id}, ${item.quantity - 1})">-</button>
                            <span style="margin: 0 10px; font-weight: bold;">${item.quantity}</span>
                            <button type="button" class="btn-qty" onclick="updateCartItemAJAX(${item.product.id}, ${item.quantity + 1})">+</button>
                        </td>
                        <td style="color: #d0011b; font-weight: bold;">
                            <fmt:formatNumber value="${item.totalPrice}" pattern="#,##0 ₫"/>
                        </td>
                        <td>
                            <button type="button" class="btn-delete" onclick="deleteCartItemAJAX(${item.product.id})">
                                <i class="fa-solid fa-trash"></i> Xóa
                            </button>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <div class="voucher-input" style="margin-bottom: 20px; text-align: right; margin-top: 20px;">
                <form action="cart" method="GET">
                    <input type="text" name="voucherCode" placeholder="Nhập mã giảm giá..." style="padding: 8px; border: 1px solid #ddd; outline: none; border-radius: 4px 0 0 4px;">
                    <button type="submit" style="padding: 8px 15px; background: #1b6e76; color: white; border: none; cursor: pointer; border-radius: 0 4px 4px 0;">Áp dụng</button>
                </form>
                <p style="color: #d0011b; margin-top: 5px;">${voucherMessage}</p>
            </div>

            <div class="cart-summary">
                <div style="background: #fdfdfd; padding: 20px; border: 1px solid #eee; display: inline-block; min-width: 300px; text-align: right; border-radius: 8px;">
                    <p>Tạm tính: <b id="temp-total">0 ₫</b></p>

                    <c:if test="${discount > 0}">
                        <p>Giảm giá: <b style="color: green;">- <fmt:formatNumber value="${discount}" pattern="#,##0 ₫"/></b></p>
                    </c:if>

                    <h3 style="color: #d0011b; margin-top: 10px; border-top: 1px dashed #ddd; padding-top: 15px;">
                        Tổng cộng: <span id="final-total">0 ₫</span>
                    </h3>

                    <input type="hidden" id="hidden-discount" value="<fmt:formatNumber value='${not empty discount ? discount : 0}' pattern='0' groupingUsed='false'/>">

                    <button type="button" onclick="goToCheckout()" class="checkout-btn" style="width: 100%; box-sizing: border-box; margin-top: 15px;">
                        Tiến hành thanh toán
                    </button>
                </div>
            </div>
        </div>
    </c:if>
</div>

<script>
    const formatCurrency = (amount) => amount.toLocaleString('vi-VN') + ' ₫';
    async function updateCartItemAJAX(pid, newQty) {
        if (newQty < 1) return;

        let wrapper = document.getElementById('cart-content-wrapper');
        wrapper.classList.add('loading-overlay');

        let checkedIds = Array.from(document.querySelectorAll('.item-check:checked')).map(cb => cb.value);

        try {
            let response = await fetch('${pageContext.request.contextPath}/cart?action=update&pid=' + pid + '&quantity=' + newQty);
            if (response.ok) {
                let html = await response.text();
                replaceCartDOM(html, checkedIds);
            } else if (response.redirected) {
                window.location.href = response.url;
            }
        } catch (error) {
            console.error("Lỗi cập nhật giỏ hàng", error);
        } finally {
            wrapper.classList.remove('loading-overlay');
        }
    }

    async function deleteCartItemAJAX(pid) {
        if(!confirm('Bạn chắc chắn muốn xóa sản phẩm này?')) return;

        let wrapper = document.getElementById('cart-content-wrapper');
        wrapper.classList.add('loading-overlay');

        let checkedIds = Array.from(document.querySelectorAll('.item-check:checked')).map(cb => cb.value);

        try {
            let response = await fetch('${pageContext.request.contextPath}/cart?action=delete&pid=' + pid);
            if (response.ok) {
                let html = await response.text();
                let doc = new DOMParser().parseFromString(html, 'text/html');

                let newTbody = doc.getElementById('cartTbody');
                if(!newTbody || newTbody.innerHTML.trim() === '') {
                    window.location.reload();
                    return;
                }

                replaceCartDOM(html, checkedIds);
            }
        } catch (error) {
            console.error("Lỗi xóa sản phẩm", error);
        } finally {
            if(wrapper) wrapper.classList.remove('loading-overlay');
        }
    }

    function replaceCartDOM(html, checkedIds) {
        let doc = new DOMParser().parseFromString(html, 'text/html');

        document.getElementById('cartTbody').innerHTML = doc.getElementById('cartTbody').innerHTML;

        let newHeaderCount = doc.getElementById('cartCountHeader');
        if(newHeaderCount && document.getElementById('cartCountHeader')) {
            document.getElementById('cartCountHeader').innerHTML = newHeaderCount.innerHTML;
        }

        document.querySelectorAll('.item-check').forEach(cb => {
            if(checkedIds.includes(cb.value)) cb.checked = true;
        });

        updateTotal();
    }
    function toggleAll(source) {
        const checkboxes = document.querySelectorAll('.item-check');
        checkboxes.forEach(cb => cb.checked = source.checked);
        updateTotal();
    }

    function updateTotal() {
        const checkboxes = document.querySelectorAll('.item-check');
        let total = 0;
        let discount = parseInt(document.getElementById('hidden-discount')?.value) || 0;
        let allChecked = true;
        let hasItem = false;

        checkboxes.forEach(cb => {
            if (cb.checked) {
                total += parseInt(cb.getAttribute('data-price')) || 0;
            } else {
                allChecked = false;
            }
            hasItem = true;
        });

        if(hasItem && document.getElementById('checkAll')) {
            document.getElementById('checkAll').checked = allChecked;
        }

        let tempTotalEl = document.getElementById('temp-total');
        let finalTotalEl = document.getElementById('final-total');

        if(tempTotalEl) tempTotalEl.innerText = formatCurrency(total);

        let finalTotal = total > 0 ? (total - discount) : 0;
        if(finalTotal < 0) finalTotal = 0;

        if(finalTotalEl) finalTotalEl.innerText = formatCurrency(finalTotal);
    }

    function goToCheckout() {
        const checkedItems = document.querySelectorAll('.item-check:checked');
        if (checkedItems.length === 0) {
            alert("Vui lòng chọn ít nhất 1 sản phẩm để thanh toán!");
            return;
        }

        let selectedIds = [];
        checkedItems.forEach(item => selectedIds.push(item.value));
        window.location.href = "${pageContext.request.contextPath}/checkout?selectedIds=" + selectedIds.join(',');
    }

    window.addEventListener('DOMContentLoaded', () => {
        const checkAllBtn = document.getElementById('checkAll');
        if(checkAllBtn){
            checkAllBtn.checked = true;
            toggleAll(checkAllBtn);
        }
    });
</script>

<jsp:include page="../WEB-INF/tags/footer.jsp" />
</body>
</html>