<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Khách hàng | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">

    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        /* Sidebar */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 25px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        /* Content */
        .content { margin-left: 250px; padding: 30px; width: 100%; }

        /* Search Box */
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .search-box { display: flex; background: white; border: 1px solid #ddd; border-radius: 5px; overflow: hidden; }
        .search-box input { border: none; padding: 10px; outline: none; width: 300px; }
        .search-box button { background: #343a40; color: white; border: none; padding: 0 15px; cursor: pointer; }
        .search-box button:hover { background: #d0011b; }

        /* Table */
        .table-container { background: white; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle; }
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 13px; letter-spacing: 0.5px; }
        tr:hover { background-color: #f9f9f9; }

        .avatar-circle { width: 40px; height: 40px; background: #1b6e76; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; }

        /* Status Badges */
        .badge { padding: 5px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; text-transform: uppercase; }
        .badge-active { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .badge-locked { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }

        /* Pagination */
        .page-link {
            padding: 8px 16px;
            text-decoration: none;
            border: 1px solid #ddd;
            color: #333;
            border-radius: 4px;
            transition: all 0.3s;
            background: white;
            font-weight: 500;
        }
        .page-link.active {
            background-color: #1b6e76;
            color: white;
            border-color: #1b6e76;
        }
        .page-link:hover:not(.active) {
            background-color: #f1f1f1;
            border-color: #ccc;
        }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager" class="active"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>

    <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
    <a href="category-manager"><i class="fa-solid fa-paintbrush"></i> Danh mục & Menu</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div class="page-header">
        <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333;">Danh sách khách hàng</h2>

        <form action="user-manager" method="GET" class="search-box">
            <input type="text" name="keyword" placeholder="Tìm theo tên, email hoặc SĐT..." value="${searchKeyword}">
            <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
    </div>

    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th style="width: 50px;">ID</th>
                <th style="width: 60px;">Avatar</th>
                <th>Tên tài khoản</th>
                <th>Họ và tên</th>
                <th>Ngày đăng ký</th> <th>Trạng thái</th>   <th>Email</th>
                <th>SĐT</th>
                <th>Địa chỉ</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty listUsers}">
                <tr>
                    <td colspan="9" style="text-align: center; padding: 40px; color: #777;">
                        <i class="fa-solid fa-user-slash" style="font-size: 30px; display: block; margin-bottom: 10px;"></i>
                        Không tìm thấy khách hàng nào.
                    </td>
                </tr>
            </c:if>

            <c:forEach items="${listUsers}" var="u">
                <tr>
                    <td><b>#${u.id}</b></td>
                    <td>
                        <div class="avatar-circle">
                                ${u.username.substring(0, 1).toUpperCase()}
                        </div>
                    </td>
                    <td style="font-weight: bold;">
                        <a href="user-detail?id=${u.id}"
                           style="color: #1b6e76; text-decoration: none; transition: 0.2s;"
                           onmouseover="this.style.textDecoration='underline'"
                           onmouseout="this.style.textDecoration='none'">
                                ${u.username}
                        </a>
                    </td>
                    <td>${u.fullName}</td>

                    <td style="font-size: 13px; color: #555;">
                        <fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy"/>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${u.status == 'Active' || u.status == '1'}">
                                <span class="badge badge-active">Hoạt động</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-locked">Bị khóa</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td style="font-size: 13px;">${u.email}</td>
                    <td style="font-size: 13px;">${u.phone}</td>
                    <td style="color: #666; font-size: 12px; max-width: 200px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap;">
                            ${u.address}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

    <c:if test="${totalPages > 1}">
        <div class="pagination" style="display: flex; justify-content: center; margin-top: 25px; gap: 8px;">
            <c:if test="${currentPage > 1}">
                <a href="user-manager?page=${currentPage - 1}&keyword=${searchKeyword}" class="page-link">&laquo; Trước</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
                <a href="user-manager?page=${i}&keyword=${searchKeyword}"
                   class="page-link ${currentPage == i ? 'active' : ''}">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <a href="user-manager?page=${currentPage + 1}&keyword=${searchKeyword}" class="page-link">Sau &raquo;</a>
            </c:if>
        </div>
    </c:if>
</div>

</body>
</html>