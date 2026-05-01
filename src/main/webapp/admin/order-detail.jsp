<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết đơn hàng #${order.orderId} | Admin</title>
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

        /* Detail Box */
        .detail-box { background: white; padding: 20px; border-radius: 5px; box-shadow: 0 0 5px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .detail-row { display: flex; justify-content: space-between; border-bottom: 1px solid #eee; padding: 10px 0; }
        .detail-row:last-child { border-bottom: none; }

        /* Table Sản phẩm */
        table { width: 100%; border-collapse: collapse; margin-top: 10px; }
        th, td { padding: 10px; border: 1px solid #ddd; text-align: left; }
        th { background: #f8f9fa; }
        img { width: 50px; height: 50px; object-fit: cover; border: 1px solid #ddd; }

        .btn-back { display: inline-block; margin-bottom: 15px; text-decoration: none; color: #333; }
        .btn-back:hover { color: #d0011b; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard" class="active"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>

    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>

    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-list"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ</a>
</div>

<div class="content">
    <a href="dashboard" class="btn-back"><i class="fa-solid fa-arrow-left"></i> Quay lại danh sách</a>

    <h2 style="border-bottom: 2px solid #333; padding-bottom: 10px;">
        Chi tiết đơn hàng #${order.orderId} - <span style="color: blue;">${order.status}</span>
    </h2>

    <div class="detail-box">
        <h3>1. Thông tin người nhận & Thanh toán</h3>
        <div class="detail-row">
            <div>
                <strong>Người nhận:</strong> ${address.name}<br>
                <strong>SĐT:</strong> ${address.phone}<br>
                <strong>Địa chỉ:</strong> ${address.address} <c:if test="${not empty address.city}">- ${address.city}</c:if>
            </div>
            <div style="text-align: right;">
                <strong>Ngày đặt:</strong> <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/><br>
                <strong>Thanh toán:</strong> ${order.paymentMethod}<br>

                <strong>Tài khoản:</strong> <span style="color: #d0011b;">${customer.username}</span>
                <br>
                <small>(${customer.fullName})</small>
            </div>
        </div>
    </div>

    <div class="detail-box">
        <h3>2. Danh sách sản phẩm</h3>
        <table>
            <thead>
            <tr>
                <th>Ảnh</th>
                <th>Tên sản phẩm</th>
                <th>Giá mua</th>
                <th>Số lượng</th>
                <th>Thành tiền</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${details}" var="d">
                <tr>
                    <td><img src="${d.product.imageUrl}" onerror="this.src='https://via.placeholder.com/50'"></td>
                    <td>
                        <b>${d.product.name}</b><br>
                        <small style="color:#777">Mã SP: #${d.productId}</small>
                    </td>
                    <td><fmt:formatNumber value="${d.priceAtPurchase}" type="currency" currencySymbol="₫"/></td>
                    <td>x${d.quantity}</td>
                    <td style="color: #d0011b; font-weight: bold;">
                        <fmt:formatNumber value="${d.priceAtPurchase * d.quantity}" type="currency" currencySymbol="₫"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>

        <div style="text-align: right; margin-top: 20px; font-size: 18px;">
            Tổng tiền đơn hàng: <b style="color: #d0011b; font-size: 24px;">
            <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/>
        </b>
        </div>
    </div>

    <div class="detail-box" style="border-left: 5px solid #17a2b8;">
        <h3><i class="fa-solid fa-pen-to-square"></i> 3. Cập nhật trạng thái đơn hàng</h3>

        <c:choose>
            <c:when test="${order.status == 'Request Cancel'}">
                <div style="margin-top: 15px; padding: 15px; background: #fff3cd; border-left: 5px solid #ffc107; border-radius: 4px;">
                    <p style="margin: 0 0 15px 0; color: #856404; font-weight: bold;">
                        <i class="fa-solid fa-triangle-exclamation"></i> Khách hàng đang yêu cầu hủy đơn hàng này!
                    </p>
                    <div style="display: flex; gap: 10px;">
                        <a href="dashboard?action=approveCancel&id=${order.orderId}&source=detail_page"
                           style="background: #28a745; text-decoration: none; padding: 8px 15px; color: white; border-radius: 4px; display: inline-block; font-weight: bold;"
                           onclick="return confirm('Xác nhận hủy đơn và hoàn kho?');">
                            <i class="fa-solid fa-check"></i> Duyệt hủy
                        </a>
                        <a href="dashboard?action=rejectCancel&id=${order.orderId}&source=detail_page"
                           style="background: #dc3545; text-decoration: none; padding: 8px 15px; color: white; border-radius: 4px; display: inline-block; font-weight: bold;"
                           onclick="return confirm('Từ chối yêu cầu hủy này?');">
                            <i class="fa-solid fa-xmark"></i> Từ chối
                        </a>
                    </div>
                </div>
            </c:when>

            <c:otherwise>
                <form action="dashboard" method="POST" style="display: flex; gap: 15px; align-items: center; margin-top: 15px;">
                    <input type="hidden" name="action" value="update_status">
                    <input type="hidden" name="orderId" value="${order.orderId}">

                    <input type="hidden" name="source" value="detail_page">

                    <select name="status" style="padding: 8px 12px; border-radius: 4px; border: 1px solid #ccc; font-size: 14px; min-width: 200px;">
                        <option value="Pending" ${order.status == 'Pending' ? 'selected' : ''}>Chờ duyệt</option>
                        <option value="Processing" ${order.status == 'Processing' ? 'selected' : ''}>Đang chuẩn bị</option>
                        <option value="Shipping" ${order.status == 'Shipping' ? 'selected' : ''}>Đang giao</option>
                        <option value="Completed" ${order.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                        <option value="Cancelled" ${order.status == 'Cancelled' ? 'selected' : ''}>Hủy đơn</option>
                    </select>

                    <button type="submit" style="background: #28a745; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; font-weight: bold;">
                        <i class="fa-solid fa-floppy-disk"></i> Lưu thay đổi
                    </button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>

</div>

</body>
</html>