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

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; top: 0; left: 0; z-index: 1000; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0); color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: calc(100% - 250px); min-height: 100vh; background-color: #f4f6f9; }
        .nav-tabs { position: relative; z-index: 1; }

        .toolbar-warehouse { background: #fff; padding: 15px; border-radius: 8px; box-shadow: 0 2px 5px rgba(0,0,0,0.05); margin-bottom: 20px; display: flex; justify-content: space-between; gap: 15px;}
        .pagination { justify-content: center; margin-top: 20px; }

        .ref-link { cursor: pointer; color: #0d6efd; font-weight: bold; text-decoration: underline; transition: 0.2s; }
        .ref-link:hover { color: #0a58ca; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>

    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>

    <a href="order-manager" ><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>

    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
        <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
        <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
        <a href="category-manager"><i class="fa-solid fa-list"></i> Quản lý tìm kiếm</a>
        <a href="warehouse" class="active"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div class="container-fluid p-0">
        <h2 class="mb-4" style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333;"><i class="fa-solid fa-boxes-stacked text-primary" style="color: #1b6e76 !important;"></i> Hệ Thống Quản Lý Kho</h2>

        <c:if test="${param.msg == 'success'}">
            <div class="alert alert-success fw-bold shadow-sm" role="alert" style="border-left: 5px solid #198754;">
                <i class="fa-solid fa-circle-check"></i> Lưu Phiếu nhập kho thành công! Dữ liệu đã được cập nhật.
            </div>
        </c:if>

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
                        <button type="submit" class="btn" style="background: #1b6e76; color: white;"><i class="fa-solid fa-magnifying-glass"></i></button>
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
                        <th style="width: 80px;">Mã SP</th>
                        <th style="width: 70px; text-align: center;">Hình ảnh</th>
                        <th>Tên đồng hồ</th>
                        <th class="text-center" style="width: 150px;">Số lượng tồn</th>
                        <th style="width: 120px;">Trạng thái</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty listProducts}">
                        <tr><td colspan="5" class="text-center text-muted py-4">Không tìm thấy sản phẩm nào!</td></tr>
                    </c:if>
                    <c:forEach items="${listProducts}" var="p">
                        <tr class="${p.stockQuantity < 5 ? 'table-danger' : ''}">
                            <td><b style="color: #1b6e76;">#${p.id}</b></td>
                            <td class="text-center"><img src="${p.imageUrl}" width="50" class="rounded" onerror="this.src='https://via.placeholder.com/50'"></td>
                            <td class="fw-bold">${p.name}</td>
                            <td class="text-center fw-bold fs-5 ${p.stockQuantity < 5 ? 'text-danger' : 'text-success'}">${p.stockQuantity}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${p.stockQuantity == 0}"><span class="badge bg-dark w-100">Hết hàng</span></c:when>
                                    <c:when test="${p.stockQuantity < 5}"><span class="badge bg-danger w-100">Sắp hết</span></c:when>
                                    <c:otherwise><span class="badge bg-success w-100">Còn hàng</span></c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation" class="mt-4">
                        <ul class="pagination">
                            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                <a class="page-link" href="warehouse?page=${currentPage - 1}&keyword=${searchKeyword}&sort=${currentSort}">&laquo;</a>
                            </li>

                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <c:if test="${i == 1 || i == totalPages || (i >= currentPage - 2 && i <= currentPage + 2)}">
                                    <li class="page-item ${currentPage == i ? 'active' : ''}">
                                        <a class="page-link" href="warehouse?page=${i}&keyword=${searchKeyword}&sort=${currentSort}"
                                           style="${currentPage == i ? 'background-color: #1b6e76; border-color: #1b6e76;' : 'color: #333;'}">
                                                ${i}
                                        </a>
                                    </li>
                                </c:if>

                                <c:if test="${(i == currentPage - 3 && i > 1) || (i == currentPage + 3 && i < totalPages)}">
                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                </c:if>
                            </c:forEach>

                            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                <a class="page-link" href="warehouse?page=${currentPage + 1}&keyword=${searchKeyword}&sort=${currentSort}">&raquo;</a>
                            </li>
                        </ul>
                    </nav>
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
                        <button type="submit" class="btn btn-lg" style="background: #1b6e76; color: white;"><i class="fa-solid fa-check"></i> Lưu Phiếu Nhập</button>
                    </div>
                </form>
            </div>

            <div class="tab-pane fade" id="history">
                <ul class="list-group">
                    <c:if test="${empty history}">
                        <li class="list-group-item text-center text-muted py-4">Chưa có biến động kho nào.</li>
                    </c:if>
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

                                <span class="ms-2" style="font-size: 0.95em;">
                                    (Từ:
                                    <c:choose>
                                        <c:when test="${h.type == 'IMPORT'}">
                                            <a class="ref-link" onclick="viewImportDetail('${h.refCode}')">${h.refCode}</a>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="text-muted">${h.refCode}</span>
                                        </c:otherwise>
                                    </c:choose>
                                    )
                                </span>
                            </div>
                            <span class="badge ${h.qtyChange > 0 ? 'bg-success' : 'bg-danger'} rounded-pill fs-6" style="min-width: 40px;">
                                    ${h.qtyChange > 0 ? '+' : ''}${h.qtyChange}
                            </span>
                        </li>
                    </c:forEach>
                </ul>
            </div>

        </div>
    </div>
</div>

<div class="modal fade" id="importDetailModal" tabindex="-1" aria-labelledby="importDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header text-white" style="background: #1b6e76;">
                <h5 class="modal-title" id="importDetailModalLabel"><i class="fa-solid fa-file-invoice"></i> Chi Tiết Phiếu Nhập Kho</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                <div id="modalLoading" class="text-center py-4">
                    <div class="spinner-border" style="color: #1b6e76;" role="status"></div>
                    <p class="mt-2 text-muted">Đang tải dữ liệu...</p>
                </div>

                <div id="modalContent" style="display: none;">
                    <table class="table table-borderless mb-0">
                        <tbody>
                        <tr>
                            <th width="35%" class="text-muted">Mã phiếu nhập:</th>
                            <td id="md-importId" class="fw-bold" style="color: #1b6e76;"></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Tên sản phẩm:</th>
                            <td id="md-productName" class="fw-bold"></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Số lượng nhập:</th>
                            <td id="md-quantity"></td>
                        </tr>
                        <tr>
                            <th class="text-muted">Đơn giá nhập:</th>
                            <td id="md-price" class="fw-bold"></td>
                        </tr>
                        <tr class="border-top">
                            <th class="text-muted pt-3">Tổng giá trị:</th>
                            <td id="md-total" class="text-danger fw-bold fs-5 pt-3"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Đóng</button>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function viewImportDetail(refCode) {
        const importId = refCode.replace("PNK-", "").trim();

        const detailModal = new bootstrap.Modal(document.getElementById('importDetailModal'));
        detailModal.show();

        document.getElementById('modalLoading').style.display = 'block';
        document.getElementById('modalContent').style.display = 'none';

        fetch('warehouse?action=getImportDetail&id=' + importId)
            .then(res => res.json())
            .then(data => {
                document.getElementById('modalLoading').style.display = 'none';
                document.getElementById('modalContent').style.display = 'block';

                document.getElementById('md-importId').innerText = "PNK-" + data.importId;
                document.getElementById('md-productName').innerText = data.productName;
                document.getElementById('md-quantity').innerText = data.quantity + " chiếc";

                const formatter = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' });
                document.getElementById('md-price').innerText = formatter.format(data.importPrice);
                document.getElementById('md-total').innerText = formatter.format(data.totalValue);
            })
            .catch(err => {
                document.getElementById('modalLoading').innerHTML = '<p class="text-danger fw-bold text-center"><i class="fa-solid fa-triangle-exclamation"></i> Lỗi khi tải dữ liệu. Vui lòng thử lại!</p>';
                console.error("Fetch Error: ", err);
            });
    }

    function extractProductId(input) {
        const val = input.value;
        const realInput = input.closest('td').querySelector('.real-product-id');
        if(val.includes(" - ")) {
            realInput.value = val.split(" - ")[0];
        } else {
            realInput.value = "";
        }
    }

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

    document.addEventListener("input", function(e) {
        if (e.target.classList.contains("qty-input") || e.target.classList.contains("price-input")) {
            if (e.target.value < 0) {
                e.target.value = e.target.classList.contains("qty-input") ? 1 : 0;
                if (typeof calculateRow === "function") {
                    calculateRow(e.target);
                }
            }
        }
    });
</script>
</body>
</html>