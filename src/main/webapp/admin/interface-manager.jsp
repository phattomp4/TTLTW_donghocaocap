<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Giao diện & Banner | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <script src="https://cdn.jsdelivr.net/npm/sortablejs@1.15.0/Sortable.min.js"></script>
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: calc(100% - 250px); box-sizing: border-box;}
        .card { background: #fff; padding: 25px; margin-bottom: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card h3 { margin-top: 0; color: #1b6e76; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 15px; }
        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 14px; }
        .form-control { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; outline: none; }
        .form-control:focus { border-color: #1b6e76; }
        textarea.form-control { resize: vertical; }

        .btn-save { background: #1b6e76; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; font-weight: bold; transition: 0.2s; }
        .btn-save:hover { background: #145258; }
        .btn-delete { background: #dc3545; color: white; border: none; padding: 6px 12px; border-radius: 4px; cursor: pointer; transition: 0.2s; }
        .btn-delete:hover { background: #c82333; }

        .admin-table { width: 100%; border-collapse: collapse; text-align: left; margin-bottom: 15px; }
        .admin-table th, .admin-table td { padding: 12px; border-bottom: 1px solid #eee; vertical-align: middle; }
        .admin-table th { background: #f8f9fa; color: #555; font-weight: 600; }

        .drag-row { background: white; }
        .sortable-ghost { opacity: 0.4; background-color: #d1ecf1 !important; }
        .handle-icon { color: #aaa; font-size: 18px; cursor: grab; }
        .handle-icon:hover { color: #1b6e76; }

        .item-list { display: flex; gap: 15px; flex-wrap: wrap; }
        .item-box { border: 1px solid #ddd; padding: 10px; border-radius: 6px; position: relative; width: 180px; text-align: center; background: #fafafa; }
        .item-box img { max-width: 100%; height: 80px; object-fit: contain; margin-bottom: 10px; border-radius: 4px; border: 1px solid #eee; background: white; }
        .item-box .del-form { position: absolute; top: 5px; right: 5px; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard" ><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>
    <a href="order-manager"><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager" ><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>

    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
        <a href="interface-manager" class="active"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
        <a href="category-manager"><i class="fa-solid fa-list"></i> Danh mục & Menu</a>
        <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ</a>
</div>

<div class="content">
    <h2>Quản lý Tổng thể Giao diện & Banner Hệ thống</h2>

    <c:if test="${not empty param.msg}">
        <div style="padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold;
                    ${param.msg == 'success' ? 'background: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : 'background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;'}">
            <c:choose>
                <c:when test="${param.msg == 'success'}"><i class="fa-solid fa-circle-check"></i> Thao tác cập nhật cấu hình thành công!</c:when>
                <c:otherwise><i class="fa-solid fa-circle-xmark"></i> Thao tác thất bại. Vui lòng kiểm tra lại!</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <div class="card">
        <h3><i class="fa-solid fa-store"></i> Thông tin Cửa hàng & Footer</h3>
        <form action="interface-manager" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="action" value="updateInfo">
            <input type="hidden" name="oldMainImage" value="${shopInfo.mainImageUrl}">

            <div style="display: flex; gap: 20px;">
                <div style="flex: 1;">
                    <div class="form-group"><label>Tên Shop:</label><input type="text" name="brandName" class="form-control" value="${shopInfo.brandName}"></div>
                    <div class="form-group"><label>Slogan:</label><input type="text" name="subtitle" class="form-control" value="${shopInfo.subtitle}"></div>
                    <div class="form-group">
                        <label>Ảnh Giới Thiệu (Main):</label>
                        <img src="${shopInfo.mainImageUrl}" style="height: 60px; margin-bottom: 5px; display: block; border-radius:4px;">
                        <input type="file" name="mainImage" class="form-control">
                    </div>
                </div>
                <div style="flex: 1;">
                    <div class="form-group"><label>Hotline:</label><input type="text" name="hotline" class="form-control" value="${shopInfo.hotline}"></div>
                    <div class="form-group"><label>Email:</label><input type="text" name="email" class="form-control" value="${shopInfo.email}"></div>
                    <div class="form-group"><label>Địa chỉ:</label><input type="text" name="address" class="form-control" value="${shopInfo.address}"></div>
                    <div class="form-group"><label>Copyright:</label><input type="text" name="copyright" class="form-control" value="${shopInfo.copyright}"></div>
                </div>
            </div>

            <div class="form-group"><label>Mô tả Footer:</label><textarea name="footerDesc" rows="2" class="form-control">${shopInfo.footerDesc}</textarea></div>
            <div class="form-group"><label>Lịch sử 1:</label><textarea name="history1" rows="2" class="form-control">${shopInfo.history1}</textarea></div>
            <div class="form-group"><label>Lịch sử 2:</label><textarea name="history2" rows="2" class="form-control">${shopInfo.history2}</textarea></div>
            <div class="form-group"><label>Lịch sử 3:</label><textarea name="history3" rows="2" class="form-control">${shopInfo.history3}</textarea></div>

            <button type="submit" class="btn-save"><i class="fa-solid fa-floppy-disk"></i> Cập nhật thông tin shop</button>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-images"></i> Cấu hình Banner Slideshow (Kéo thả sắp xếp & Hẹn giờ)</h3>

        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="background: #f8f9fa; padding: 20px; border-radius: 6px; border: 1px solid #e3e6f0; margin-bottom: 25px;">
            <input type="hidden" name="action" value="addBanner">
            <h4 style="margin-top: 0; margin-bottom: 15px; color: #555; font-size: 15px;"><i class="fa-solid fa-plus-circle"></i> Tải lên banner mới</h4>

            <div class="form-grid">
                <div>
                    <div class="form-group">
                        <label>Chọn file hình ảnh: <span style="color: red;">*</span></label>
                        <input type="file" name="bannerImage" class="form-control" accept="image/*" required>
                    </div>
                    <div class="form-group">
                        <label>Đường dẫn liên kết khi click (Link Điều Hướng):</label>
                        <input type="text" name="linkUrl" class="form-control" placeholder="Ví dụ: product-detail?id=12 hoặc khuyen-mai-he">
                    </div>
                    <div class="form-group">
                        <label>Thứ tự hiển thị ban đầu:</label>
                        <input type="number" name="sortOrder" class="form-control" value="1" min="1"
                               oninput="if(this.value < 1) this.value = 1;">
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
            <div style="text-align: right;">
                <button type="submit" class="btn-save" style="background-color: #28a745;"><i class="fa-solid fa-cloud-arrow-up"></i> Tải Lên Banner</button>
            </div>
        </form>

        <table class="admin-table">
            <thead>
            <tr>
                <th style="width: 50px; text-align: center;">Kéo</th>
                <th style="width: 180px;">Hình ảnh</th>
                <th>Đường dẫn kết nối (Link URL)</th>
                <th>Thời hạn hiển thị banner</th>
                <th style="text-align: center; width: 100px;">Hành động</th>
            </tr>
            </thead>
            <tbody id="sortable-banner-list">
            <c:forEach items="${listSlideshow}" var="b">
                <tr class="drag-row" data-id="${b.id}">
                    <td style="text-align: center;"><i class="fa-solid fa-bars handle-icon" title="Giữ chuột kéo để đổi vị trí"></i></td>
                    <td>
                        <img src="${b.imageUrl.startsWith('http') ? b.imageUrl : pageContext.request.contextPath.concat('/').concat(b.imageUrl)}"
                             style="width: 120px; height: 60px; object-fit: cover;">                    </td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty b.linkUrl}">
                                <code style="color: #1b6e76; font-weight: bold; font-size:13px;">${b.linkUrl}</code>
                            </c:when>
                            <c:otherwise><span style="color: #999; font-style: italic;">Không gắn link điều hướng</span></c:otherwise>
                        </c:choose>
                    </td>
                    <td style="font-size: 13px; color: #555;">
                        <div><i class="fa-regular fa-clock" style="color: #28a745; margin-right:5px;"></i>Bắt đầu:
                            <c:choose>
                                <c:when test="${not empty b.startDate}"><fmt:formatDate value="${b.startDate}" pattern="dd/MM/yyyy HH:mm"/></c:when>
                                <c:otherwise>Hiển thị ngay lập tức</c:otherwise>
                            </c:choose>
                        </div>
                        <div style="margin-top: 5px;"><i class="fa-regular fa-clock" style="color: #dc3545; margin-right:5px;"></i>Kết thúc:
                            <c:choose>
                                <c:when test="${not empty b.endDate}"><fmt:formatDate value="${b.endDate}" pattern="dd/MM/yyyy HH:mm"/></c:when>
                                <c:otherwise>Hiển thị vô thời hạn</c:otherwise>
                            </c:choose>
                        </div>
                    </td>
                    <td style="text-align: center;">
                        <form action="interface-manager" method="POST" style="margin: 0;" onsubmit="return confirm('Bạn có chắc chắn muốn gỡ bỏ hoàn toàn banner này?');">
                            <input type="hidden" name="action" value="deleteBanner">
                            <input type="hidden" name="id" value="${b.id}">
                            <button type="submit" class="btn-delete"><i class="fa-solid fa-trash-can"></i> Xóa</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            <c:if test="${empty listSlideshow}">
                <tr><td colspan="5" style="text-align: center; color: #999; padding: 25px;">Chưa có banner slideshow nào được cấu hình.</td></tr>
            </c:if>
            </tbody>
        </table>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-photo-film"></i> Small Banner (Quảng cáo nhỏ bên sườn)</h3>
        <div class="item-list">
            <c:forEach items="${listSmallBanners}" var="s">
                <div class="item-box">
                    <img src="${s.imageUrl.startsWith('http') ? s.imageUrl : pageContext.request.contextPath.concat('/').concat(s.imageUrl)}">
                    <p style="font-size: 12px; margin: 0; font-weight: bold; color:#555;">Thứ tự: ${s.sortOrder}</p>
                    <form action="interface-manager" method="POST" class="del-form" onsubmit="return confirm('Xóa banner nhỏ này?');">
                        <input type="hidden" name="action" value="deleteSmallBanner">
                        <input type="hidden" name="id" value="${s.id}">
                        <button class="btn-delete" style="padding: 3px 8px; font-size: 11px;"><i class="fa-solid fa-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <hr style="border:0; border-top: 1px solid #eee; margin: 20px 0;">
        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" value="addSmallBanner">
            <div style="flex: 1;"><label style="font-size: 13px; font-weight: 600; color:#555;">Chọn file ảnh quảng cáo phụ:</label><input type="file" name="smallImage" class="form-control" required></div>
            <div style="width: 100px;"><label style="font-size: 13px; font-weight: 600; color:#555;">Thứ tự:</label><input type="number" name="sortOrder" class="form-control" value="1" min="1"
                                                                                                                           oninput="if(this.value < 1) this.value = 1;"></div>
            <button type="submit" class="btn-save" style="background-color: #28a745;">Thêm Small Banner</button>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-copyright"></i> Logo Thương hiệu đối tác (Footer)</h3>
        <div class="item-list">
            <c:forEach items="${listBrands}" var="brand">
                <div class="item-box">
                    <img src="${brand.logoUrl.startsWith('http') ? brand.logoUrl : pageContext.request.contextPath.concat('/').concat(brand.logoUrl)}"
                         alt="${brand.name}">
                    <strong style="display: block; font-size: 13px; color:#333;">${brand.name}</strong>
                    <form action="interface-manager" method="POST" class="del-form" onsubmit="return confirm('Xóa đối tác này?');">
                        <input type="hidden" name="action" value="deleteBrand">
                        <input type="hidden" name="id" value="${brand.id}">
                        <button class="btn-delete" style="padding: 3px 8px; font-size: 11px;"><i class="fa-solid fa-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <hr style="border:0; border-top: 1px solid #eee; margin: 20px 0;">
        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" value="addBrand">
            <div style="flex: 1;"><label style="font-size: 13px; font-weight: 600; color:#555;">Tên nhà sản xuất/Đối tác:</label><input type="text" name="brandName" class="form-control" required placeholder="VD: Patek Philippe"></div>
            <div style="flex: 1;"><label style="font-size: 13px; font-weight: 600; color:#555;">Logo đối tác:</label><input type="file" name="brandLogo" class="form-control" required></div>
            <div style="width: 80px;"><label style="font-size: 13px; font-weight: 600; color:#555;">Thứ tự:</label><input type="number" name="sortOrder" class="form-control" value="1" min="1"
                                                                                                                          oninput="if(this.value < 1) this.value = 1;"></div>
            <button type="submit" class="btn-save" style="background-color: #28a745;">Thêm Đối Tác</button>
        </form>
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

                    sortableContainer.querySelectorAll('.drag-row').forEach(tr => {
                        reorderedIds.push(tr.getAttribute('data-id'));
                    });

                    const formParams = new URLSearchParams();
                    formParams.append('action', 'updateOrder');
                    reorderedIds.forEach(id => {
                        formParams.append('ids[]', id);
                    });

                    fetch('${pageContext.request.contextPath}/admin/interface-manager', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/x-www-form-urlencoded'
                        },
                        body: formParams.toString()
                    })
                        .then(response => response.text())
                        .then(result => {
                            if (result.trim() === "success") {
                                console.log("Trật tự sắp xếp Slideshow đã được lưu lại thành công!");
                            } else {
                                alert("Có sự cố phát sinh tại máy chủ khi sắp xếp vị trí!");
                            }
                        })
                        .catch(error => {
                            console.error("Lỗi truyền tải mạng:", error);
                        });
                }
            });
        }
    });
</script>
</body>
</html>