<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 14px; }
        tr:hover { background-color: #f9f9f9; }

        .avatar-circle { width: 40px; height: 40px; background: #eee; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #777; font-weight: bold; }
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
                <th>Email</th>
                <th>Số điện thoại</th>
                <th>Địa chỉ</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty listUsers}">
                <tr>
                    <td colspan="7" style="text-align: center; padding: 30px; color: #777;">
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
                    <td>${u.email}</td>
                    <td>${u.phone}</td>
                    <td style="color: #666; font-size: 13px;">
                            ${u.address}
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

</body>
</html>