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

        /* Status Form & Search */
        select, input[type="text"] { padding: 6px 12px; border-radius: 4px; border: 1px solid #ccc; outline: none; }
        .btn-update { background: #28a745; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }

        .search-container { position: relative; display: inline-block; }
        .search-container i { position: absolute; left: 10px; top: 50%; transform: translateY(-50%); color: #888; }
        .search-container input { padding-left: 32px; width: 220px; }
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

    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #333; padding-bottom: 10px; margin-bottom: 15px;">
        <h2 style="margin: 0; border: none; padding: 0;">Quản lý đơn hàng</h2>

        <div style="display: flex; align-items: center; gap: 15px;">
            <div class="search-container">
                <i class="fa-solid fa-magnifying-glass"></i>
                <input type="text" id="searchInput" onkeyup="filterOrders()" placeholder="Nhập mã đơn hoặc SĐT...">
            </div>

            <div style="display: flex; align-items: center; gap: 8px;">
                <label for="statusFilter" style="font-weight: bold; color: #555; margin: 0;">
                    <i class="fa-solid fa-filter"></i> Lọc:
                </label>
                <select id="statusFilter" onchange="filterOrders()" style="cursor: pointer;">
                    <option value="all">Tất cả đơn hàng</option>
                    <option value="Pending">Chờ duyệt</option>
                    <option value="Processing">Đang chuẩn bị</option>
                    <option value="Shipping">Đang giao</option>
                    <option value="Completed">Hoàn thành</option>
                    <option value="Cancelled">Hủy đơn</option>
                    <option value="Request Cancel">Yêu cầu hủy</option>
                </select>
            </div>
        </div>
    </div>

    <table>
        <thead>
        <tr>
            <th>ID</th>
            <th>Ngày đặt</th>
            <th>Khách hàng</th>
            <th>Tổng tiền</th>
            <th>Thanh toán</th>
            <th>Trạng thái hiện tại</th>
            <th>Cập nhật trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listOrders}" var="o">
            <tr class="order-row" data-status="${o.status}" data-id="${o.orderId}" data-phone="${o.phone}">
                <td><b>#${o.orderId}</b></td>
                <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>
                    User #${o.userId}
                    <br><small style="color: #666;"><i class="fa-solid fa-phone" style="font-size: 10px;"></i> ${o.phone}</small>
                </td>
                <td style="color: #d0011b; font-weight: bold;">
                    <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/>
                </td>
                <td>${o.paymentMethod}</td>
                <td>
                    <span style="font-weight: bold; color: ${o.status == 'Completed' ? 'green' : (o.status == 'Cancelled' ? 'red' : 'blue')}">
                            ${o.status}
                    </span>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${o.status == 'Request Cancel'}">
                            <div style="display: flex; gap: 5px; margin-bottom: 5px;">
                                <a href="${pageContext.request.contextPath}/admin/dashboard?action=approveCancel&id=${o.orderId}"
                                   class="btn-update" style="background: #28a745; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px;"
                                   onclick="return confirm('Xác nhận hủy đơn và hoàn kho?');">
                                    <i class="fa-solid fa-check"></i> Duyệt
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/dashboard?action=rejectCancel&id=${o.orderId}"
                                   class="btn-update" style="background: #dc3545; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px;"
                                   onclick="return confirm('Từ chối hủy yêu cầu này?');">
                                    <i class="fa-solid fa-xmark"></i> Từ chối
                                </a>
                            </div>
                            <span style="font-size: 12px; color: orange;">Khách yêu cầu hủy</span>
                        </c:when>

                        <c:otherwise>
                            <form action="dashboard" method="POST" style="display: flex; gap: 5px;">
                                <input type="hidden" name="action" value="update_status">
                                <input type="hidden" name="orderId" value="${o.orderId}">

                                <select name="status">
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
    </table> </div> <script>
    function filterOrders() {
        const selectedStatus = document.getElementById('statusFilter').value;
        const searchText = document.getElementById('searchInput').value.toLowerCase().trim();
        const rows = document.querySelectorAll('.order-row');

        rows.forEach(row => {
            const rowStatus = row.getAttribute('data-status');
            const rowId = row.getAttribute('data-id').toLowerCase();
            const rowPhone = (row.getAttribute('data-phone') || "").toLowerCase();

            // Lọc trạng thái
            const matchStatus = (selectedStatus === 'all' || rowStatus === selectedStatus);

            // Tìm kiếm ID hoặc Số điện thoại
            const matchSearch = (searchText === '' || rowId.includes(searchText) || rowPhone.includes(searchText));

            if (matchStatus && matchSearch) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }
</script>

</body>
</html>