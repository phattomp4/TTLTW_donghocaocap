<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="assets/css/ProductCards.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        .layout-container { display: flex; max-width: 1200px; margin: 150px auto 50px; gap: 30px; padding: 0 15px; }

        .sidebar-filter { width: 25%; background: #fff; border: 1px solid #eaeaea; border-radius: 8px; padding: 20px; height: fit-content; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
        .filter-group { margin-bottom: 20px; border-bottom: 1px dashed #ddd; padding-bottom: 15px; }
        .filter-group h4 { margin-top: 0; margin-bottom: 10px; font-size: 16px; color: #333; text-transform: uppercase; }
        .filter-group label.text-label { display: block; margin-bottom: 8px; cursor: pointer; color: #555; font-size: 14px; }
        .filter-group input[type="radio"], .filter-group input[type="checkbox"].text-check { margin-right: 8px; cursor: pointer; }
        .btn-filter-submit { width: 100%; background: #1b6e76; color: white; border: none; padding: 10px; font-weight: bold; border-radius: 4px; cursor: pointer; transition: 0.3s; }
        .btn-filter-submit:hover { background: #14555b; }

        .brand-grid { display: flex; flex-wrap: wrap; gap: 10px; }
        .brand-item { width: calc(50% - 5px); }
        .brand-item input { display: none; }
        .brand-item label { display: flex; align-items: center; justify-content: center; border: 1px solid #ddd; padding: 5px; height: 50px; cursor: pointer; text-align: center; border-radius: 4px; transition: 0.2s;}
        .brand-item label img { max-width: 100%; max-height: 100%; object-fit: contain; }
        .brand-item input:checked + label { border-color: #1b6e76; border-width: 2px; box-shadow: 0 0 5px rgba(27,110,118,0.3); background-color: #f4fbfb; }

        .main-content { width: 75%; }
    </style>
</head>
<body>
<jsp:include page="WEB-INF/tags/header.jsp" />

<div class="layout-container">

    <div class="sidebar-filter">
        <h3><i class="fa-solid fa-filter"></i> BỘ LỌC TÌM KIẾM</h3>
        <form action="category" method="GET" id="filterForm">
            <input type="hidden" name="action" value="filter">
            <c:if test="${fn:contains(selectedTypes, 'Đồng hồ')}"><input type="hidden" name="typeFilter" value="Đồng hồ"></c:if>
            <c:if test="${fn:contains(selectedTypes, 'Dây')}"><input type="hidden" name="typeFilter" value="Dây"></c:if>
            <c:if test="${fn:contains(selectedTypes, 'Hộp')}"><input type="hidden" name="typeFilter" value="Hộp"></c:if>
            <c:if test="${fn:contains(selectedTypes, 'Phụ kiện')}"><input type="hidden" name="typeFilter" value="Phụ kiện"></c:if>

            <c:if test="${!isAccessoryOnly}">
                <div class="filter-group">
                    <h4>Thương Hiệu</h4>
                    <div class="brand-grid">
                        <c:forEach items="${mapBrands}" var="brand">
                            <div class="brand-item">
                                <input type="checkbox" id="brand_${brand.key}" name="brandFilter" value="${brand.key}" ${fn:contains(selectedBrands, brand.key) ? 'checked' : ''}>
                                <label for="brand_${brand.key}" title="${brand.key}">
                                    <img src="${brand.value}" alt="${brand.key}"
                                         onerror="this.onerror=null; this.style.display='none'; this.parentNode.innerHTML='<span style=\'font-size:12px;font-weight:bold;\'>${brand.key}</span>';">
                                </label>
                            </div>
                        </c:forEach>
                    </div>
                </div>

                <div class="filter-group">
                    <h4>Đối Tượng</h4>
                    <label class="text-label"><input type="checkbox" class="text-check" name="genderFilter" value="Nam" ${fn:contains(selectedGenders, 'Nam') ? 'checked' : ''}> Đồng hồ Nam</label>
                    <label class="text-label"><input type="checkbox" class="text-check" name="genderFilter" value="Nữ" ${fn:contains(selectedGenders, 'Nữ') ? 'checked' : ''}> Đồng hồ Nữ</label>
                    <label class="text-label"><input type="checkbox" class="text-check" name="genderFilter" value="Unisex" ${fn:contains(selectedGenders, 'Unisex') ? 'checked' : ''}> Unisex</label>
                </div>
            </c:if>

            <div class="filter-group" style="${isAccessoryOnly ? 'border-bottom:none;' : ''}">
                <h4>Khoảng Giá</h4>
                <label class="text-label"><input type="radio" name="priceFilter" value="" ${empty selectedPrice ? 'checked' : ''}> Tất cả mức giá</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="under1" ${selectedPrice == 'under1' ? 'checked' : ''}> Dưới 1 triệu</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="1to3" ${selectedPrice == '1to3' ? 'checked' : ''}> Từ 1 - 3 triệu</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="3to6" ${selectedPrice == '3to6' ? 'checked' : ''}> Từ 3 - 6 triệu</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="6to9" ${selectedPrice == '6to9' ? 'checked' : ''}> Từ 6 - 9 triệu</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="9to15" ${selectedPrice == '9to15' ? 'checked' : ''}> Từ 9 - 15 triệu</label>
                <label class="text-label"><input type="radio" name="priceFilter" value="over15" ${selectedPrice == 'over15' ? 'checked' : ''}> Trên 15 triệu</label>
            </div>

            <c:if test="${!isAccessoryOnly}">
                <div class="filter-group">
                    <h4>Bộ Sưu Tập</h4>
                    <label class="text-label"><input type="radio" name="collectionFilter" value="" ${empty selectedCollection ? 'checked' : ''}> Tất cả</label>
                    <label class="text-label"><input type="radio" name="collectionFilter" value="luxury" ${selectedCollection == 'luxury' ? 'checked' : ''}> Đồng hồ Luxury</label>
                    <label class="text-label"><input type="radio" name="collectionFilter" value="noibat" ${selectedCollection == 'noibat' ? 'checked' : ''}> Nổi bật</label>
                    <label class="text-label"><input type="radio" name="collectionFilter" value="banchay" ${selectedCollection == 'banchay' ? 'checked' : ''}> Bán chạy</label>
                </div>
            </c:if>

            <button type="submit" class="btn-filter-submit">ÁP DỤNG BỘ LỌC</button>
        </form>
    </div>

    <div class="main-content">
        <c:if test="${not empty pageTitle}">
            <h2 style="margin-top: 0; margin-bottom: 20px; text-transform: uppercase; color: #1b6e76;">${pageTitle}</h2>
        </c:if>

        <div class="Product-page">
            <c:if test="${empty listProduct}">
                <div style="text-align: center; width: 100%; padding: 50px 0; color: #999;">
                    <i class="fa-solid fa-box-open" style="font-size: 40px; margin-bottom: 10px;"></i>
                    <p>Không tìm thấy sản phẩm nào phù hợp với bộ lọc.</p>
                </div>
            </c:if>

            <c:forEach items="${listProduct}" var="p">
                <div class="link-product">
                    <div class="Product">
                        <a href="${pageContext.request.contextPath}/detail?pid=${p.id}" class="img-product">
                            <img src="${p.imageUrl}" alt="${p.name}">
                        </a>
                        <br />
                        <a href="${pageContext.request.contextPath}/detail?pid=${p.id}"><p class="Item">${p.name}</p></a>
                        <p class="PriceOfPoduct"><fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></p>
                        <div class="Discount-row">
                            <p class="Discount"><fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></p>
                        </div>
                        <p class="Sold">Đã bán ${p.soldQuantity}</p>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<jsp:include page="WEB-INF/tags/footer.jsp" />

</body>
</html>