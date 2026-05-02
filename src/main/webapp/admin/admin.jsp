<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin | VVP Store</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/assets/css/admin.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
</head>
<body>

<nav class="sidebar">
    <div class="sidebar-header">
        <i class="fas fa-clock"></i>
        <span>WatchAdmin</span>
    </div>

    <ul class="sidebar-nav">
        <li>
            <a class="nav-link active" data-page="page-dashboard">
                <i class="fas fa-tachometer-alt"></i>
                Bảng điều khiển
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-products">
                <i class="fas fa-box"></i>
                Quản lý Sản phẩm
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-orders">
                <i class="fas fa-receipt"></i>
                Quản lý Đơn hàng
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-users">
                <i class="fas fa-users"></i>
                Quản lý Khách hàng
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-banners">
                <i class="fas fa-images"></i>
                Quản lý Banner
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-menus">
                <i class="fas fa-th-list"></i>
                Quản lý Menu
            </a>
        </li>
        <li>
            <a class="nav-link" data-page="page-settings">
                <i class="fas fa-cog"></i>
                Cài đặt
            </a>
        </li>
    </ul>
</nav>

<div class="main-wrapper">

    <header class="header">
        <div class="header-profile">
            <i class="fas fa-bell"></i> <span>Chào, Admin!</span>
        </div>
    </header>

    <main class="main-content">

        <div id="page-dashboard" class="page-content active">
            <div class="page-header">
                <h1>Bảng điều khiển</h1>
            </div>

            <div class="stat-cards">
                <div class="card sales">
                    <div class="card-icon"><i class="fas fa-dollar-sign"></i></div>
                    <h3>Tổng doanh thu</h3>
                    <p><fmt:formatNumber value="${revenue}" type="currency" currencySymbol="₫"/></p>
                </div>
                <div class="card orders">
                    <div class="card-icon"><i class="fas fa-receipt"></i></div>
                    <h3>Tổng đơn hàng</h3>
                    <p>${totalOrders}</p>
                </div>
                <div class="card products">
                    <div class="card-icon"><i class="fas fa-box"></i></div>
                    <h3>Sản phẩm</h3>
                    <p>${totalProducts != null ? totalProducts : '0'}</p>
                </div>
                <div class="card customers">
                    <div class="card-icon"><i class="fas fa-users"></i></div>
                    <h3>Khách hàng</h3>
                    <p>${totalUsers}</p>
                </div>
            </div>

            <div class="data-table">
                <h2>Đơn hàng gần đây</h2>
                <table>
                    <thead>
                    <tr>
                        <th>Mã Đơn</th>
                        <th>Ngày đặt</th>
                        <th>Tổng tiền</th>
                        <th>Phương thức</th>
                        <th>Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listOrders}" var="o" begin="0" end="4">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/></td>
                            <td>${o.paymentMethod}</td>
                            <td>
                                <span class="status ${o.status.toLowerCase()}">${o.status}</span>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-products" class="page-content">
            <div class="page-header">
                <h1>Quản lý Sản phẩm</h1>
                <button class="btn btn-primary" id="btn-add-product">
                    <i class="fas fa-plus"></i> Thêm sản phẩm mới
                </button>
            </div>

            <div class="data-table">
                <h2>Danh sách sản phẩm</h2>
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Ảnh</th>
                        <th>Tên Sản phẩm</th>
                        <th>Giá</th>
                        <th>Tồn kho</th>
                        <th>Loại</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listProducts}" var="p">
                        <tr>
                            <td>${p.id}</td>
                            <td><img src="${p.imageUrl}" alt="Product" class="product-image-small" onerror="this.src='https://via.placeholder.com/50'"></td>
                            <td>${p.name}</td>
                            <td><fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫"/></td>
                            <td>${p.stockQuantity}</td>
                            <td><span class="status ${p.luxury ? 'luxury' : 'normal'}">${p.luxury ? 'Luxury' : 'Thường'}</span></td>
                            <td>
                                <button class="btn-icon edit" onclick="editProduct(${p.id})"><i class="fas fa-edit"></i></button>
                                <button class="btn-icon delete" onclick="deleteProduct(${p.id})"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-orders" class="page-content">
            <div class="page-header">
                <h1>Quản lý Đơn hàng</h1>
            </div>

            <div class="data-table">
                <h2>Tất cả đơn hàng</h2>
                <table>
                    <thead>
                    <tr>
                        <th>Mã Đơn</th>
                        <th>Khách hàng ID</th>
                        <th>Ngày đặt</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listOrders}" var="o">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td>User #${o.userId}</td>
                            <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/></td>
                            <td><span class="status ${o.status.toLowerCase()}">${o.status}</span></td>
                            <td>
                                <button class="btn-icon view" onclick="location.href='order-detail?id=${o.orderId}'"><i class="fas fa-eye"></i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-banners" class="page-content">
            <div class="page-header">
                <h1>Quản lý Banner</h1>
                <button class="btn btn-primary" onclick="openAddBanner()"><i class="fas fa-plus"></i> Thêm Banner</button>
            </div>
            <div class="data-table">
                <table id="banner-sortable">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Ảnh</th>
                        <th>Link liên kết</th>
                        <th>Thời gian</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody id="banner-list-body">
                    <c:forEach items="${listBanners}" var="b">
                        <tr data-id="${b.id}">
                            <td><i class="fas fa-grip-vertical handle" style="cursor: move; color: #ccc;"></i></td>
                            <td><img src="${b.imageUrl}" width="150" style="border-radius: 5px;"></td>
                            <td>${b.linkUrl}</td>
                            <td style="font-size: 12px;">
                                S: <fmt:formatDate value="${b.startDate}" pattern="dd/MM/yyyy"/><br>
                                E: <fmt:formatDate value="${b.endDate}" pattern="dd/MM/yyyy"/>
                            </td>
                            <td><span class="status ${b.active ? 'active' : 'inactive'}">${b.active ? 'Hiển thị' : 'Ẩn'}</span></td>
                            <td>
                                <button class="btn-icon delete" onclick="deleteBanner(${b.id})"><i class="fas fa-trash"></i></button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-menus" class="page-content">
            <div class="page-header">
                <h1>Quản lý Menu chính</h1>
            </div>
            <div class="data-table">
                <table>
                    <thead>
                    <tr>
                        <th>Danh mục</th>
                        <th>Vị trí</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody id="menu-list-body">
                    <c:forEach items="${listCats}" var="c">
                        <c:if test="${c.parentId == 0}">
                            <tr data-id="${c.id}">
                                <td><strong>${c.name}</strong></td>
                                <td>${c.sortOrder}</td>
                                <td>
                                    <label class="switch">
                                        <input type="checkbox" ${c.active ? 'checked' : ''} onchange="toggleCat(${c.id}, this.checked)">
                                        <span class="slider round"></span>
                                    </label>
                                </td>
                                <td>
                                    <button class="btn-icon edit" onclick="editMenu(${c.id})"><i class="fas fa-edit"></i></button>
                                </td>
                            </tr>
                        </c:if>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-users" class="page-content">
            <div class="page-header">
                <h1>Quản lý Khách hàng</h1>
            </div>
            <div class="data-table">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Họ tên</th>
                        <th>Email</th>
                        <th>SĐT</th>
                        <th>Địa chỉ mặc định</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${listUsers}" var="u">
                        <tr>
                            <td>${u.id}</td>
                            <td>${u.fullName}</td>
                            <td>${u.email}</td>
                            <td>${u.phone}</td>
                            <td>${u.address}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div id="page-settings" class="page-content">
            <div class="page-header">
                <h1>Cài đặt hệ thống</h1>
            </div>
            <div class="card">
                <h2>Thông tin Cửa hàng</h2>
                <form class="form-grid" action="update-settings" method="POST">
                    <div class="form-group full-width">
                        <label>Tên cửa hàng</label>
                        <input type="text" name="storeName" value="VVP Luxury Watch">
                    </div>
                    <div class="form-group">
                        <label>Email liên hệ</label>
                        <input type="email" name="storeEmail" value="contact@vvpstore.vn">
                    </div>
                    <div class="form-group">
                        <label>Hotline</label>
                        <input type="tel" name="storePhone" value="0354 952 965">
                    </div>
                    <div class="form-actions">
                        <button type="submit" class="btn btn-primary"><i class="fas fa-save"></i> Cập nhật cấu hình</button>
                    </div>
                </form>
            </div>
        </div>

    </main>
</div>

<script src="${pageContext.request.contextPath}/assets/js/admin.js"></script>
<script>
    const bannerBody = document.getElementById('banner-list-body');
    if (bannerBody) {
        new Sortable(bannerBody, {
            handle: '.handle',
            animation: 150,
            onEnd: function () {
                let orders = [];
                bannerBody.querySelectorAll('tr').forEach((tr, index) => {
                    orders.push({id: tr.dataset.id, order: index + 1});
                });
                fetch('banner-manager?action=updateOrder', {
                    method: 'POST',
                    body: JSON.stringify(orders)
                });
            }
        });
    }
    function toggleCat(id, status) {
        location.href = 'category-manager?action=toggleStatus&id=' + id + '&status=' + status;
    }
</script>
</body>
</html>