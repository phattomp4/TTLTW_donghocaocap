<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết khách hàng: ${user.username} | VVP Admin</title>
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

        /* Stats Grid - CHỈ SỐ MUA SẮM */
        .stats-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 20px; margin-bottom: 30px; }
        .stat-card { background: white; padding: 20px; border-radius: 10px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); border-top: 4px solid #1b6e76; }
        .stat-card h4 { margin: 0; color: #777; font-size: 13px; text-transform: uppercase; }
        .stat-card .value { font-size: 22px; font-weight: bold; color: #333; margin: 10px 0; }
        .stat-card .footer { font-size: 12px; color: #888; }
        .text-success { color: #28a745; font-weight: bold; }
        .text-danger { color: #d0011b; font-weight: bold; }

        /* Layout chia cột */
        .profile-container { display: flex; gap: 30px; align-items: flex-start; }
        .profile-card { flex: 1; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }
        .history-card { flex: 2; background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }

        .avatar-large { width: 70px; height: 70px; background: #1b6e76; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 28px; font-weight: bold; color: white; margin: 0 auto 15px; }

        .info-row { margin-bottom: 12px; border-bottom: 1px solid #f0f0f0; padding-bottom: 8px; }
        .info-row strong { display: block; color: #777; font-size: 12px; margin-bottom: 2px; }
        .info-row span { font-size: 15px; color: #333; font-weight: 500; }

        /* Bảng lịch sử */
        table { width: 100%; border-collapse: collapse; margin-top: 15px; }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; font-size: 14px; }
        th { background: #f8f9fa; color: #555; text-transform: uppercase; font-size: 12px; }

        .status-badge { padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; }
        .status-Completed { color: #28a745; background: #e6f9e6; }
        .status-Cancelled { color: #d0011b; background: #ffe6e6; }
        .status-Pending { color: #f39c12; background: #fff5e6; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager" class="active"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div style="display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px;">
        <a href="user-manager" style="text-decoration: none; color: #1b6e76; font-weight: 500;">
            <i class="fa-solid fa-arrow-left"></i> Quay lại danh sách
        </a>
        <div class="user-actions">
            <c:if test="${user.status == 'Active' || user.status == '1'}">
                <button class="btn" style="background: #d0011b; color: white; border: none; padding: 8px 15px; border-radius: 5px; cursor: pointer;">
                    <i class="fa-solid fa-lock"></i> Khóa tài khoản
                </button>
            </c:if>
        </div>
    </div>

    <div class="stats-grid">
        <div class="stat-card">
            <h4>Tổng đơn hàng</h4>
            <div class="value">${stats.totalOrders} đơn</div>
            <div class="footer">Tất cả trạng thái</div>
        </div>
        <div class="stat-card">
            <h4>Tổng chi tiêu</h4>
            <div class="value" style="color: #d0011b;">
                <fmt:formatNumber value="${stats.totalSpent}" type="currency" currencySymbol="₫"/>
            </div>
            <div class="footer">Chỉ tính đơn Hoàn thành</div>
        </div>
        <div class="stat-card" style="border-top-color: ${(stats.cancelledCount / stats.totalOrders) > 0.3 ? '#d0011b' : '#28a745'}">
            <h4>Tỷ lệ hủy đơn</h4>
            <div class="value">
                <c:choose>
                    <c:when test="${stats.totalOrders > 0}">
                        <fmt:formatNumber value="${(stats.cancelledCount / stats.totalOrders) * 100}" maxFractionDigits="1"/>%
                    </c:when>
                    <c:otherwise>0%</c:otherwise>
                </c:choose>
            </div>
            <div class="footer">
                <span class="text-success">Nhận: ${stats.completedCount}</span> |
                <span class="text-danger">Hủy: ${stats.cancelledCount}</span>
            </div>
        </div>
    </div>

    <div class="profile-container">
        <div class="profile-card">
            <div class="avatar-large">
                ${user.username.substring(0, 1).toUpperCase()}
            </div>
            <h2 style="text-align: center; margin: 0; color: #1b6e76;">${user.username}</h2>
            <div style="text-align: center; margin-top: 5px;">
                <span class="status-badge status-${user.status == '1' || user.status == 'Active' ? 'Completed' : 'Cancelled'}">
                    ${user.status == '1' || user.status == 'Active' ? 'Đang hoạt động' : 'Đang bị khóa'}
                </span>
            </div>

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
            <div class="info-row">
                <strong>Ngày gia nhập:</strong>
                <span><fmt:formatDate value="${user.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
            </div>
        </div>

        <div class="history-card">
            <h3 style="margin-top: 0; color: #333; font-size: 18px;">
                <i class="fa-solid fa-clock-rotate-left"></i> Lịch sử đơn hàng
            </h3>

            <c:if test="${empty userOrders}">
                <div style="text-align: center; padding: 40px; color: #999;">
                    <i class="fa-solid fa-box-open" style="font-size: 40px; margin-bottom: 10px;"></i>
                    <p>Khách hàng này chưa có đơn hàng nào.</p>
                </div>
            </c:if>

            <c:if test="${not empty userOrders}">
                <table>
                    <thead>
                    <tr>
                        <th>Mã đơn</th>
                        <th>Ngày đặt</th>
                        <th>Tổng tiền</th>
                        <th>Trạng thái</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${userOrders}" var="o">
                        <tr>
                            <td><strong>#${o.orderId}</strong></td>
                            <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy"/></td>
                            <td style="color: #d0011b; font-weight: bold;">
                                <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/>
                            </td>
                            <td>
                                <span class="status-badge status-${o.status}">${o.status}</span>
                            </td>
                            <td>
                                <a href="order-detail?id=${o.orderId}" style="color: #1b6e76; font-size: 13px;">Chi tiết</a>
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