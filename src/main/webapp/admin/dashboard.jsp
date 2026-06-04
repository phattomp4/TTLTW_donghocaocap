<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Admin Dashboard | VVP</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 10; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; font-weight: 800; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 25px; width: calc(100% - 250px); box-sizing: border-box; }

        .dashboard-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 25px; }
        .filter-group { display: flex; gap: 10px; align-items: center; }
        .btn-filter { padding: 8px 16px; border: 1px solid #ddd; background: white; border-radius: 4px; cursor: pointer; text-decoration: none; color: #555; font-size: 14px; font-weight: 500; transition: 0.2s; }
        .btn-filter.active, .btn-filter:hover { background: #1b6e76; color: white; border-color: #1b6e76; }
        .btn-export { padding: 8px 16px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; display: flex; align-items: center; gap: 8px; text-decoration: none; font-size: 14px; transition: 0.2s; }
        .btn-export:hover { background: #218838; }

        /* Cập nhật Card Container sang Grid hỗ trợ click link */
        .card-container { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 25px; }
        .card { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); border-left: 5px solid #d0011b; position: relative; overflow: hidden; text-decoration: none; color: inherit; display: block; transition: transform 0.2s, box-shadow 0.2s; }

        /* Hiệu ứng nổi bật khi rê chuột vào Card (giống alert-box mẫu cũ) */
        .card:hover { transform: translateY(-3px); box-shadow: 0 5px 15px rgba(0,0,0,0.1); cursor: pointer; }

        .card h3 { margin: 0; color: #777; font-size: 13px; text-transform: uppercase; letter-spacing: 0.5px; display: flex; align-items: center; gap: 6px; }
        .card p { font-size: 26px; font-weight: 800; margin: 12px 0 0; color: #333; }
        .card .card-icon { position: absolute; right: 15px; bottom: 15px; font-size: 32px; color: rgba(0,0,0,0.05); }

        .charts-grid { display: grid; grid-template-columns: 2fr 1fr; gap: 25px; margin-bottom: 25px; }
        .chart-box { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
        .chart-box h3 { margin-top: 0; margin-bottom: 20px; font-size: 16px; color: #333; border-bottom: 1px solid #eee; padding-bottom: 10px; }

        .tables-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 25px; }
        .table-box { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
        .table-box h3 { margin-top: 0; margin-bottom: 15px; font-size: 16px; color: #333; display: flex; justify-content: space-between; align-items: center; border-bottom: 1px solid #eee; padding-bottom: 10px; }

        .admin-table { width: 100%; border-collapse: collapse; text-align: left; font-size: 14px; }
        .admin-table th { background: #f8f9fa; padding: 12px 10px; color: #555; font-weight: 600; border-bottom: 2px solid #eee; }
        .admin-table td { padding: 12px 10px; border-bottom: 1px solid #eee; color: #444; vertical-align: middle; }
        .admin-table tr:hover { background-color: #fcfcfc; }

        .badge { padding: 4px 8px; border-radius: 4px; font-size: 11px; font-weight: bold; text-transform: uppercase; display: inline-block; }
        .badge-pending { background: #fff3cd; color: #856404; }
        .badge-processing { background: #cce5ff; color: #004085; }
        .badge-cancel { background: #f8d7da; color: #721c24; animation: pulse 2s infinite; }
        .badge-danger { background: #dc3545; color: white; padding: 2px 6px; border-radius: 3px; font-size: 11px; }

        .btn-detail { padding: 5px 10px; background: #1b6e76; color: white; border: none; border-radius: 4px; cursor: pointer; font-size: 12px; text-decoration: none; }
        .btn-detail:hover { background: #145258; }

        @keyframes pulse {
            0% { opacity: 1; }
            50% { opacity: 0.6; }
            100% { opacity: 1; }
        }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard" class="active"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-list"></i> Danh mục & Menu</a>
    <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">

    <div id="chartDataBridge"
         data-line-labels="<c:out value='${chartLabels}' />"
         data-line-data="<c:out value='${chartData}' />"
         data-brand-labels="<c:out value='${brandLabels}' />"
         data-brand-data="<c:out value='${brandData}' />"
         style="display: none;">
    </div>

    <div class="dashboard-header">
        <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333; margin: 0; font-size: 24px;">Tổng quan hệ thống</h2>

        <div class="filter-group">
            <a href="dashboard?period=today" class="btn-filter ${param.period == 'today' ? 'active' : ''}">Hôm nay</a>
            <a href="dashboard?period=week" class="btn-filter ${param.period == 'week' ? 'active' : ''}">Tuần này</a>
            <a href="dashboard?period=month" class="btn-filter ${param.period == 'month' || empty param.period ? 'active' : ''}">Tháng này</a>
            <a href="dashboard?period=year" class="btn-filter ${param.period == 'year' ? 'active' : ''}">Năm nay</a>

            <a href="export-statistics?period=${not empty param.period ? param.period : 'month'}" class="btn-export">
                <i class="fa-solid fa-file-excel"></i> Xuất Báo Cáo
            </a>
        </div>
    </div>

    <div class="card-container">
        <div class="card" style="border-color: #28a745; transform: none; box-shadow: 0 2px 8px rgba(0,0,0,0.06); cursor: default;">
            <h3><i class="fa-solid fa-sack-dollar" style="color: #28a745;"></i> Doanh Thu</h3>
            <p><fmt:formatNumber value="${revenue != null ? revenue : 0}" type="currency" currencySymbol="₫"/></p>
            <i class="fa-solid fa-sack-dollar card-icon"></i>
        </div>

        <a href="order-manager?status=Pending&period=${not empty param.period ? param.period : 'month'}" class="card" style="border-color: #ffc107;">
            <h3><i class="fa-solid fa-bell" style="color: #ffc107;"></i> Đơn Cần Xử Lý</h3>
            <p>
                ${pendingProcessingCount != null ? pendingProcessingCount : 0}
                <c:if test="${cancelRequestsCount > 0}">
                    <span style="font-size: 13px; color: #dc3545; font-weight: normal; margin-left: 5px;">
                        (${cancelRequestsCount} khách đòi hủy <i class="fa-solid fa-triangle-exclamation"></i>)
                    </span>
                </c:if>
            </p>
            <i class="fa-solid fa-bell card-icon"></i>
        </a>

        <a href="warehouse?filter=lowstock" class="card" style="border-color: #fd7e14;">
            <h3><i class="fa-solid fa-boxes-stacked" style="color: #fd7e14;"></i> Sắp Hết Hàng</h3>
            <p style="color: ${lowStockCount > 0 ? '#dc3545' : '#333'}">
                ${lowStockCount != null ? lowStockCount : 0} <span style="font-size: 14px; font-weight: normal; color: #666;">mẫu (&lt; 3 chiếc)</span>
            </p>
            <i class="fa-solid fa-boxes-stacked card-icon"></i>
        </a>

        <a href="order-manager?status=All&period=${not empty param.period ? param.period : 'month'}" class="card" style="border-color: #17a2b8;">
            <h3><i class="fa-solid fa-cart-shopping" style="color: #17a2b8;"></i> Đơn Giai Đoạn</h3>
            <p>${totalOrders != null ? totalOrders : 0} <span style="font-size: 14px; font-weight: normal; color: #666;">đơn chốt</span></p>
            <i class="fa-solid fa-cart-shopping card-icon"></i>
        </a>
    </div>

    <div class="charts-grid">
        <div class="chart-box">
            <h3><i class="fa-solid fa-chart-line" style="color: #1b6e76;"></i> Xu Hướng Doanh Thu Tiêu Thụ</h3>
            <div style="position: relative; height:280px; width:100%">
                <canvas id="revenueChart"></canvas>
            </div>
        </div>

        <div class="chart-box">
            <h3><i class="fa-solid fa-chart-pie" style="color: #daa51e;"></i> Lợi Nhuận Theo Thương Hiệu</h3>
            <div style="position: relative; height:280px; width:100%; display: flex; justify-content: center;">
                <canvas id="brandPieChart"></canvas>
            </div>
        </div>
    </div>

    <div class="tables-grid">
        <div class="table-box">
            <h3>
                <span><i class="fa-solid fa-clock" style="color: #17a2b8; margin-right: 5px;"></i> Đơn hàng mới chốt</span>
                <a href="order-manager" style="font-size: 13px; color: #1b6e76; text-decoration: none;">Xem tất cả ></a>
            </h3>
            <table class="admin-table">
                <thead>
                <tr>
                    <th>Mã Đơn</th>
                    <th>Khách Hàng</th>
                    <th>Tổng Tiền</th>
                    <th>Trạng Thái</th>
                    <th style="text-align: center;">Hành Động</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${recentOrders}" var="o">
                    <tr>
                        <td><b>#${o.orderId}</b></td>
                        <td><c:out value="${o.transactionId}"/></td>
                        <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${o.status == 'Pending'}"><span class="badge badge-pending">Chờ Duyệt</span></c:when>
                                <c:when test="${o.status == 'Processing'}"><span class="badge badge-processing">Đang Xử Lý</span></c:when>
                                <c:when test="${o.status == 'Request Cancel'}"><span class="badge badge-cancel">Khách Đòi Hủy</span></c:when>
                                <c:otherwise><span class="badge" style="background:#eee; color:#666;"><c:out value="${o.status}"/></span></c:otherwise>
                            </c:choose>
                        </td>
                        <td style="text-align: center;">
                            <a href="order-detail?id=${o.orderId}" class="btn-detail" title="Duyệt / Xem chi tiết đơn">Xử lý</a>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty recentOrders}">
                    <tr><td colspan="5" style="text-align: center; color: #999; padding: 20px;">Không có đơn hàng mới phát sinh.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>

        <div class="table-box">
            <h3>
                <span><i class="fa-solid fa-fire" style="color: #d0011b; margin-right: 5px;"></i> Xu hướng mua sắm (Top 5 Bán Chạy)</span>
            </h3>
            <table class="admin-table">
                <thead>
                <tr>
                    <th style="text-align: center; width: 50px;">Hạng</th>
                    <th>Tên Sản Phẩm</th>
                    <th>Thương Hiệu</th>
                    <th style="text-align: center;">Đã Bán</th>
                    <th style="text-align: center;">Kho Còn</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${topSellingWatches}" var="w" varStatus="status">
                    <tr>
                        <td style="text-align: center;">
                            <b style="color: ${status.index == 0 ? '#daa51e' : '#666'}; font-size: 16px;">${status.index + 1}</b>
                        </td>
                        <td>
                            <span style="display: block; font-weight: 500; max-width: 180px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis;" title="<c:out value='${w.name}'/>">
                                <c:out value="${w.name}"/>
                            </span>
                        </td>
                        <td><span style="color: #1b6e76; font-weight: 500;"><c:out value="${w.description}"/></span></td>
                        <td style="text-align: center;"><b>${w.soldQuantity}</b> chiếc</td>
                        <td style="text-align: center;">
                            <c:choose>
                                <c:when test="${w.stockQuantity < 3}">
                                    <span class="badge badge-danger" title="Nguy cơ đứt hàng!">${w.stockQuantity} cạn kiệt</span>
                                </c:when>
                                <c:otherwise>
                                    ${w.stockQuantity} chiếc
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty topSellingWatches}">
                    <tr><td colspan="5" style="text-align: center; color: #999; padding: 20px;">Chưa có dữ liệu thống kê lượt mua.</td></tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
    // 1. ĐỌC VÀ CHUYỂN ĐỔI MẢNG TỪ CẦU NỐI HTML5
    const dataBridge = document.getElementById('chartDataBridge');

    const rawLineLabels = dataBridge.getAttribute('data-line-labels');
    const lineLabelsArr = rawLineLabels && rawLineLabels.trim() !== "" ? rawLineLabels.split(',') : [];

    const rawLineData = dataBridge.getAttribute('data-line-data');
    const lineDataArr = rawLineData && rawLineData.trim() !== "" ? rawLineData.split(',').map(Number) : [];

    const rawBrandLabels = dataBridge.getAttribute('data-brand-labels');
    const brandLabelsArr = rawBrandLabels && rawBrandLabels.trim() !== "" ? rawBrandLabels.split(',') : [];

    const rawBrandData = dataBridge.getAttribute('data-brand-data');
    const brandDataArr = rawBrandData && rawBrandData.trim() !== "" ? rawBrandData.split(',').map(Number) : [];

    // 2. BIỂU ĐỒ ĐƯỜNG
    const ctxRevenue = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctxRevenue, {
        type: 'line',
        data: {
            labels: lineLabelsArr,
            datasets: [{
                label: 'Doanh thu tiêu thụ (đ)',
                data: lineDataArr,
                borderColor: '#1b6e76',
                backgroundColor: 'rgba(27, 110, 118, 0.08)',
                borderWidth: 3,
                fill: true,
                tension: 0.3,
                pointBackgroundColor: '#1b6e76'
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: { legend: { display: false } },
            scales: {
                y: { beginAtZero: true, grid: { color: '#f0f0f0' } },
                x: { grid: { display: false } }
            }
        }
    });

    // 3. BIỂU ĐỒ TRÒN
    const ctxBrand = document.getElementById('brandPieChart').getContext('2d');
    new Chart(ctxBrand, {
        type: 'doughnut',
        data: {
            labels: brandLabelsArr,
            datasets: [{
                data: brandDataArr,
                backgroundColor: ['#d0011b', '#17a2b8', '#daa51e', '#28a745', '#6c757d', '#8e44ad', '#f39c12'],
                borderWidth: 2,
                hoverOffset: 6
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: { position: 'bottom', labels: { boxWidth: 12, padding: 15, font: { size: 12 } } }
            }
        }
    });
</script>

</body>
</html>