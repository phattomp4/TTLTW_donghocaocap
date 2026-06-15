<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Đơn hàng | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100;}
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }
        .content { margin-left: 250px; padding: 20px; width: 100%; box-sizing: border-box;}

        table { width: 100%; border-collapse: collapse; background: white; box-shadow: 0 0 10px rgba(0,0,0,0.1); border-radius: 8px; overflow: hidden;}
        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle;}
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 13px; letter-spacing: 0.5px;}
        tr:hover { background-color: #fcfcfc; }

        select, input[type="text"] { padding: 6px 12px; border-radius: 4px; border: 1px solid #ccc; outline: none; }
        .btn-update { background: #1b6e76; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; transition: 0.2s;}
        .btn-update:hover { background: #145258; }

        .search-container { position: relative; display: inline-block; }
        .search-container i { position: absolute; left: 10px; top: 50%; transform: translateY(-50%); color: #888; }
        .search-container input { padding-left: 32px; width: 220px; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>

    <a href="order-manager"   class="active"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>

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
    <div style="display: flex; justify-content: space-between; align-items: center; border-bottom: 2px solid #1b6e76; padding-bottom: 10px; margin-bottom: 20px;">
        <h2 style="margin: 0; border-left: 5px solid #1b6e76; padding-left: 15px; color: #333;">Quản lý danh sách đơn hàng</h2>

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

    <c:if test="${not empty param.msg}">
        <div style="padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold;
                    ${param.msg == 'success' ? 'background: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : 'background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;'}">
            <c:choose>
                <c:when test="${param.msg == 'success'}"><i class="fa-solid fa-circle-check"></i> Cập nhật trạng thái đơn hàng thành công!</c:when>
                <c:otherwise><i class="fa-solid fa-circle-xmark"></i> Thao tác thất bại. Vui lòng thử lại!</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <table>
        <thead>
        <tr>
            <th style="width: 70px;">ID</th>
            <th>Ngày đặt</th>
            <th>Khách hàng</th>
            <th>Tổng tiền</th>
            <th>Thanh toán</th>
            <th>Trạng thái hiện tại</th>
            <th style="width: 220px;">Cập nhật trạng thái</th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${empty listOrders}">
            <tr>
                <td colspan="7" style="text-align: center; padding: 40px; color: #777;">
                    <i class="fa-solid fa-box-open" style="font-size: 30px; margin-bottom: 10px; display: block;"></i>
                    Hiện tại không có đơn hàng nào trong hệ thống.
                </td>
            </tr>
        </c:if>

        <c:forEach items="${listOrders}" var="o">
            <tr class="order-row" data-status="${o.status}" data-id="${o.orderId}" data-phone="${o.phone}">
                <td><b style="color: #1b6e76;">#${o.orderId}</b></td>
                <td style="font-size: 13px;"><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                <td>
                    <span style="font-weight: 500;">User #${o.userId}</span>
                    <br><small style="color: #666;"><i class="fa-solid fa-phone" style="font-size: 10px;"></i> ${o.phone}</small>
                </td>
                <td style="color: #d0011b; font-weight: bold;">
                    <fmt:formatNumber value="${o.totalAmount}" pattern="#,##0 ₫"/>
                </td>
                <td style="font-size: 13px;">${o.paymentMethod}</td>
                <td>
                    <span style="font-weight: bold; padding: 4px 8px; border-radius: 4px; font-size: 12px;
                        ${o.status == 'Completed' ? 'background: #d4edda; color: #155724;' :
                         (o.status == 'Cancelled' ? 'background: #f8d7da; color: #721c24;' :
                         (o.status == 'Request Cancel' ? 'background: #fff3cd; color: #856404;' : 'background: #e2e3e5; color: #383d41;'))}">
                            ${o.status}
                    </span>
                </td>
                <td>
                    <c:choose>
                        <%-- TRƯỜNG HỢP: KHÁCH YÊU CẦU HỦY ĐƠN --%>
                        <c:when test="${o.status == 'Request Cancel'}">
                            <c:choose>
                                <%-- Admin được quyền Duyệt/Từ chối --%>
                                <c:when test="${sessionScope.acc.role == 'Admin'}">
                                    <div style="display: flex; gap: 5px; margin-bottom: 5px;">
                                        <a href="${pageContext.request.contextPath}/admin/order-manager?action=approveCancel&id=${o.orderId}"
                                           class="btn-update" style="background: #28a745; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px; font-weight: bold; font-size: 12px;"
                                           onclick="return confirm('Xác nhận hủy đơn và hoàn kho?');">
                                            <i class="fa-solid fa-check"></i> Duyệt
                                        </a>
                                        <a href="${pageContext.request.contextPath}/admin/order-manager?action=rejectCancel&id=${o.orderId}"
                                           class="btn-update" style="background: #dc3545; text-decoration: none; padding: 5px 10px; color: white; border-radius: 4px; font-weight: bold; font-size: 12px;"
                                           onclick="return confirm('Từ chối yêu cầu hủy này?');">
                                            <i class="fa-solid fa-xmark"></i> Từ chối
                                        </a>
                                    </div>
                                    <span style="font-size: 12px; color: #d0011b; font-weight: 500;"><i class="fa-solid fa-circle-exclamation"></i> Khách yêu cầu hủy</span>
                                </c:when>
                                <%-- Staff chỉ được xem, không được duyệt --%>
                                <c:otherwise>
                                    <span style="font-size: 12px; color: #dc3545; font-weight: bold; display: block; margin-bottom: 5px;"><i class="fa-solid fa-lock"></i> Chờ Admin phê duyệt</span>
                                    <span style="font-size: 12px; color: #856404;">Khách yêu cầu hủy</span>
                                </c:otherwise>
                            </c:choose>
                        </c:when>

                        <%-- TRƯỜNG HỢP: CẬP NHẬT TRẠNG THÁI BÌNH THƯỜNG --%>
                        <c:otherwise>
                            <form action="order-manager" method="POST" style="display: flex; gap: 5px;">
                                <input type="hidden" name="action" value="update_status">
                                <input type="hidden" name="orderId" value="${o.orderId}">

                                <select name="status" style="flex: 1;">
                                    <option value="Pending" ${o.status == 'Pending' ? 'selected' : ''}>Chờ duyệt</option>
                                    <option value="Processing" ${o.status == 'Processing' ? 'selected' : ''}>Đang chuẩn bị</option>
                                    <option value="Shipping" ${o.status == 'Shipping' ? 'selected' : ''}>Đang giao</option>
                                    <option value="Completed" ${o.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>

                                        <%-- Chỉ hiển thị tùy chọn 'Hủy đơn' nếu là Admin hoặc đơn đã bị hủy từ trước --%>
                                    <c:if test="${sessionScope.acc.role == 'Admin' || o.status == 'Cancelled'}">
                                        <option value="Cancelled" ${o.status == 'Cancelled' ? 'selected' : ''}>Hủy đơn</option>
                                    </c:if>
                                </select>
                                <button type="submit" class="btn-update" title="Lưu thay đổi"><i class="fa-solid fa-floppy-disk"></i></button>
                            </form>
                        </c:otherwise>
                    </c:choose>

                    <a href="order-detail?id=${o.orderId}" style="display: inline-block; margin-top: 8px; text-decoration: none; color: #1b6e76; font-size: 13px; font-weight: bold;">
                        <i class="fa-solid fa-arrow-right-to-bracket"></i> Xem chi tiết
                    </a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function filterOrders() {
        const selectedStatus = document.getElementById('statusFilter').value;
        const searchText = document.getElementById('searchInput').value.toLowerCase().trim();
        const rows = document.querySelectorAll('.order-row');

        rows.forEach(row => {
            const rowStatus = row.getAttribute('data-status');
            const rowId = row.getAttribute('data-id').toLowerCase();
            const rowPhone = (row.getAttribute('data-phone') || "").toLowerCase();

            const matchStatus = (selectedStatus === 'all' || rowStatus === selectedStatus);
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