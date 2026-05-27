<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản Lý Kho Hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body style="background-color: #f4f6f9;">

<div class="container-fluid p-4">
    <h2 class="mb-4"><i class="fa-solid fa-boxes-stacked text-primary"></i> Quản Lý Kho</h2>

    <ul class="nav nav-tabs mb-4" id="warehouseTabs">
        <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" href="#inventory">Bảng Tồn Kho</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#import">Nhập Kho</a></li>
        <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#history">Lịch Sử Biến Động</a></li>
    </ul>

    <div class="tab-content bg-white p-4 shadow-sm rounded border border-top-0">

        <div class="tab-pane fade show active" id="inventory">
            <table class="table table-bordered table-hover align-middle">
                <thead class="table-light">
                <tr>
                    <th>Mã SP</th><th>Hình ảnh</th><th>Tên đồng hồ</th>
                    <th class="text-center">Số lượng tồn</th><th>Trạng thái</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${listProducts}" var="p">
                    <tr class="${p.stockQuantity < 5 ? 'table-danger' : ''}">
                        <td>#${p.id}</td>
                        <td><img src="${p.imageUrl}" width="50" class="rounded"></td>
                        <td class="fw-bold">${p.name}</td>
                        <td class="text-center fw-bold fs-5 ${p.stockQuantity < 5 ? 'text-danger' : ''}">${p.stockQuantity}</td>
                        <td>
                            <c:choose>
                                <c:when test="${p.stockQuantity == 0}"><span class="badge bg-dark">Hết hàng</span></c:when>
                                <c:when test="${p.stockQuantity < 5}"><span class="badge bg-danger">Sắp hết</span></c:when>
                                <c:otherwise><span class="badge bg-success">Còn hàng</span></c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>

        <div class="tab-pane fade" id="import">
            <form action="warehouse" method="POST" style="max-width: 600px;">
                <input type="hidden" name="action" value="import">

                <div class="mb-3">
                    <label class="form-label fw-bold">Chọn đồng hồ cần nhập:</label>
                    <select name="productId" class="form-select" required>
                        <option value="">-- Chọn sản phẩm --</option>
                        <c:forEach items="${listProducts}" var="p">
                            <option value="${p.id}">${p.name} (Tồn: ${p.stockQuantity})</option>
                        </c:forEach>
                    </select>
                </div>

                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Số lượng nhập:</label>
                        <input type="number" name="quantity" class="form-control" min="1" required>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label class="form-label fw-bold">Giá nhập vào (Vốn):</label>
                        <div class="input-group">
                            <input type="number" name="importPrice" class="form-control" min="0" required>
                            <span class="input-group-text">VNĐ</span>
                        </div>
                    </div>
                </div>

                <button type="submit" class="btn btn-primary"><i class="fa-solid fa-download"></i> Tạo phiếu nhập kho</button>
            </form>
        </div>

        <div class="tab-pane fade" id="history">
            <ul class="list-group">
                <c:forEach items="${history}" var="h">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        <div>
                            <span class="text-muted"><fmt:formatDate value="${h.createdAt}" pattern="HH:mm dd/MM/yyyy"/>:</span>

                            <c:choose>
                                <c:when test="${h.type == 'IMPORT'}">Nhập</c:when>
                                <c:when test="${h.type == 'SALE'}">Bán</c:when>
                                <c:when test="${h.type == 'RETURN'}">Hoàn lại kho</c:when>
                            </c:choose>

                            <b>${Math.abs(h.qtyChange)} chiếc ${h.productName}</b>

                            <span class="text-muted" style="font-size: 0.9em;">
                                (Từ: <c:if test="${h.type == 'IMPORT'}">Phiếu nhập kho</c:if>
                                     <c:if test="${h.type == 'SALE' || h.type == 'RETURN'}">Đơn hàng</c:if>
                                ${h.refCode})
                            </span>
                        </div>
                        <span class="badge ${h.qtyChange > 0 ? 'bg-success' : 'bg-danger'} rounded-pill fs-6">
                                ${h.qtyChange > 0 ? '+' : ''}${h.qtyChange}
                        </span>
                    </li>
                </c:forEach>
            </ul>
        </div>

    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>