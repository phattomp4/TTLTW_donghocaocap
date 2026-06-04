<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Danh mục & Giá | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: 100%; }
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .card h2 { margin-top: 0; border-bottom: 2px solid #eee; padding-bottom: 10px; color: #333; font-size: 20px; }

        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { padding: 12px; border-bottom: 1px solid #eee; text-align: left; font-size: 14px; }
        th { background: #343a40; color: white; }
        tr:hover { background-color: #f1f1f1; }

        .form-inline { background: #f8f9fa; padding: 20px; border-radius: 5px; display: flex; gap: 15px; align-items: flex-end; border: 1px solid #ddd; flex-wrap: wrap; }
        .form-group { display: flex; flex-direction: column; gap: 5px; }
        .form-control { padding: 8px; border: 1px solid #ccc; border-radius: 4px; min-width: 150px; }

        .btn { padding: 8px 15px; border: none; border-radius: 4px; cursor: pointer; color: white; font-weight: 500; text-decoration: none; display: inline-block;}
        .btn-add { background: #28a745; }
        .btn-update { background: #007bff; }
        .btn-delete { background: #dc3545; padding: 5px 10px; font-size: 12px; }
        .btn-edit { background: #ffc107; color: #333; padding: 5px 10px; font-size: 12px; margin-right: 5px; }
        .btn-cancel { background: #6c757d; display: none; }

        .handle { cursor: grab; color: #ccc; margin-right: 10px; }

        .switch { position: relative; display: inline-block; width: 34px; height: 20px; }
        .switch input { opacity: 0; width: 0; height: 0; }
        .slider { position: absolute; cursor: pointer; top: 0; left: 0; right: 0; bottom: 0; background-color: #ccc; transition: .4s; border-radius: 20px; }
        .slider:before { position: absolute; content: ""; height: 14px; width: 14px; left: 3px; bottom: 3px; background-color: white; transition: .4s; border-radius: 50%; }
        input:checked + .slider { background-color: #28a745; }
        input:checked + .slider:before { transform: translateX(14px); }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard" ><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"  class="active"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">

    <div class="card">
        <h2><i class="fa-solid fa-list"></i> Quản lý Danh mục Sản phẩm</h2>

        <form action="category-manager" method="POST" class="form-inline" id="catForm">
            <input type="hidden" name="action" id="catAction" value="add">
            <input type="hidden" name="id" id="catId" value="">

            <div class="form-group">
                <label>Tên Danh Mục:</label>
                <input type="text" name="name" id="catName" class="form-control" required placeholder="Ví dụ: Hublot">
            </div>

            <div class="form-group">
                <label>Thuộc Nhóm:</label>
                <select name="parentId" id="catParent" class="form-control">
                    <option value="0">Danh mục gốc</option>
                    <option value="1">Thương hiệu</option>
                    <option value="2">Bộ sưu tập</option>
                    <option value="3">Phụ kiện</option>
                </select>
            </div>

            <div class="form-group">
                <label>Thứ tự (Vị trí):</label>
                <input type="number" name="sortOrder" id="catSortOrder" class="form-control" value="1" min="1" style="min-width: 80px;">
            </div>

            <div class="form-group">
                <label>Trạng thái:</label>
                <div style="padding-top: 8px;">
                    <label class="switch">
                        <input type="checkbox" name="isActive" id="catActive" checked>
                        <span class="slider"></span>
                    </label> Hiển thị
                </div>
            </div>

            <div class="form-group">
                <label>&nbsp;</label> <div>
                <button type="submit" class="btn btn-add" id="btnCatSubmit"><i class="fa-solid fa-plus"></i> Thêm mới</button>
                <button type="button" class="btn btn-cancel" id="btnCatCancel" onclick="cancelEditCat()">Hủy</button>
            </div>
            </div>
        </form>

        <table>
            <thead>
            <tr>
                <th width="30"></th>
                <th>ID</th>
                <th>Tên Danh Mục</th>
                <th>Thuộc Nhóm</th>
                <th>Vị trí</th>
                <th>Hiển thị</th>
                <th width="150">Hành động</th>
            </tr>
            </thead>
            <tbody id="sortable-cats">
            <c:forEach items="${listCats}" var="c">
                <tr data-id="${c.id}">
                    <td><i class="fa-solid fa-grip-vertical handle"></i></td>
                    <td>${c.id}</td>
                    <td><b>${c.name}</b></td>
                    <td>
                        <c:choose>
                            <c:when test="${c.parentId == 0}"><b>Gốc</b></c:when>
                            <c:when test="${c.parentId == 1}"><span style="color:blue">Thương hiệu</span></c:when>
                            <c:when test="${c.parentId == 2}"><span style="color:orange">Bộ sưu tập</span></c:when>
                            <c:when test="${c.parentId == 3}"><span style="color:green">Phụ kiện</span></c:when>
                            <c:otherwise>Khác</c:otherwise>
                        </c:choose>
                    </td>
                    <td class="sort-val">${c.sortOrder}</td>
                    <td>
                        <label class="switch">
                            <input type="checkbox" ${c.active ? 'checked' : ''} onchange="toggleStatus('${c.id}', this.checked)">
                            <span class="slider"></span>
                        </label>
                    </td>
                    <td>
                        <button class="btn btn-edit" onclick="editCategory('${c.id}', '${c.name}', '${c.parentId}', '${c.sortOrder}', ${c.active})">
                            <i class="fa-solid fa-pen"></i>
                        </button>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <div class="card" style="margin-top: 30px;">
        <h2 style="color: #d0011b;"><i class="fa-solid fa-tags"></i> Quản lý Khoảng Giá</h2>

        <form action="category-manager" method="POST" id="priceForm" style="background: #fff0f0; padding: 15px; border-radius: 5px; display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" id="priceAction" value="addPrice">
            <input type="hidden" name="id" id="priceId" value="">

            <div>
                <label>Tên hiển thị:</label><br>
                <input type="text" name="label" id="priceLabel" placeholder="VD: Dưới 1 triệu" required style="padding: 8px;">
            </div>
            <div>
                <label>Giá Min:</label><br>
                <input type="number" name="min" id="priceMin" value="0" required style="padding: 8px; width: 100px;">
            </div>
            <div>
                <label>Giá Max (-1 là vô cùng):</label><br>
                <input type="number" name="max" id="priceMax" required style="padding: 8px; width: 100px;">
            </div>

            <div>
                <button type="submit" id="btnPriceSubmit" style="padding: 8px 15px; background: #d0011b; color: white; border: none; cursor: pointer;">Thêm giá</button>
                <button type="button" id="btnPriceCancel" onclick="cancelEditPrice()" style="padding: 8px 15px; background: #6c757d; color: white; border: none; cursor: pointer; display: none;">Hủy</button>
            </div>
        </form>

        <table>
            <thead>
            <tr>
                <th>Tên hiển thị</th>
                <th>Min</th>
                <th>Max</th>
                <th width="150">Hành động</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${menuPrices}" var="p">
                <tr>
                    <td><b>${p.label}</b></td>
                    <td><fmt:formatNumber value="${p.minPrice}" type="currency"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${p.maxPrice == -1}">Trên mức Min</c:when>
                            <c:otherwise><fmt:formatNumber value="${p.maxPrice}" type="currency"/></c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <button type="button" onclick="editPrice('${p.id}', '${p.label}', '${p.minPrice}', '${p.maxPrice}')"
                                style="background: #ffc107; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; margin-right: 5px;">
                            <i class="fa-solid fa-pen"></i>
                        </button>

                        <a href="category-manager?action=deletePrice&id=${p.id}" onclick="return confirm('Xóa khoảng giá này?')"
                           style="background: #dc3545; color: white; padding: 5px 10px; border-radius: 4px; text-decoration: none; font-size: 13px;">
                            <i class="fa-solid fa-trash"></i>
                        </a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</div>

<script>
    function toggleStatus(id, status) {
        location.href = 'category-manager?action=toggleStatus&id=' + id + '&status=' + status;
    }

    const sortable = new Sortable(document.getElementById('sortable-cats'), {
        handle: '.handle',
        animation: 150,
        onEnd: function (evt) {
            let items = document.querySelectorAll('#sortable-cats tr');
            items.forEach((tr, index) => {
                let id = tr.dataset.id;
                let order = index + 1;
                fetch('category-manager', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                    body: 'action=updateOrder&id=' + id + '&sortOrder=' + order
                });
                tr.querySelector('.sort-val').innerText = order;
            });
        }
    });

    function editCategory(id, name, parentId, sortOrder, isActive) {
        document.getElementById('catName').value = name;
        document.getElementById('catParent').value = parentId;
        document.getElementById('catId').value = id;
        document.getElementById('catSortOrder').value = sortOrder;
        document.getElementById('catActive').checked = isActive;

        document.getElementById('catAction').value = 'update';

        const btn = document.getElementById('btnCatSubmit');
        btn.innerHTML = '<i class="fa-solid fa-save"></i> Cập nhật';
        btn.classList.remove('btn-add');
        btn.classList.add('btn-update');

        document.getElementById('btnCatCancel').style.display = 'inline-block';
        document.getElementById('catForm').scrollIntoView({ behavior: 'smooth' });
    }

    function cancelEditCat() {
        document.getElementById('catForm').reset();
        document.getElementById('catAction').value = 'add';
        document.getElementById('catId').value = '';

        const btn = document.getElementById('btnCatSubmit');
        btn.innerHTML = '<i class="fa-solid fa-plus"></i> Thêm mới';
        btn.classList.remove('btn-update');
        btn.classList.add('btn-add');
        document.getElementById('btnCatCancel').style.display = 'none';
    }

    function editPrice(id, label, min, max) {
        document.getElementById('priceId').value = id;
        document.getElementById('priceLabel').value = label;
        document.getElementById('priceMin').value = parseFloat(min);
        document.getElementById('priceMax').value = parseFloat(max);
        document.getElementById('priceAction').value = 'updatePrice';

        const btn = document.getElementById('btnPriceSubmit');
        btn.innerHTML = '<i class="fa-solid fa-save"></i> Cập nhật';
        btn.style.background = '#007bff';

        document.getElementById('btnPriceCancel').style.display = 'inline-block';
    }

    function cancelEditPrice() {
        document.getElementById('priceForm').reset();
        document.getElementById('priceAction').value = 'addPrice';
        document.getElementById('priceId').value = '';

        const btn = document.getElementById('btnPriceSubmit');
        btn.innerHTML = 'Thêm giá';
        btn.style.background = '#d0011b';

        document.getElementById('btnPriceCancel').style.display = 'none';
    }
</script>

</body>
</html>