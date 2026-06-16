<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Sản phẩm | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; display: flex; margin: 0; background: #f4f6f9; }
        .search-box { display: flex; background: white; border: 1px solid #ddd; border-radius: 5px; overflow: hidden; margin-right: 20px; }
        .search-box input { border: none; padding: 10px; outline: none; width: 250px; }
        .search-box button { background: #343a40; color: white; border: none; padding: 0 15px; cursor: pointer; transition: 0.2s; }
        .search-box button:hover { background: #1b6e76; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100;}
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: 100%; box-sizing: border-box; }

        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .btn-add { background: #1b6e76; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; transition: 0.2s; box-shadow: 0 2px 5px rgba(0,0,0,0.2); }
        .btn-add:hover { background: #145258; transform: translateY(-2px); }

        .table-container { background: white; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle; }
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 14px; }
        tr:hover { background-color: #fcfcfc; }
        .row-hidden { background-color: #f8f9fa; opacity: 0.6; }

        .prod-img { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd; }

        .btn-action { padding: 6px 12px; border-radius: 4px; text-decoration: none; font-size: 14px; margin-right: 5px; display: inline-block; border: none; cursor: pointer; transition: 0.2s;}
        .btn-edit { background: #e0f3ff; color: #007bff; }
        .btn-edit:hover { background: #007bff; color: white; }
        .btn-delete { background: #ffe6e6; color: #dc3545; }
        .btn-delete:hover { background: #dc3545; color: white; }
        .btn-show { background: #e2e3e5; color: #383d41; }
        .btn-show:hover { background: #383d41; color: white; }

        .btn-feature { background: #fff8e1; color: #ffc107; border: 1px solid #ffe082; }
        .btn-feature:hover { background: #ffc107; color: white; }
        .btn-feature.active { background: #ffc107; color: white; box-shadow: inset 0 3px 5px rgba(0,0,0,0.125); border-color: #ffb300; }

        .toast-msg { visibility: hidden; min-width: 250px; background-color: #333; color: #fff; text-align: center; border-radius: 5px; padding: 16px; position: fixed; z-index: 1000; left: 50%; bottom: 30px; transform: translateX(-50%); font-size: 15px; transition: visibility 0s, opacity 0.5s linear; opacity: 0; }
        .toast-msg.show { visibility: visible; opacity: 1; }
        .toast-msg.success { background-color: #28a745; }
        .toast-msg.error { background-color: #dc3545; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>
    <a href="order-manager" ><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager" class="active"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
        <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
        <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
        <a href="category-manager"><i class="fa-solid fa-list"></i> Quản lý tìm kiếm</a>
        <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div class="page-header">
        <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333; margin: 0;">Danh sách sản phẩm</h2>
        <div style="display: flex; align-items: center;">
            <form action="product-manager" method="GET" class="search-box">
                <input type="text" name="keyword" placeholder="Nhập tên sản phẩm..." value="${searchKeyword}">
                <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
            </form>
            <a href="product-form" class="btn-add"><i class="fa-solid fa-plus"></i> Thêm sản phẩm mới</a>
        </div>
    </div>

    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th>ID</th><th>Ảnh</th><th>Tên sản phẩm</th><th>Mã SKU</th><th>Giá bán</th><th>Kho</th><th>Đã bán</th><th>Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${listProducts}" var="p">
                <tr class="${p.isActive == 0 ? 'row-hidden' : ''}">
                    <td><b>#${p.id}</b></td>
                    <td>
                        <img src="${p.imageUrl.startsWith('http') ? p.imageUrl : pageContext.request.contextPath.concat('/').concat(p.imageUrl)}"
                             class="prod-img" onerror="this.src='https://via.placeholder.com/60'">
                    </td>
                    <td>
                        <div style="font-weight: bold;">
                                ${p.name}
                            <c:if test="${p.isActive == 0}">
                                <span style="background: #dc3545; color: white; padding: 2px 6px; border-radius: 3px; font-size: 11px; margin-left: 5px;">Đã ẩn</span>
                            </c:if>
                        </div>
                        <div style="font-size: 12px; color: #777;">Giá gốc: <fmt:formatNumber value="${p.originalPrice}" pattern="#,##0 ₫"/></div>
                    </td>
                    <td>${p.sku}</td>
                    <td style="color: #d0011b; font-weight: bold;"><fmt:formatNumber value="${p.currentPrice}" pattern="#,##0 ₫"/></td>
                    <td>${p.stockQuantity}</td>
                    <td>${p.soldQuantity}</td>
                    <td style="text-align: center; white-space: nowrap;">

                        <button type="button" class="btn-action btn-feature ${p.featured ? 'active' : ''}"
                                id="btn-feature-${p.id}" onclick="toggleFeatured(${p.id}, ${p.featured ? 0 : 1})"
                                title="${p.featured ? 'Bỏ Nổi Bật' : 'Đánh dấu Nổi Bật'}">
                            <i class="${p.featured ? 'fa-solid' : 'fa-regular'} fa-star"></i>
                        </button>

                        <a href="product-form?id=${p.id}" class="btn-action btn-edit" title="Sửa thông tin"><i class="fa-solid fa-pen-to-square"></i></a>

                        <form action="product-manager" method="GET" style="display: inline-block;"
                              onsubmit="return confirm('Bạn có chắc chắn muốn ${p.isActive == 1 ? 'ẨN' : 'HIỆN'} sản phẩm này khỏi cửa hàng?');">
                            <input type="hidden" name="action" value="toggleStatus">
                            <input type="hidden" name="pid" value="${p.id}">
                            <input type="hidden" name="status" value="${p.isActive == 1 ? 0 : 1}">
                            <button type="submit" class="btn-action ${p.isActive == 1 ? 'btn-delete' : 'btn-show'}"
                                    title="${p.isActive == 1 ? 'Nhấn để Ẩn sản phẩm' : 'Nhấn để Hiện sản phẩm'}">
                                <i class="fa-solid ${p.isActive == 1 ? 'fa-eye-slash' : 'fa-eye'}"></i>
                            </button>
                        </form>

                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <c:if test="${totalPages > 1}">
            <div style="margin: 30px 0; display: flex; justify-content: center; gap: 5px;">
                <a href="product-manager?page=${currentPage > 1 ? currentPage - 1 : 1}&keyword=${searchKeyword}"
                   style="padding: 8px 16px; border: 1px solid #ddd; border-radius: 4px; text-decoration: none; color: #333; background: white;">
                    &laquo; Trước
                </a>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                        <a href="product-manager?page=${i}&keyword=${searchKeyword}"
                           style="padding: 8px 16px; border: 1px solid #ddd; border-radius: 4px; text-decoration: none;
                           ${currentPage == i ? 'background: #1b6e76; color: white; border-color: #1b6e76;' : 'background: white; color: #333;'}">
                                ${i}
                        </a>
                    </c:if>

                    <c:if test="${(i == currentPage - 3 && i > 1) || (i == currentPage + 3 && i < totalPages)}">
                        <span style="padding: 8px 12px; color: #999;">...</span>
                    </c:if>
                </c:forEach>

                <a href="product-manager?page=${currentPage < totalPages ? currentPage + 1 : totalPages}&keyword=${searchKeyword}"
                   style="padding: 8px 16px; border: 1px solid #ddd; border-radius: 4px; text-decoration: none; color: #333; background: white;">
                    Sau &raquo;
                </a>
            </div>
        </c:if>
    </div>
</div>

<div id="toastBox" class="toast-msg">Thông báo...</div>

<script>
    function toggleFeatured(productId, newStatus) {
        fetch('product-action', {
            method: 'POST',
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            body: 'pid=' + productId + '&featured=' + newStatus
        })
            .then(response => response.text())
            .then(data => {
                if (data.trim() === 'success') {
                    const btn = document.getElementById('btn-feature-' + productId);
                    const icon = btn.querySelector('i');

                    if (newStatus === 1) {
                        btn.classList.add('active');
                        icon.classList.remove('fa-regular');
                        icon.classList.add('fa-solid');
                        btn.title = 'Bỏ Nổi Bật';
                        btn.setAttribute('onclick', 'toggleFeatured(' + productId + ', 0)');
                        showToast('Đã thêm vào danh sách Sản phẩm Nổi Bật!', 'success');
                    } else {
                        btn.classList.remove('active');
                        icon.classList.remove('fa-solid');
                        icon.classList.add('fa-regular');
                        btn.title = 'Đánh dấu Nổi Bật';
                        btn.setAttribute('onclick', 'toggleFeatured(' + productId + ', 1)');
                        showToast('Đã xóa khỏi danh sách Nổi Bật.', 'success');
                    }
                } else {
                    showToast('Có lỗi xảy ra khi cập nhật!', 'error');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showToast('Lỗi kết nối Server!', 'error');
            });
    }

    function showToast(message, type) {
        const toast = document.getElementById("toastBox");
        toast.innerText = message;
        toast.className = "toast-msg show " + type;
        setTimeout(function(){ toast.className = toast.className.replace("show", ""); }, 3000);
    }
</script>

</body>
</html>