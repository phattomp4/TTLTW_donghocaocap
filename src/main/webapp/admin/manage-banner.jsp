<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Banner Hệ Thống | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100;}
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }
        .content { margin-left: 250px; padding: 30px; width: calc(100% - 250px); box-sizing: border-box; }

        .card { background: #fff; padding: 25px; margin-bottom: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card h3 { margin-top: 0; color: #1b6e76; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 14px; }
        .form-control { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }

        .btn-save { background: #1b6e76; color: white; padding: 10px 25px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; transition: 0.2s;}
        .btn-save:hover { background: #145258; }
        .btn-delete { background: #dc3545; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; transition: 0.2s;}
        .btn-delete:hover { background: #c82333; }

        .admin-table { width: 100%; border-collapse: collapse; text-align: left; }
        .admin-table th, .admin-table td { padding: 12px; border-bottom: 1px solid #eee; }
        .admin-table th { background: #f8f9fa; color: #555; font-weight: 600; }
        .drag-row { cursor: grab; background: white; }
        .drag-row:active { cursor: grabbing; }
        .sortable-ghost { opacity: 0.4; background-color: #d1ecf1 !important; }
        .handle-icon { color: #aaa; font-size: 18px; cursor: grab; }
        .handle-icon:hover { color: #1b6e76; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>
    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>

    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
        <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
        <a href="interface-manager"  class="active"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
        <a href="category-manager"><i class="fa-solid fa-list"></i> Quản lý tìm kiếm</a>
        <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <h2>Quản lý Giao diện - Phân hệ Cấu hình Banner</h2>

    <c:if test="${not empty param.msg}">
        <div style="padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold;
                    ${param.msg == 'success' ? 'background: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : 'background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;'}">
            <c:choose>
                <c:when test="${param.msg == 'success'}"><i class="fa-solid fa-circle-check"></i> Cập nhật banner thành công!</c:when>
                <c:otherwise><i class="fa-solid fa-circle-xmark"></i> Thao tác thất bại. Vui lòng thử lại!</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <div class="card">
        <h3><i class="fa-solid fa-circle-plus"></i> Thêm Banner Mới</h3>
        <form action="banner-manager" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="add">

            <div class="form-grid">
                <div>
                    <div class="form-group">
                        <label>Chọn file hình ảnh: <span style="color: red;">*</span></label>
                        <input type="file" name="image" class="form-control" accept="image/*" required>
                    </div>
                    <div class="form-group">
                        <label>Đường dẫn liên kết khi nhấn vào (Link Điều Hướng Chương Trình):</label>
                        <input type="text" name="linkUrl" class="form-control" placeholder="Ví dụ: product-detail?id=12 hoặc khuyen-mai-he">
                    </div>
                    <div class="form-group">
                        <label>Thứ tự hiển thị mặc định:</label>
                        <input type="number" name="sortOrder" class="form-control" value="1" min="1">
                    </div>
                </div>
                <div>
                    <div class="form-group">
                        <label>Thời gian tự động hiển thị (Bắt đầu):</label>
                        <input type="datetime-local" name="startDate" class="form-control">
                    </div>
                    <div class="form-group">
                        <label>Thời gian tự động ẩn (Kết thúc):</label>
                        <input type="datetime-local" name="endDate" class="form-control">
                    </div>
                </div>
            </div>
            <div style="text-align: right; margin-top: 15px;">
                <button type="submit" class="btn-save"><i class="fa-solid fa-cloud-arrow-up"></i> Tải Lên & Áp Dụng</button>
            </div>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-arrows-up-down-left-right"></i> Thứ tự sắp xếp Banner Slideshow (Kéo thả hàng để thay đổi)</h3>
        <table class="admin-table">
            <thead>
            <tr>
                <th style="width: 60px; text-align: center;">Kéo</th>
                <th>Hình ảnh</th>
                <th>Đường dẫn kết nối (Link)</th>
                <th>Thời hạn hiển thị</th>
                <th style="text-align: center;">Hành động</th>
            </tr>
            </thead>
            <tbody id="sortable-banner-list">
            <c:forEach items="${listSlideshow}" var="b">
                <tr class="drag-row" data-id="${b.id}">
                    <td style="text-align: center;"><i class="fa-solid fa-bars handle-icon" title="Kéo để di chuyển"></i></td>
                    <td>
                        <img src="${pageContext.request.contextPath}/${b.imageUrl}" style="width: 150px; height: 65px; object-fit: cover; border-radius: 4px; border: 1px solid #ddd;">
                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty b.linkUrl}">
                                <code style="color: #1b6e76; font-weight: bold;">${b.linkUrl}</code>
                            </c:when>
                            <c:otherwise><span style="color: #999; font-style: italic;">Không gắn link</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td style="font-size: 13px; color: #555;">
                        <div><i class="fa-regular fa-clock" style="color: #28a745;"></i> Bắt đầu:
                            <c:choose>
                                <c:when test="${not empty b.startDate}"><fmt:formatDate value="${b.startDate}" pattern="dd/MM/yyyy HH:mm"/></c:when>
                                <c:otherwise>Hiển thị ngay</c:otherwise>
                            </c:choose>
                        </div>
                        <div style="margin-top: 5px;"><i class="fa-regular fa-clock" style="color: #dc3545;"></i> Kết thúc:
                            <c:choose>
                                <c:when test="${not empty b.endDate}"><fmt:formatDate value="${b.endDate}" pattern="dd/MM/yyyy HH:mm"/></c:when>
                                <c:otherwise>Vô thời hạn</c:otherwise>
                            </c:choose>
                        </div>
                    </td>
                    <td style="text-align: center;">
                        <form action="banner-manager" method="POST" style="display:inline;" onsubmit="return confirm('Bạn có chắc chắn muốn xóa bỏ banner này khỏi hệ thống?');">
                            <input type="hidden" name="action" value="deleteBanner">
                            <input type="hidden" name="id" value="${b.id}">
                            <button type="submit" class="btn-delete"><i class="fa-solid fa-trash-can"></i> Xóa</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listSlideshow}">
                <tr><td colspan="5" style="text-align: center; color: #999; padding: 30px;">Hệ thống trống, hãy thêm dữ liệu banner ở trên!</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>
</div>

<script>
    document.addEventListener("DOMContentLoaded", function() {
        const sortableContainer = document.getElementById('sortable-banner-list');

        if (sortableContainer) {
            new Sortable(sortableContainer, {
                handle: '.handle-icon',
                animation: 250,
                ghostClass: 'sortable-ghost',

                onEnd: function() {
                    let reorderedIds = [];

                    sortableContainer.querySelectorAll('tr').forEach(tr => {
                        reorderedIds.push(tr.getAttribute('data-id'));
                    });

                    const formParams = new URLSearchParams();
                    formParams.append('action', 'updateOrder');
                    reorderedIds.forEach(id => {
                        formParams.append('ids[]', id);
                    });

                    fetch('${pageContext.request.contextPath}/admin/banner-manager', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: formParams.toString()
                    })
                        .then(response => response.text())
                        .then(result => {
                            if (result === "success") {
                                console.log("Hệ thống đã lưu lại trật tự sắp xếp mới thành công vào DB!");
                            } else {
                                alert("Có lỗi phát sinh từ máy chủ khi lưu cấu hình vị trí!");
                            }
                        })
                        .catch(error => {
                            console.error("Lỗi mất kết nối mạng truyền tải dữ liệu:", error);
                        });
                }
            });
        }
    });
</script>
</body>
</html>