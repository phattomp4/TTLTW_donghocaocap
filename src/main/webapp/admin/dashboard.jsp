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

        /* Order Table */
        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
        th { background: #343a40; color: white; }

        /* Status Form */
        select { padding: 5px; border-radius: 4px; border: 1px solid #ddd; }
        .btn-update { background: #28a745; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard" class="active"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager" ><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <h1>Tổng quan hệ thống</h1>

    <div class="card-container">
        <div class="card" style="border-color: #28a745;">
            <h3><i class="fa-solid fa-sack-dollar"></i> TỔNG DOANH THU</h3>
            <p><fmt:formatNumber value="${revenue}" type="currency" currencySymbol="₫"/></p>
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

    <h2 style="border-bottom: 2px solid #333; padding-bottom: 10px;">Quản lý đơn hàng</h2>
    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Ngày đặt</th>
            <th>Khách hàng (ID)</th>
            <th>Tổng tiền</th>
            <th>Thanh toán</th>
            <th>Trạng thái hiện tại</th>
            <th>Cập nhật trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listOrders}" var="o">
            <tr>
                <td><b>#${o.orderId}</b></td>
                <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>User #${o.userId}</td>
                <td style="color: #d0011b; font-weight: bold;">
                    <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/>
                </td>
                <td>${o.paymentMethod}</td>
                <td>
                            <span style="font-weight: bold;
                                color: ${o.status == 'Completed' ? 'green' : (o.status == 'Cancelled' ? 'red' : 'blue')}">
                                    ${o.status}
                            </span>
                </td>
                <td>
                    <c:choose>
                        <%-- TRƯỜNG HỢP 1: Đơn đang yêu cầu hủy -> Hiện nút Duyệt/Từ chối --%>
                        <c:when test="${o.status == 'Request Cancel'}">
                            <div style="display: flex; gap: 5px; margin-bottom: 5px;">
                                <a href="${pageContext.request.contextPath}/admin/dashboard?action=approveCancel&id=${o.orderId}"
                                   class="btn-update"
                                   style="background: #28a745; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px;"
                                   onclick="return confirm('Xác nhận hủy đơn và hoàn kho?');">
                                    <i class="fa-solid fa-check"></i> Duyệt
                                </a>

                                <a href="${pageContext.request.contextPath}/admin/dashboard?action=rejectCancel&id=${o.orderId}"
                                   class="btn-update"
                                   style="background: #dc3545; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px;"
                                   onclick="return confirm('Từ chối hủy yêu cầu này?');">
                                    <i class="fa-solid fa-xmark"></i> Từ chối
                                </a>
                            </div>
                            <span style="font-size: 12px; color: orange;">Khách yêu cầu hủy</span>
                        </c:when>

                        <%-- TRƯỜNG HỢP 2: Các trạng thái khác -> Hiện Dropdown như cũ --%>
                        <c:otherwise>
                            <form action="dashboard" method="POST" style="display: flex; gap: 5px;">
                                <input type="hidden" name="action" value="update_status">
                                <input type="hidden" name="orderId" value="${o.orderId}">

                                <select name="status" style="padding: 5px; border-radius: 4px; border: 1px solid #ddd;">
                                    <option value="Pending" ${o.status == 'Pending' ? 'selected' : ''}>Chờ duyệt</option>
                                    <option value="Processing" ${o.status == 'Processing' ? 'selected' : ''}>Đang chuẩn bị</option>
                                    <option value="Shipping" ${o.status == 'Shipping' ? 'selected' : ''}>Đang giao</option>
                                    <option value="Completed" ${o.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                                    <option value="Cancelled" ${o.status == 'Cancelled' ? 'selected' : ''}>Hủy đơn</option>
                                </select>
                                <button type="submit" class="btn-update"><i class="fa-solid fa-floppy-disk"></i></button>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <a href="order-detail?id=${o.orderId}" style="display: block; margin-top: 5px; text-decoration: none; color: #007bff; font-size: 13px;">
                        <i class="fa-solid fa-eye"></i> Xem chi tiết
                    </a>
                </td>

            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

</body>
</html>