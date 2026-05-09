<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body style="background-color: #f4f6f9;">

<div class="container-fluid p-4">
    <h2 class="mb-4"><i class="fa-solid fa-chart-pie text-primary"></i> Tổng Quan Hệ Thống</h2>

    <div class="row mb-4">
        <div class="col-md-4">
            <div class="card text-white bg-warning shadow-sm">
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="card-title text-uppercase">Chờ Xác Nhận (Pending)</h6>
                        <h2 class="mb-0 fw-bold">${orderStats['Pending']}</h2>
                    </div>
                    <i class="fa-solid fa-hourglass-half fa-3x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-info shadow-sm">
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="card-title text-uppercase">Đang Xử Lý (Processing)</h6>
                        <h2 class="mb-0 fw-bold">${orderStats['Processing']}</h2>
                    </div>
                    <i class="fa-solid fa-box-open fa-3x opacity-50"></i>
                </div>
            </div>
        </div>
        <div class="col-md-4">
            <div class="card text-white bg-danger shadow-sm">
                <div class="card-body d-flex justify-content-between align-items-center">
                    <div>
                        <h6 class="card-title text-uppercase">Yêu Cầu Hủy (Cancel Request)</h6>
                        <h2 class="mb-0 fw-bold">${orderStats['Request Cancel']}</h2>
                    </div>
                    <i class="fa-solid fa-triangle-exclamation fa-3x opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-5 mb-4">
            <div class="card shadow-sm h-100 border-danger">
                <div class="card-header bg-white border-danger text-danger fw-bold">
                    <i class="fa-solid fa-bell"></i> Cảnh Báo Sắp Hết Hàng
                </div>
                <div class="card-body p-0">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>Sản phẩm</th>
                            <th class="text-center">Tồn kho</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${empty lowStockList}">
                            <tr><td colspan="2" class="text-center text-success py-3">Kho hàng đang ổn định!</td></tr>
                        </c:if>
                        <c:forEach items="${lowStockList}" var="p">
                            <tr>
                                <td>
                                    <div class="d-flex align-items-center">
                                        <img src="${p.imageUrl}" width="40" height="40" class="rounded me-2" style="object-fit:cover;">
                                        <span class="text-truncate" style="max-width: 200px;" title="${p.name}">${p.name}</span>
                                    </div>
                                </td>
                                <td class="text-center">
                                        <span class="badge ${p.stockQuantity == 0 ? 'bg-dark' : 'bg-danger'} rounded-pill" style="font-size: 14px;">
                                                ${p.stockQuantity}
                                        </span>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="col-md-7 mb-4">
            <div class="card shadow-sm h-100">
                <div class="card-header bg-white fw-bold">
                    <i class="fa-solid fa-cart-shopping text-success"></i> 5 Đơn Hàng Mới Nhất
                </div>
                <div class="card-body p-0 table-responsive">
                    <table class="table table-hover mb-0 align-middle">
                        <thead class="table-light">
                        <tr>
                            <th>Mã ĐH</th>
                            <th>Tổng Tiền</th>
                            <th>Trạng Thái</th>
                            <th class="text-center">Hành Động</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${recentOrders}" var="o">
                            <tr>
                                <td class="fw-bold">#ORD-${o.id}</td>
                                <td class="text-danger fw-bold">
                                    <fmt:formatNumber value="${o.totalMoney}" type="currency" currencySymbol="₫"/>
                                </td>
                                <td>
                                        <span class="badge
                                            ${o.status == 'Pending' ? 'bg-warning text-dark' :
                                              o.status == 'Processing' ? 'bg-info text-dark' :
                                              o.status == 'Delivered' ? 'bg-success' : 'bg-danger'}">
                                                ${o.status}
                                        </span>
                                </td>
                                <td class="text-center">
                                    <a href="order-detail?id=${o.id}" class="btn btn-sm btn-outline-primary">
                                        <i class="fa-solid fa-eye"></i> Chi tiết
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

</div>

</body>
</html>