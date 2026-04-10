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
        .search-box {
            display: flex;
            background: white;
            border: 1px solid #ddd;
            border-radius: 5px;
            overflow: hidden;
            margin-right: 20px;
        }
        .search-box input {
            border: none;
            padding: 10px;
            outline: none;
            width: 250px;
        }
        .search-box button {
            background: #343a40;
            color: white;
            border: none;
            padding: 0 15px;
            cursor: pointer;
        }
        .search-box button:hover { background: #d0011b; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: 100%; }

        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .btn-add { background: #28a745; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px; font-weight: bold; transition: 0.2s; box-shadow: 0 2px 5px rgba(0,0,0,0.2); }
        .btn-add:hover { background: #218838; transform: translateY(-2px); }

        .table-container { background: white; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle; }
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 14px; }
        tr:hover { background-color: #f9f9f9; }

        .prod-img { width: 60px; height: 60px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd; }

        .btn-action { padding: 6px 12px; border-radius: 4px; text-decoration: none; font-size: 14px; margin-right: 5px; display: inline-block; border: none; cursor: pointer; transition: 0.2s;}
        .btn-edit { background: #e0f3ff; color: #007bff; }
        .btn-edit:hover { background: #007bff; color: white; }
        .btn-delete { background: #ffe6e6; color: #dc3545; }
        .btn-delete:hover { background: #dc3545; color: white; }

        .btn-feature { background: #fff8e1; color: #ffc107; border: 1px solid #ffe082; }
        .btn-feature:hover { background: #ffc107; color: white; }
        .btn-feature.active { background: #ffc107; color: white; box-shadow: inset 0 3px 5px rgba(0,0,0,0.125); border-color: #ffb300; }

        .toast-msg {
            visibility: hidden; min-width: 250px; background-color: #333; color: #fff; text-align: center; border-radius: 5px; padding: 16px; position: fixed; z-index: 1000; left: 50%; bottom: 30px; transform: translateX(-50%); font-size: 15px; transition: visibility 0s, opacity 0.5s linear; opacity: 0;
        }
        .toast-msg.show { visibility: visible; opacity: 1; }
        .toast-msg.success { background-color: #28a745; }
        .toast-msg.error { background-color: #dc3545; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager" class="active"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>

    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div class="page-header">
        <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333;">Danh sách sản phẩm</h2>

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
                <th style="width: 50px;">ID</th>
                <th style="width: 80px;">Ảnh</th>
                <th>Tên sản phẩm</th>
                <th>Mã SKU</th>
                <th>Giá bán</th>
                <th>Kho</th>
                <th>Đã bán</th>
                <th style="text-align: center; width: 180px;">Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty listProducts}">
                <tr>
                    <td colspan="8" style="text-align: center; padding: 30px; color: #777;">
                        Chưa có sản phẩm nào. Hãy bấm "Thêm sản phẩm mới".
                    </td>
                </tr>
            </c:if>

            <c:forEach items="${listProducts}" var="p">
                <tr>
                    <td><b>#${p.id}</b></td>
                    <td>
                        <c:choose>
                            <c:when test="${p.imageUrl.startsWith('http')}">
                                <img src="${p.imageUrl}" class="prod-img" onerror="this.src='https://via.placeholder.com/60'">
                            </c:when>
                            <c:otherwise>
                                <img src="${pageContext.request.contextPath}/${p.imageUrl}" class="prod-img" onerror="this.src='https://via.placeholder.com/60'">
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <div style="font-weight: bold;">
                            <a href="${pageContext.request.contextPath}/detail?pid=${p.id}"
                               target="_blank"
                               style="color: #333; text-decoration: none; transition: 0.2s;"
                               onmouseover="this.style.color='#d0011b'"
                               onmouseout="this.style.color='#333'">
                                    ${p.name} <i class="fa-solid fa-arrow-up-right-from-square" style="font-size: 11px; color: #999;"></i>
                            </a>
                        </div>
                        <div style="font-size: 12px; color: #777;">
                            Giá gốc: <fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫"/>
                        </div>
                    </td>
                    <td><span style="background: #eee; padding: 2px 6px; border-radius: 3px; font-size: 12px;">${p.sku}</span></td>
                    <td style="color: #d0011b; font-weight: bold;">
                        <fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${p.stockQuantity > 0}">
                                <span style="color: green; font-weight: bold;">${p.stockQuantity}</span>
                            </c:when>
                            <c:otherwise>
                                <span style="color: red; font-weight: bold;">Hết hàng</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${p.soldQuantity}</td>

                    <td style="text-align: center; white-space: nowrap;">

                        <button type="button" class="btn-action btn-feature ${p.isFeatured == 1 ? 'active' : ''}"
                                id="btn-feature-${p.id}"
                                onclick="toggleFeatured(${p.id}, ${p.isFeatured == 1 ? 0 : 1})"
                                title="${p.isFeatured == 1 ? 'Bỏ Nổi Bật' : 'Đánh dấu Nổi Bật'}">
                            <i class="${p.isFeatured == 1 ? 'fa-solid' : 'fa-regular'} fa-star"></i>
                        </button>

                        <a href="product-form?id=${p.id}" class="btn-action btn-edit" title="Sửa">
                            <i class="fa-solid fa-pen-to-square"></i>
                        </a>

                        <form action="product-manager" method="POST" style="display: inline-block;" onsubmit="return confirm('CẢNH BÁO: Bạn có chắc chắn muốn xóa sản phẩm này? Hành động này không thể hoàn tác!');">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="pid" value="${p.id}">
                            <button type="submit" class="btn-action btn-delete" title="Xóa">
                                <i class="fa-solid fa-trash"></i>
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<div id="toastBox" class="toast-msg">Thông báo...</div>

<script>
    function toggleFeatured(productId, newStatus) {
        fetch('product-action', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
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
                        showToast('Đã thêm vào danh sách Nổi Bật!', 'success');
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

        setTimeout(function(){
            toast.className = toast.className.replace("show", "");
        }, 3000);
    }
</script>

</body>
</html>