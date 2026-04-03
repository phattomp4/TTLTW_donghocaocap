<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<style>
    /* Container của avatar */
    .profile-dropdown {
        position: relative;
        display: inline-block;
    }

    /* Nút Avatar */
    .profile-avatar-btn {
        background: none;
        border: none;
        cursor: pointer;
        padding: 0;
        display: flex;
        align-items: center;
    }

    .profile-avatar-btn img {
        width: 35px;
        height: 35px;
        border-radius: 50%;
        object-fit: cover;
        border: 2px solid #ddd;
        transition: 0.2s;
    }

    .profile-avatar-btn:hover img {
        border-color: #1b6e76;
    }

    /* Menu Dropdown */
    .dropdown-menu {
        display: none;
        position: absolute;
        right: 0;
        top: 120%; /
        background-color: white;
        min-width: 220px;
        box-shadow: 0px 4px 15px rgba(0,0,0,0.15);
        z-index: 99999;
        border-radius: 8px;
        border: 1px solid #eee;
        animation: fadeIn 0.2s;
    }

    /* Class này sẽ được JS thêm vào để hiện menu */
    .dropdown-menu.show {
        display: block;
    }

    /* Style bên trong menu */
    .menu-header {
        padding: 15px;
        border-bottom: 1px solid #f0f0f0;
        background-color: #fafafa;
        font-size: 14px;
        color: #333;
        border-radius: 8px 8px 0 0;
    }

    .dropdown-menu a {
        color: #333;
        padding: 12px 16px;
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 10px;
        font-size: 14px;
        transition: 0.2s;
    }

    .dropdown-menu a i {
        width: 20px;
        text-align: center;
        color: #777;
    }

    .dropdown-menu a:hover {
        background-color: #f5f5f5;
        color: #1b6e76;
    }

    .dropdown-menu .divider {
        height: 1px;
        background-color: #eee;
        margin: 4px 0;
    }

    @keyframes fadeIn {
        from {opacity: 0; transform: translateY(10px);}
        to {opacity: 1; transform: translateY(0);}
    }
