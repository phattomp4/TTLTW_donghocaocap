<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý Giao diện | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }
        /* Sidebar */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }
        .content { margin-left: 250px; padding: 30px; width: 100%; }

        /* Card Style */
        .card { background: #fff; padding: 25px; margin-bottom: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.05); }
        .card h3 { margin-top: 0; color: #1b6e76; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 20px; }

        .form-group { margin-bottom: 15px; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 14px; }
        .form-control { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 4px; box-sizing: border-box; }
        textarea.form-control { resize: vertical; }

        .btn-save { background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; }
        .btn-delete { background: #dc3545; color: white; border: none; padding: 5px 10px; border-radius: 4px; cursor: pointer; }

        /* Item List (Banner/Brand) */
        .item-list { display: flex; gap: 15px; flex-wrap: wrap; }
        .item-box { border: 1px solid #ddd; padding: 10px; border-radius: 6px; position: relative; width: 180px; text-align: center; background: #fafafa; }
        .item-box img { max-width: 100%; height: 80px; object-fit: contain; margin-bottom: 10px; border-radius: 4px; border: 1px solid #eee; background: white; }
        .item-box .del-form { position: absolute; top: 5px; right: 5px; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="interface-manager" class="active"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">

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
                        <img src="${shopInfo.mainImageUrl}" style="height: 60px; margin-bottom: 5px; display: block;">
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

            <button type="submit" class="btn-save">Cập nhật thông tin</button>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-images"></i> Banner Slideshow (Lớn)</h3>
        <div class="item-list">
            <c:forEach items="${listSlideshow}" var="b">
                <div class="item-box">
                    <img src="${b.imageUrl}">
                    <p style="font-size: 12px; margin: 0;">Thứ tự: ${b.sortOrder}</p>
                    <form action="interface-manager" method="POST" class="del-form" onsubmit="return confirm('Xóa banner này?');">
                        <input type="hidden" name="action" value="deleteBanner">
                        <input type="hidden" name="id" value="${b.id}">
                        <button class="btn-delete"><i class="fa-solid fa-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <hr>
        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" value="addBanner">
            <div style="flex: 1;"><label>Chọn ảnh:</label><input type="file" name="bannerImage" class="form-control" required></div>
            <div style="width: 100px;"><label>Thứ tự:</label><input type="number" name="sortOrder" class="form-control" value="1"></div>
            <button type="submit" class="btn-save">Thêm Banner</button>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-photo-film"></i> Small Banner (Quảng cáo nhỏ)</h3>
        <div class="item-list">
            <c:forEach items="${listSmallBanners}" var="s">
                <div class="item-box">
                    <img src="${s.imageUrl}">
                    <p style="font-size: 12px; margin: 0;">Thứ tự: ${s.sortOrder}</p>
                    <form action="interface-manager" method="POST" class="del-form" onsubmit="return confirm('Xóa banner nhỏ này?');">
                        <input type="hidden" name="action" value="deleteSmallBanner">
                        <input type="hidden" name="id" value="${s.id}">
                        <button class="btn-delete"><i class="fa-solid fa-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <hr>
        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" value="addSmallBanner">
            <div style="flex: 1;"><label>Chọn ảnh:</label><input type="file" name="smallImage" class="form-control" required></div>
            <div style="width: 100px;"><label>Thứ tự:</label><input type="number" name="sortOrder" class="form-control" value="1"></div>
            <button type="submit" class="btn-save">Thêm Small Banner</button>
        </form>
    </div>

    <div class="card">
        <h3><i class="fa-solid fa-copyright"></i> Thương hiệu đối tác</h3>
        <div class="item-list">
            <c:forEach items="${listBrands}" var="brand">
                <div class="item-box">
                    <img src="${brand.logoUrl}">
                    <strong style="display: block; font-size: 13px;">${brand.name}</strong>
                    <form action="interface-manager" method="POST" class="del-form" onsubmit="return confirm('Xóa thương hiệu này?');">
                        <input type="hidden" name="action" value="deleteBrand">
                        <input type="hidden" name="id" value="${brand.id}">
                        <button class="btn-delete"><i class="fa-solid fa-trash"></i></button>
                    </form>
                </div>
            </c:forEach>
        </div>
        <hr>
        <form action="interface-manager" method="POST" enctype="multipart/form-data" style="display: flex; gap: 10px; align-items: flex-end;">
            <input type="hidden" name="action" value="addBrand">
            <div style="flex: 1;"><label>Tên Thương hiệu:</label><input type="text" name="brandName" class="form-control" required></div>
            <div style="flex: 1;"><label>Logo:</label><input type="file" name="brandLogo" class="form-control" required></div>
            <div style="width: 80px;"><label>Thứ tự:</label><input type="number" name="sortOrder" class="form-control" value="1"></div>
            <button type="submit" class="btn-save">Thêm</button>
        </form>
    </div>



</div>

</body>
</html>