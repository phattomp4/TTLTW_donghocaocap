<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết khách hàng: ${user.username} | Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        /* Sidebar */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }
        .content { margin-left: 250px; padding: 30px; width: 100%; }

        /* Layout chia cột */
        .profile-container { display: flex; gap: 30px; }
        .profile-card { flex: 1; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05); height: fit-content; }
        .history-card { flex: 2; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }

        /* Avatar to */
        .avatar-large { width: 80px; height: 80px; background: #eee; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 30px; font-weight: bold; color: #555; margin: 0 auto 20px; border: 3px solid #ddd; }

        .info-row { margin-bottom: 15px; border-bottom: 1px solid #eee; padding-bottom: 10px; }
        .info-row strong { display: block; color: #555; font-size: 13px; margin-bottom: 3px; }
        .info-row span { font-size: 16px; color: #333; font-weight: 500; }

        /* Bảng lịch sử */
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { padding: 10px; text-align: left; border-bottom: 1px solid #eee; font-size: 14px; }
        th { background: #f8f9fa; color: #555; }

        .status-badge { padding: 3px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; }
        .status-Completed { color: green; background: #e6f9e6; }
        .status-Cancelled { color: red; background: #ffe6e6; }
        .status-Processing { color: blue; background: #e6f2ff; }
        .status-Pending { color: orange; background: #fff5e6; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager" class="active"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>

    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <a href="user-manager" style="text-decoration: none; color: #666; margin-bottom: 20px; display: inline-block;">
        <i class="fa-solid fa-arrow-left"></i> Quay lại danh sách
    </a>

    <div class="profile-container">
        <div class="profile-card">
            <div class="avatar-large">
                ${user.username.substring(0, 1).toUpperCase()}
            </div>
            <h2 style="text-align: center; margin: 0 0 5px; color: #1b6e76;">${user.username}</h2>
            <p style="text-align: center; color: #777; margin-top: 0;">Khách hàng</p>
            <hr style="border: 0; border-top: 1px solid #eee; margin: 20px 0;">

            <div class="info-row">
                <strong>Họ và tên:</strong>
                <span>${user.fullName}</span>
            </div>
            <div class="info-row">
                <strong>Email:</strong>
                <span>${user.email}</span>
            </div>
            <div class="info-row">
                <strong>Số điện thoại:</strong>
                <span>${user.phone}</span>
            </div>
            <div class="info-row" style="border-bottom: none;">
                <strong>Danh sách địa chỉ (${listAddress.size()}):</strong>

                <c:if test="${empty listAddress}">
                    <span style="color: #999; font-style: italic;">User chưa lưu địa chỉ nào.</span>
                </c:if>

                <c:if test="${not empty listAddress}">
                    <div style="margin-top: 10px; display: flex; flex-direction: column; gap: 10px;">
                        <c:forEach items="${listAddress}" var="addr">
                            <div style="background: #f8f9fa; padding: 10px; border-radius: 5px; border: 1px solid #eee; font-size: 13px; position: relative;">

                                <c:if test="${addr.defaultAddress}">
                                    <span style="position: absolute; top: 5px; right: 5px; font-size: 10px; background: #d0011b; color: white; padding: 2px 5px; border-radius: 3px;">Mặc định</span>
                                </c:if>

                                <div style="font-weight: bold; color: #333; margin-bottom: 2px;">
                                        ${addr.name} - ${addr.phone}
                                </div>
                                <div style="color: #555;">
                                        ${addr.address} <c:if test="${not empty addr.city}">, ${addr.city}</c:if>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="history-card">
            <h3 style="margin-top: 0; border-bottom: 2px solid #1b6e76; padding-bottom: 10px; display: inline-block;">
                Lịch sử mua hàng (${userOrders.size()} đơn)
            </h3>

            <c:if test="${empty userOrders}">
                <p style="color: #777; font-style: italic; margin-top: 20px;">Khách hàng này chưa có đơn hàng nào.</p>
            </c:if>

            <c:if test="${not empty userOrders}">
                <table>
                    <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Ngày đặt</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Chi tiết</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userOrders}" var="o">
                        <tr>
                            <td>#${o.orderId}</td>
                            <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/></td>
                            <td style="color: #d0011b; font-weight: bold;">
                                <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/>
                            </td>
                            <td>
                                <span class="status-badge status-${o.status}">${o.status}</span>
                            </td>
                            <td>
                                <a href="order-detail?id=${o.orderId}" style="color: #007bff; text-decoration: none;">Xem</a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</div>

</body>
</html>