</style>
<header class="main-header">

    <!--Logo-->
    <div class="logo-container-header">
        <a href="${pageContext.request.contextPath}/home" class="logo-header"> <h1 class="logo-text" style="font-weight: 900; font-size: 35px;">VVP</h1>
        </a>
    </div>


    <!--Links dieu huong-->
    <div class="nav-item">
        <nav class="main-nav">
            <ul>
                <li class="nav-item-has-dropdown">
                    <a href="category?type=search&keyword=" class="link-yellow">Đồng hồ<i class="fa-solid fa-chevron-down"></i></a>
                    <div class="megamenu megamenu-dongho" style="width: 900px; padding: 25px;">
                        <div style="display: grid; grid-template-columns: 1fr 2.5fr 1fr; gap: 40px;">

                            <div class="megamenu-column">
                                <h4 style="color: #d0011b; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 15px; text-transform: uppercase;">
                                    <i class="fa-solid fa-tags"></i> Theo Giá
                                </h4>
                                <ul style="list-style: none; padding: 0;">
                                    <c:forEach items="${menuPrices}" var="p">
                                        <li style="margin-bottom: 8px;">
                                            <a href="${pageContext.request.contextPath}/category?type=price&min=${p.minPrice}&max=${p.maxPrice}"
                                               style="color: #333; text-decoration: none; transition: 0.2s;">
                                                    ${p.label}
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>

                            <div class="megamenu-column">
                                <h4 style="color: #1b6e76; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 15px; text-transform: uppercase;">
                                    <i class="fa-solid fa-medal"></i> Thương Hiệu
                                </h4>

                                <ul style="list-style: none; padding: 0; columns: 3; -webkit-columns: 3; gap: 20px;">
                                    <c:forEach items="${menuBrands}" var="cat">
                                        <li style="margin-bottom: 10px;">
                                            <a href="${pageContext.request.contextPath}/category?type=brand&name=${cat.name}"
                                               style="color: #555; text-decoration: none; font-weight: 500; display: block; padding: 2px 0;">
                                                    ${cat.name}
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>

                            <div class="megamenu-column">
                                <h4 style="color: #daa51e; border-bottom: 2px solid #eee; padding-bottom: 10px; margin-bottom: 15px; text-transform: uppercase;">
                                    <i class="fa-solid fa-gem"></i> Bộ Sưu Tập
                                </h4>
                                <ul style="list-style: none; padding: 0;">

                                    <c:forEach items="${menuCollections}" var="cat">
                                        <li style="margin-bottom: 10px;">
                                            <a href="${pageContext.request.contextPath}/category?type=collection&name=${cat.name}"
                                               style="color: #333; text-decoration: none;">
                                                    ${cat.name}
                                            </a>
                                        </li>
                                    </c:forEach>
                                </ul>
                            </div>

                        </div>
                    </div>
                </li>

                <li><a href="category?type=nam">Nam</a></li>
                <li><a href="category?type=nu">Nữ</a></li>

                <li class="nav-item-has-dropdown">
                    <a href="category?type=accessories">Phụ kiện<i class="fa-solid fa-chevron-down"></i></a>
                    <div class="megamenu megamenu-phukien">
                        <div class="megamenu-column-phukien">
                            <ul style="list-style: none; padding: 0; margin: 0;">

                                <c:forEach items="${menuAccessories}" var="cat">
                                    <li style="margin-bottom: 8px; border-bottom: 1px dashed #eee; padding-bottom: 5px;">
                                        <a href="${pageContext.request.contextPath}/category?type=search&keyword=${cat.name}"
                                           style="color: #333; text-decoration: none; font-size: 14px; display: block;">
                                             ${cat.name}
                                        </a>
                                    </li>
                                </c:forEach>

                            </ul>
                        </div>
                    </div>
                </li>
            </ul>
        </nav>
    </div>

    <!--Tim kiem, gio hang-->
    <div class="header-action">
        <ul class="ul-header-action">
            <li>
                <div class="search-bar" style="position: relative;">

                    <form action="${pageContext.request.contextPath}/category" method="GET" class="search-form">
                        <input type="hidden" name="type" value="search">

                        <input type="text" name="keyword"
                               oninput="searchByName(this)"
                               placeholder="Tìm kiếm..." required autocomplete="off">

                        <button type="submit" class="search-button">
                            <i class="fa-solid fa-magnifying-glass"></i>
                        </button>
                    </form>

                    <div id="search-results" class="search-results-box">
                    </div>

                </div>
            </li>

            <li>
                <div class="header-action-item">
                    <a href="${pageContext.request.contextPath}/cart" style="position: relative; text-decoration: none; color: inherit;">
                        <i class="fa-solid fa-cart-shopping" style="font-size: 20px;"></i>

                        <span class="cart-count" id="cartCountHeader" style="position: absolute; top: -8px; right: -8px; background: #d0011b; color: white; font-size: 11px; font-weight: bold; padding: 2px 6px; border-radius: 50%;">
                            ${sessionScope.cartCount != null ? sessionScope.cartCount : 0}
                        </span>
                    </a>
                </div>
            </li>

            <li>
                <c:if test="${sessionScope.acc == null}">
                    <div class="container-button-login" style="text-align:center">
                        <a href="${pageContext.request.contextPath}/login.jsp" class="button button-login">
                            <span style="color: #fff">Đăng nhập</span>
                        </a>
                    </div>
                </c:if>

                <c:if test="${sessionScope.acc != null}">
                <div class="profile-dropdown">
                    <button id="profile-btn" class="profile-avatar-btn">
                        <img src="https://cdn-icons-png.flaticon.com/512/149/149071.png" alt="Avatar">
                    </button>

                    <div id="profile-menu" class="dropdown-menu">
                        <div class="menu-header">
                            Xin chào, <br>
                            <b style="color: #1b6e76;">${sessionScope.acc.username}</b>
                        </div>

                        <c:choose>

                            <%-- TRƯỜNG HỢP LÀ ADMIN --%>
                            <c:when test="${sessionScope.acc.role == 'Admin'}">
                                <a href="${pageContext.request.contextPath}/admin/dashboard" style="color: #d0011b; font-weight: bold; background-color: #fff5f5;">
                                    <i class="fa-solid fa-screwdriver-wrench"></i> Trang Quản Trị
                                </a>
                            </c:when>

                            <%-- TRƯỜNG HỢP LÀ USER THƯỜNG--%>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/profile">
                                    <i class="fa-regular fa-user"></i> Hồ sơ cá nhân
                                </a>

                                <a href="${pageContext.request.contextPath}/order-history" class="btn-history">
                                    <i class="fa-solid fa-clock-rotate-left"></i> Lịch sử đơn hàng
                                </a>
                            </c:otherwise>

                        </c:choose>

                        <div class="divider"></div>

                        <a href="${pageContext.request.contextPath}/logout" style="color: #d0011b;">
                            <i class="fa-solid fa-right-from-bracket"></i> Đăng xuất
                        </a>
                    </div>
                </div>
            </c:if>
            </li>
        </ul>
    </div>

    <script>
        function toggleProfileMenu(event) {
            event.stopPropagation();

            var menu = document.getElementById("profile-menu");
            if (menu) {
                menu.classList.toggle("show");
            }
        }

        // click bất kỳ đâu trên màn hình để đóng menu
        window.onclick = function(event) {
            if (!event.target.closest('.profile-avatar-btn')) {
                var dropdowns = document.getElementsByClassName("dropdown-menu");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }
    </script>

</header>