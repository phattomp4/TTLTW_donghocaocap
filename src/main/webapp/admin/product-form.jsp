<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>
        ${empty product ? 'Thêm sản phẩm mới' : 'Cập nhật sản phẩm'} | Admin
    </title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100;}
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }
        .content { margin-left: 250px; padding: 30px; width: 100%; box-sizing: border-box;}

        .form-container { background: white; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 900px; margin: 0 auto; }

        .form-section { margin-bottom: 30px; border-bottom: 1px dashed #ddd; padding-bottom: 20px; }
        .form-section h3 { color: #1b6e76; margin-bottom: 15px; font-size: 18px; text-transform: uppercase; }

        .form-row { display: flex; gap: 20px; margin-bottom: 15px; }
        .form-group { flex: 1; }
        .form-group label { display: block; margin-bottom: 5px; font-weight: 600; font-size: 14px; }
        .form-control { width: 100%; padding: 10px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; outline: none;}
        .form-control:focus { border-color: #1b6e76; }

        .btn-save { background: #1b6e76; color: white; padding: 12px 25px; border: none; border-radius: 4px; cursor: pointer; font-size: 15px; font-weight: bold; transition: 0.2s;}
        .btn-save:hover { background: #145258; }

        .btn-back { display: inline-block; padding: 11px 20px; color: #555; text-decoration: none; margin-right: 15px; border-radius: 4px; border: 1px solid #ddd; font-weight: bold; transition: 0.2s; background: #fff;}
        .btn-back:hover { background: #f1f1f1; color: #d0011b; border-color: #ccc;}
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <c:if test="${sessionScope.acc.role == 'Admin'}">
        <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    </c:if>

    <a href="order-manager" ><i class="fa-solid fa-receipt"></i> Quản lý Đơn hàng</a>
    <a href="product-manager" class="active"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>

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
    <div class="form-container">
        <h2 style="margin-top: 0; border-bottom: 2px solid #eee; padding-bottom: 15px; margin-bottom: 20px; color: #333;">
            <c:choose>
                <c:when test="${empty product}">
                    <i class="fa-solid fa-plus-circle" style="color: #28a745;"></i> THÊM SẢN PHẨM MỚI
                </c:when>
                <c:otherwise>
                    <i class="fa-solid fa-pen-to-square" style="color: #007bff;"></i> CẬP NHẬT SẢN PHẨM: <span style="color: #d0011b;">#${product.id}</span>
                </c:otherwise>
            </c:choose>
        </h2>

        <form action="product-form" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${product.id}">

            <div class="form-section">
                <h3>1. Thông tin chung</h3>
                <div class="form-group" style="margin-bottom: 15px;">
                    <label>Tên sản phẩm:</label>
                    <input type="text" name="name" class="form-control" value="${product.name}" required>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Mã SKU:</label>
                        <input type="text" name="sku" class="form-control" value="${product.sku}" required>
                    </div>
                    <div class="form-group">
                        <label>Số lượng kho ban đầu:</label>
                        <input type="number" name="stock" class="form-control" value="${product.stockQuantity}" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group">
                        <label>Giá niêm yết (vnđ):</label>
                        <input type="number" name="originalPrice" class="form-control"
                               value="${empty product ? '' : Double.valueOf(product.originalPrice).longValue()}" required>
                    </div>
                    <div class="form-group">
                        <label>Giá bán thực tế (vnđ):</label>
                        <input type="number" name="price" class="form-control"
                               value="${empty product ? '' : Double.valueOf(product.currentPrice).longValue()}" required>
                    </div>
                </div>
                <div class="form-row">
                    <div class="form-group" style="background: #fff3cd; padding: 10px; border-radius: 5px; border: 1px solid #ffeeba;">
                        <label style="color: #856404; font-weight: bold;">Tùy chọn hiển thị:</label>
                        <div style="display: flex; align-items: center; gap: 10px; margin-top: 5px;">
                            <input type="checkbox" id="isLuxury" name="isLuxury" value="true"
                                   style="width: 20px; height: 20px; cursor: pointer;"
                            ${product != null && product.isLuxury() ? 'checked' : ''}>

                            <label for="isLuxury" style="margin: 0; cursor: pointer;">
                                Đánh dấu là <strong>Sản phẩm Luxury</strong> (Hiển thị nổi bật trang chủ)
                            </label>
                        </div>
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h3>2. Hình ảnh</h3>
                <div class="form-row">
                    <div class="form-group">
                        <label style="color: #d0011b;">Ảnh Đại Diện (Bắt buộc):</label>
                        <c:if test="${not empty product.imageUrl}">
                            <div style="margin-bottom: 5px;">
                                <img src="${product.imageUrl.startsWith('http') ? product.imageUrl : pageContext.request.contextPath.concat('/').concat(product.imageUrl)}"
                                     style="height: 80px; border: 1px solid #ddd; padding: 2px; border-radius: 4px;">
                            </div>
                        </c:if>
                        <input type="file" name="mainImage" class="form-control" accept="image/*">
                        <small style="color: #666;">Chỉ chọn file mới nếu muốn thay đổi ảnh đại diện.</small>
                    </div>

                    <div class="form-group">
                        <label>Danh sách ảnh phụ (Chi tiết):</label>
                        <c:if test="${not empty detailImages}">
                            <div style="margin-bottom: 5px; display: flex; gap: 5px; flex-wrap: wrap;">
                                <c:forEach items="${detailImages}" var="img">
                                    <img src="${img.startsWith('http') ? img : pageContext.request.contextPath.concat('/').concat(img)}"
                                         style="height: 60px; border: 1px solid #ddd; border-radius: 4px;">
                                </c:forEach>
                            </div>
                        </c:if>
                        <input type="file" name="detailImages" class="form-control" accept="image/*" multiple>
                        <small style="color: #666;">Chọn lại nhiều file để thay thế toàn bộ ảnh phụ cũ.</small>
                    </div>
                </div>
            </div>

            <div class="form-section">
                <h3>3. Thông số kỹ thuật</h3>

                <div class="form-row">
                    <div class="form-group">
                        <label>Thương hiệu:</label>
                        <input type="hidden" name="specName" value="Thương hiệu">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Thương hiệu']}">
                    </div>
                    <div class="form-group">
                        <label>Xuất xứ:</label>
                        <input type="hidden" name="specName" value="Xuất xứ">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Xuất xứ']}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Đối tượng:</label>
                        <input type="hidden" name="specName" value="Đối tượng">
                        <select name="specValue" class="form-control">
                            <option value="Nam" ${specMap['Đối tượng'] == 'Nam' ? 'selected' : ''}>Nam</option>
                            <option value="Nữ" ${specMap['Đối tượng'] == 'Nữ' ? 'selected' : ''}>Nữ</option>
                            <option value="Cặp đôi" ${specMap['Đối tượng'] == 'Cặp đôi' ? 'selected' : ''}>Cặp đôi</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chống nước:</label>
                        <input type="hidden" name="specName" value="Chống nước">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Chống nước']}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Loại máy:</label>
                        <input type="hidden" name="specName" value="Loại máy">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Loại máy']}">
                    </div>
                    <div class="form-group">
                        <label>Chất liệu kính:</label>
                        <input type="hidden" name="specName" value="Chất liệu kính">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Chất liệu kính']}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Chất liệu dây:</label>
                        <input type="hidden" name="specName" value="Chất liệu dây">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Chất liệu dây']}">
                    </div>
                    <div class="form-group">
                        <label>Đường kính mặt:</label>
                        <input type="hidden" name="specName" value="Đường kính mặt">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Đường kính mặt']}">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>Size mặt:</label>
                        <input type="hidden" name="specName" value="Size mặt">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Size mặt']}">
                    </div>
                    <div class="form-group">
                        <label>Độ dày:</label>
                        <input type="hidden" name="specName" value="Độ dày">
                        <input type="text" name="specValue" class="form-control" value="${specMap['Độ dày']}">
                    </div>
                </div>
            </div>

            <div class="form-group">
                <label>Mô tả chi tiết bài viết:</label>
                <textarea name="description" class="form-control" rows="6" style="resize: vertical;">${product.description}</textarea>
            </div>

            <div style="margin-top: 30px; text-align: right;">
                <a href="product-manager" class="btn-back"><i class="fa-solid fa-xmark"></i> Hủy bỏ</a>
                <button type="submit" class="btn-save">
                    <i class="fa-solid fa-floppy-disk"></i> ${empty product ? 'Lưu sản phẩm mới' : 'Cập nhật sản phẩm'}
                </button>
            </div>
        </form>
    </div>
</div>

</body>
</html>