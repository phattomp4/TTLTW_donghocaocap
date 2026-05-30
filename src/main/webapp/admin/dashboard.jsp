<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | VVP</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        /* Sidebar */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        /* Content */
        .content { margin-left: 250px; padding: 20px; width: 100%; }

        /* Stats Cards */
        .card-container { display: flex; gap: 20px; margin-bottom: 30px; }
        .card { flex: 1; background: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-left: 5px solid #d0011b; }
        .card h3 { margin: 0; color: #666; font-size: 14px; }
        .card p { font-size: 24px; font-weight: bold; margin: 10px 0 0; color: #333; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard" class="active"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager" ><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333; margin-bottom: 25px;">Tổng quan hệ thống</h2>

    <div class="card-container">
        <div class="card" style="border-color: #28a745;">
            <h3><i class="fa-solid fa-sack-dollar"></i> TỔNG DOANH THU</h3>
            <p><fmt:formatNumber value="${revenue}" pattern="#,##0 ₫"/></p>
        </div>
        <div class="card" style="border-color: #17a2b8;">
            <h3><i class="fa-solid fa-cart-shopping"></i> TỔNG ĐƠN HÀNG</h3>
            <p>${totalOrders}</p>
        </div>
        <div class="card" style="border-color: #ffc107;">
            <h3><i class="fa-solid fa-users"></i> KHÁCH HÀNG</h3>
            <p>${totalUsers}</p>
        </div>
    </div>

    <div style="background: white; padding: 30px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1); text-align: center; color: #666; margin-top: 20px;">
        <i class="fa-solid fa-chart-line" style="font-size: 48px; color: #1b6e76; margin-bottom: 15px;"></i>
        <p style="margin: 0; font-size: 16px;">Chào mừng bạn đến với hệ thống quản trị VVP Store. Chọn các mục ở thanh menu bên trái để quản lý dữ liệu.</p>
    </div>
</div>

</body>
</html>