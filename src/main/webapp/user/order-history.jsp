<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Lịch sử đơn hàng | VVP Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        .order-container {
            max-width: 1000px;
            margin: 5px auto 5px;
            padding: 20px;
            background: #fff;
            min-height: 500px;
        }
        .order-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        .order-table th {
            background: #f8f8f8;
            padding: 15px;
            text-align: left;
            border-bottom: 2px solid #ddd;
            font-weight: bold;
            color: #333;
        }
        .order-table td {
            padding: 15px;
            border-bottom: 1px solid #eee;
            vertical-align: middle;
        }
        .status-badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: bold;
            text-transform: uppercase;
        }

        .status-pending {
            background: #fff3cd;
            color: #856404;
        }
        .status-processing {
            background: #cce5ff;
            color: #004085;
        }
        .status-shipping {
            background: #d1ecf1;
            color: #0c5460;
        }
        .status-completed {
            background: #d4edda;
            color: #155724;
        }
        .status-cancelled {
            background: #f8d7da;
            color: #721c24;
        }
        .btn-view {
            padding: 6px 15px;
            border: 1px solid #1b6e76;
            color: #1b6e76;
            text-decoration: none;
            border-radius: 4px;
            font-size: 13px;
            transition: 0.2s;
        }
        .btn-view:hover {
            background: #1b6e76;
            color: #fff;
        }
        .order-tabs {
            display: flex;
            margin-bottom: 20px;
            border-bottom: 2px solid #ddd;
            width: 100%;
        }

        .order-tabs a {
            flex: 1;
            text-align: center;
            padding: 12px 5px;
            text-decoration: none;
            color: #555;
            font-weight: bold;
            border-bottom: 3px solid transparent;
            transition: 0.3s;
            font-size: 14px;
        }

        .order-tabs a:hover {
            color: #1b6e76;
            background-color: #f9f9f9;
        }

        .order-tabs a.active {
            color: #1b6e76;
            border-bottom: 3px solid #1b6e76;
        }

        .pagination {
            display: flex;
            justify-content: center;
            margin-top: 20px;
            gap: 5px;
        }
        .pagination a {
            padding: 8px 12px;
            border: 1px solid #ddd;
            text-decoration: none;
            color: #333;
            border-radius: 4px;
        }
        .pagination a.active, .pagination a:hover {
            background: #1b6e76;
            color: white;
            border-color: #1b6e76;
        }
    </style>
</head>
<body>

<jsp:include page="../WEB-INF/tags/header.jsp" />

<div class="order-container">
    <h2 style="border-left: 5px solid #1b6e76; padding-left: 10px;">Lịch sử đơn hàng</h2>
    <div class="order-tabs">
        <a href="order-history?status=all" class="${currentStatus == 'all' ? 'active' : ''}">Tất cả</a>
        <a href="order-history?status=Pending" class="${currentStatus == 'Pending' ? 'active' : ''}">Chờ xác nhận</a>
        <a href="order-history?status=Processing" class="${currentStatus == 'Processing' ? 'active' : ''}">Đang chuẩn bị</a>
        <a href="order-history?status=Shipping" class="${currentStatus == 'Shipping' ? 'active' : ''}">Đang giao</a>
        <a href="order-history?status=Completed" class="${currentStatus == 'Completed' ? 'active' : ''}">Đã giao</a>
        <a href="order-history?status=Cancelled" class="${currentStatus == 'Cancelled' ? 'active' : ''}">Đã hủy</a>
    </div>
    <c:if test="${empty listOrders}">
        <div style="text-align: center; padding: 50px; color: #777;">
            <i class="fa-solid fa-box-open" style="font-size: 50px; margin-bottom: 15px;"></i>
            <p>Bạn chưa có đơn hàng nào.</p>
            <a href="${pageContext.request.contextPath}/home" style="color: #d0011b; font-weight: bold;">Mua sắm ngay</a>
        </div>
    </c:if>

    <c:if test="${not empty listOrders}">
        <table class="order-table">
            <thead>
            <tr>
                <th>Mã đơn</th>
                <th>Ngày đặt</th>
                <th>Tổng tiền</th>
                <th>Trạng thái</th>
                <th>Thao tác</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${listOrders}" var="o">
                <tr>
                    <td><b>#${o.orderId}</b></td>
                    <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                    <td style="color: #d0011b; font-weight: bold;">
                        <fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/>
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${o.status == 'Pending'}">
                                <span class="status-badge status-pending">Chờ xác nhận</span>
                            </c:when>
                            <c:when test="${o.status == 'Processing'}">
                                <span class="status-badge status-processing">Đang chuẩn bị hàng</span>
                            </c:when>
                            <c:when test="${o.status == 'Shipping'}">
                                <span class="status-badge status-shipping">Đang giao hàng</span>
                            </c:when>
                            <c:when test="${o.status == 'Completed'}">
                                <span class="status-badge status-completed">Giao thành công</span>
                            </c:when>
                            <c:when test="${o.status == 'Cancelled'}">
                                <span class="status-badge status-cancelled">Đã hủy</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-badge">${o.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:if test="${o.status == 'Pending' || o.status == 'Processing'}">
                            <a href="order-history?action=requestCancel&id=${o.orderId}&status=${currentStatus}&page=${currentPage}"
                               class="btn-view"
                               style="margin-right: 5px; text-decoration: none;"
                               onclick="return confirm('Bạn có chắc muốn yêu cầu hủy đơn này?');">
                                Hủy đơn
                            </a>
                        </c:if>

                        <c:if test="${o.status == 'Request Cancel'}">
        <span style="color: #e67e22; font-size: 13px; font-weight: bold; margin-right: 5px;">
            <i class="fa-regular fa-clock"></i> Đang chờ duyệt hủy
        </span>
                        </c:if>

                        <a href="order-detail?id=${o.orderId}" class="btn-view">Chi tiết</a>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <c:if test="${totalPages > 1}">
            <div class="pagination">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="order-history?status=${currentStatus}&page=${i}"
                       class="${i == currentPage ? 'active' : ''}">
                            ${i}
                    </a>
                </c:forEach>
            </div>
        </c:if>

    </c:if> </div>

<jsp:include page="../WEB-INF/tags/footer.jsp" />

</body>
</html>