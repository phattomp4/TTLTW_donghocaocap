<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản Lý Kho Hàng | VVP Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        /* SIDEBAR STYLE (Fix cứng) */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; top: 0; left: 0; z-index: 1000; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0); color: white; }
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        /* CONTENT STYLE (Đẩy sang phải 250px chống chồng chéo) */
        .content { margin-left: 250px; padding: 30px; width: calc(100% - 250px); min-height: 100vh; background-color: #f4f6f9; }
        .nav-tabs { position: relative; z-index: 1; }

        .toolbar-warehouse { background: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 20px; display: flex; justify-content: space-between; gap: 15px;}
        .pagination { justify-content: center; margin-top: 20px; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-chart-pie"></i> Tổng quan</a>
    <a href="order-manager"><i class="fa-solid fa-cart-shopping"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="warehouse" class="active"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
</div>

<div class="content">
    <div class="container-fluid p-0">
        <h2 class="mb-4"><i class="fa-solid fa-boxes-stacked text-primary"></i> Hệ Thống Quản Lý Kho</h2>

        <ul class="nav nav-tabs mb-4" id="warehouseTabs">
            <li class="nav-item"><a class="nav-link active" data-bs-toggle="tab" href="#inventory">Bảng Tồn Kho</a></li>
            <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#import">Tạo Phiếu Nhập Kho</a></li>
            <li class="nav-item"><a class="nav-link" data-bs-toggle="tab" href="#history">Lịch Sử Biến Động</a></li>
        </ul>

        <div class="tab-content bg-white p-4 shadow-sm rounded border border-top-0">

            <div class="tab-pane fade show active" id="inventory">

                <form action="warehouse" method="GET" class="toolbar-warehouse">
                    <div class="input-group" style="max-width: 400px;">
                        <input type="text" name="keyword" class="form-control" placeholder="Tìm tên đồng hồ..." value="${searchKeyword}">
                        <button type="submit" class="btn btn-dark"><i class="fa-solid fa-magnifying-glass"></i></button>
                    </div>

                    <div class="d-flex align-items-center gap-2">
                        <label class="fw-bold text-nowrap">Sắp xếp theo:</label>
                        <select name="sort" class="form-select" onchange="this.form.submit()">
                            <option value="default" ${currentSort == 'default' ? 'selected' : ''}>Ưu tiên cảnh báo tồn kho</option>
                            <option value="newest" ${currentSort == 'newest' ? 'selected' : ''}>Sản phẩm mới nhất</option>
                            <option value="stock_desc" ${currentSort == 'stock_desc' ? 'selected' : ''}>Tồn kho: Nhiều đến ít</option>
                            <option value="name_asc" ${currentSort == 'name_asc' ? 'selected' : ''}>Tên (A-Z)</option>
                        </select>
                    </div>
                </form>

                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-light">
                    <tr>
                        <th>Mã SP</th><th>Hình ảnh</th><th>Tên đồng hồ</th>
                        <th class="text-center">Số lượng tồn</th><th>Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty listProducts}">
                        <tr><td colspan="5" class="text-center text-muted py-4">Không tìm thấy sản phẩm nào!</td></tr>
                    </c:if>
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

                <c:if test="${totalPages > 1}">
                    <ul class="pagination">
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                <a class="page-link" href="warehouse?page=${i}&keyword=${searchKeyword}&sort=${currentSort}">${i}</a>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
            </div>

            <div class="tab-pane fade" id="import">
                <form action="warehouse?action=importMulti" method="POST">

                    <div class="row mb-4">
                        <div class="col-md-4">
                            <label class="fw-bold">Nhà cung cấp / Nguồn nhập:</label>
                            <input type="text" name="supplier" class="form-control" placeholder="VD: Đại lý Rolex Việt Nam..." required>
                        </div>
                        <div class="col-md-8">
                            <label class="fw-bold">Ghi chú phiếu nhập:</label>
                            <input type="text" name="note" class="form-control" placeholder="VD: Lô hàng tháng 5, kèm hóa đơn số 123...">
                        </div>
                    </div>

                    <datalist id="productListDL">
                        <c:forEach items="${allProducts}" var="p">
                            <option value="${p.id} - ${p.name}">Mã: #${p.id} (Tồn: ${p.stockQuantity})</option>
                        </c:forEach>
                    </datalist>

                    <table class="table table-bordered align-middle" id="importTable">
                        <thead class="table-light">
                        <tr>
                            <th width="40%">Tìm & Chọn Sản Phẩm</th>
                            <th width="15%">Số Lượng</th>
                            <th width="20%">Giá Nhập (VNĐ)</th>
                            <th width="20%">Thành Tiền</th>
                            <th width="5%" class="text-center"><i class="fa-solid fa-trash"></i></th>
                        </tr>
                        </thead>
                        <tbody id="importBody">
                        <tr>
                            <td>
                                <input list="productListDL" class="form-control product-search" placeholder="Gõ tên để tìm..." required onchange="extractProductId(this)">
                                <input type="hidden" name="productIds[]" class="real-product-id">
                            </td>
                            <td><input type="number" name="quantities[]" class="form-control qty-input" value="1" min="1" required onchange="calculateRow(this)"></td>
                            <td><input type="number" name="prices[]" class="form-control price-input" value="0" min="0" required onchange="calculateRow(this)"></td>
                            <td><input type="text" class="form-control row-total" value="0" readonly style="background: #e9ecef; font-weight:bold; color:#d0011b;"></td>
                            <td class="text-center">
                                <button type="button" class="btn btn-sm btn-danger" onclick="removeRow(this)"><i class="fa-solid fa-xmark"></i></button>
                            </td>
                        </tr>
                        </tbody>
                    </table>

                    <div class="d-flex justify-content-between align-items-center mt-3 border-top pt-3">
                        <button type="button" class="btn btn-outline-secondary" onclick="addRow()">
                            <i class="fa-solid fa-plus"></i> Thêm sản phẩm
                        </button>
                        <h4 class="text-end mb-0">Tổng giá trị: <span id="grandTotal" class="text-danger fw-bold">0 ₫</span></h4>
                    </div>

                    <div class="text-end mt-4">
                        <button type="submit" class="btn btn-primary btn-lg"><i class="fa-solid fa-check"></i> Lưu Phiếu Nhập</button>
                    </div>
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
                                <span class="text-muted" style="font-size: 0.9em;">(Từ: ${h.refCode})</span>
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
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Trích xuất ID từ chuỗi "12 - Đồng hồ Rolex" của Datalist
    function extractProductId(input) {
        const val = input.value;
        const realInput = input.closest('td').querySelector('.real-product-id');
        if(val.includes(" - ")) {
            realInput.value = val.split(" - ")[0]; // Lấy phần số ID trước dấu gạch ngang
        } else {
            realInput.value = "";
        }
    }

    // Các hàm tính toán bảng Nhập Kho (Giữ nguyên logic lúc trước)
    function addRow() {
        const tbody = document.getElementById('importBody');
        const firstRow = tbody.querySelector('tr').cloneNode(true);
        firstRow.querySelector('.product-search').value = "";
        firstRow.querySelector('.real-product-id').value = "";
        firstRow.querySelector('.qty-input').value = "1";
        firstRow.querySelector('.price-input').value = "0";
        firstRow.querySelector('.row-total').value = "0";
        tbody.appendChild(firstRow);
    }

    function removeRow(btn) {
        if (document.getElementById('importBody').children.length > 1) {
            btn.closest('tr').remove();
            calculateGrandTotal();
        } else alert("Phải có ít nhất 1 sản phẩm!");
    }

    function calculateRow(element) {
        const row = element.closest('tr');
        const total = (row.querySelector('.qty-input').value || 0) * (row.querySelector('.price-input').value || 0);
        row.querySelector('.row-total').value = new Intl.NumberFormat('vi-VN').format(total);
        calculateGrandTotal();
    }

    function calculateGrandTotal() {
        let total = 0;
        document.querySelectorAll('#importBody tr').forEach(row => {
            total += (row.querySelector('.qty-input').value || 0) * (row.querySelector('.price-input').value || 0);
        });
        document.getElementById('grandTotal').innerText = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(total);
    }
</script>
</body>
</html>