<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${pageTitle}</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="assets/css/index.css">
    <link rel="stylesheet" href="assets/css/header.css">
    <link rel="stylesheet" href="assets/css/footer.css">
    <link rel="stylesheet" href="assets/css/ProductCards.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        .card {
            transition: transform 0.3s ease, box-shadow 0.3s ease, border-color 0.3s ease;
            border: 1px solid #eaeaea !important;
        }
        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 8px 15px rgba(0,0,0,0.1) !important;
            border-color: #daa51e !important;
        }
        .card-title {
            transition: color 0.2s ease;
        }
        .card:hover .card-title {
            color: #1b6e76;
        }
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

        .pagination .page-item.active .page-link { background-color: #1b6e76; border-color: #1b6e76; color: white; }
        .pagination .page-link { color: #1b6e76; }
    </style>
</head>
<body>
<jsp:include page="WEB-INF/tags/header.jsp" />

<div class="layout-container">

    <div class="sidebar-filter">
        <h3><i class="fa-solid fa-filter"></i> BỘ LỌC TÌM KIẾM</h3>
        <form action="category" method="GET" id="filterForm">
            <input type="hidden" name="page" id="pageInput" value="${not empty tag ? tag : 1}">
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

            <button type="button" class="btn-filter-submit" onclick="goToPage(1)">ÁP DỤNG BỘ LỌC</button>
        </form>
    </div>

    <div class="main-content">
        <c:if test="${not empty pageTitle}">
            <h2 style="margin-top: 0; margin-bottom: 20px; text-transform: uppercase; color: #1b6e76; font-weight: bold;">${pageTitle}</h2>
        </c:if>

        <div class="row g-3">
            <c:if test="${empty listProduct}">
                <div class="col-12 text-center py-5">
                    <i class="fa-solid fa-box-open" style="font-size: 50px; color: #ccc; margin-bottom: 15px;"></i>
                    <h5 class="text-muted">Không tìm thấy sản phẩm nào phù hợp với bộ lọc.</h5>
                </div>
            </c:if>

            <c:forEach items="${listProduct}" var="p">
                <div class="col-lg-3 col-md-4 col-6">
                    <div class="card h-100 shadow-sm border-0 rounded-3">
                        <a href="${pageContext.request.contextPath}/detail?pid=${p.id}" class="text-center p-3">
                            <img src="${p.imageUrl}" class="card-img-top img-fluid" alt="${p.name}" style="height: 180px; object-fit: contain;">
                        </a>
                        <div class="card-body d-flex flex-column text-start p-3">
                            <a href="${pageContext.request.contextPath}/detail?pid=${p.id}" class="text-decoration-none text-dark mb-2">
                                <h6 class="card-title text-truncate" style="font-size: 14px; font-weight: 600;" title="${p.name}">
                                        ${p.name}
                                </h6>
                            </a>

                            <div class="mt-auto">
                                <p class="text-danger fw-bold mb-1" style="font-size: 17px;">
                                    <fmt:formatNumber value="${p.currentPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                </p>

                                <c:if test="${p.originalPrice > p.currentPrice}">
                                    <p class="text-muted text-decoration-line-through mb-1" style="font-size: 13px;">
                                        <fmt:formatNumber value="${p.originalPrice}" type="currency" currencySymbol="₫" maxFractionDigits="0"/>
                                    </p>
                                </c:if>

                                <p class="text-muted mb-0" style="font-size: 12px;">
                                    <i class="fa-solid fa-cart-shopping me-1"></i> Đã bán ${p.soldQuantity}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <c:if test="${endP > 1}">
            <nav aria-label="Page navigation" class="mt-5">
                <ul class="pagination justify-content-center">

                    <li class="page-item ${tag == 1 ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0);" onclick="goToPage(${tag - 1})">
                            <i class="fa-solid fa-chevron-left"></i> Trước
                        </a>
                    </li>

                    <c:forEach begin="1" end="${endP}" var="i">
                        <c:choose>
                            <c:when test="${i == 1 or i == endP or (i >= tag - 2 and i <= tag + 2)}">
                                <li class="page-item ${tag == i ? 'active' : ''}">
                                    <a class="page-link" href="javascript:void(0);" onclick="goToPage(${i})">${i}</a>
                                </li>
                            </c:when>

                            <c:when test="${i == tag - 3 or i == tag + 3}">
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                            </c:when>
                        </c:choose>
                    </c:forEach>

                    <li class="page-item ${tag == endP ? 'disabled' : ''}">
                        <a class="page-link" href="javascript:void(0);" onclick="goToPage(${tag + 1})">
                            Sau <i class="fa-solid fa-chevron-right"></i>
                        </a>
                    </li>

                </ul>
            </nav>
        </c:if>

    </div>
</div>

<jsp:include page="WEB-INF/tags/footer.jsp" />

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // hàm xử lý chuyển trang và giữ nguyên bộ lọc
    function goToPage(pageNumber) {
        document.getElementById('pageInput').value = pageNumber;
        document.getElementById('filterForm').submit();
    }
</script>
</body>
</html>