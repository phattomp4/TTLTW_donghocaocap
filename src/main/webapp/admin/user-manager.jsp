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

        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; z-index: 100;}
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; transition: 0.2s;}
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; padding-left: 30px;}
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: 100%; box-sizing: border-box;}

        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
        .search-box { display: flex; background: white; border: 1px solid #ddd; border-radius: 5px; overflow: hidden; }
        .search-box input { border: none; padding: 10px; outline: none; width: 300px; }
        .search-box button { background: #343a40; color: white; border: none; padding: 0 15px; cursor: pointer; transition: 0.2s;}
        .search-box button:hover { background: #1b6e76; }

        .table-container { background: white; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.05); overflow: hidden; }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; vertical-align: middle; }
        th { background: #343a40; color: white; text-transform: uppercase; font-size: 13px; letter-spacing: 0.5px; }
        tr:hover { background-color: #fcfcfc; }

        .avatar-circle { width: 40px; height: 40px; background: #1b6e76; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-weight: bold; }

        .badge { padding: 5px 10px; border-radius: 20px; font-size: 11px; font-weight: 600; text-transform: uppercase; display: inline-block;}
        .badge-active { background: #d4edda; color: #155724; border: 1px solid #c3e6cb; }
        .badge-locked { background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; }
        .badge-role { background: #e2e3e5; color: #383d41; border: 1px solid #d6d8db; }

        .role-select { padding: 6px 10px; border: 1px solid #ccc; border-radius: 4px; background: #fff; font-size: 13px; cursor: pointer; outline: none; }
        .btn-save-role { background: #28a745; color: white; border: none; padding: 6px 10px; border-radius: 4px; cursor: pointer; margin-left: 5px; transition: 0.2s; }
        .btn-save-role:hover { background: #218838; }

        .btn-action { padding: 8px 12px; border: none; border-radius: 4px; cursor: pointer; color: white; transition: 0.2s; font-size: 13px; }
        .btn-lock { background: #dc3545; }
        .btn-lock:hover { background: #c82333; }
        .btn-unlock { background: #28a745; }
        .btn-unlock:hover { background: #218838; }

        .page-link { padding: 8px 16px; text-decoration: none; border: 1px solid #ddd; color: #333; border-radius: 4px; transition: all 0.3s; background: white; font-weight: 500; }
        .page-link.active { background-color: #1b6e76; color: white; border-color: #1b6e76; }
        .page-link:hover:not(.active) { background-color: #f1f1f1; border-color: #ccc; }
        .modal-overlay { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 1000; align-items: center; justify-content: center; backdrop-filter: blur(3px); }
        .modal-content { background: #fff; width: 450px; border-radius: 8px; overflow: hidden; box-shadow: 0 10px 30px rgba(0,0,0,0.3); animation: slideDown 0.3s ease-out; }
        .modal-header { background: #1b6e76; color: white; padding: 18px 20px; display: flex; justify-content: space-between; align-items: center; }
        .modal-body { padding: 25px; }
        .modal-close { background: none; border: none; color: white; font-size: 22px; cursor: pointer; opacity: 0.8; transition: 0.2s;}
        .modal-close:hover { opacity: 1; transform: scale(1.1); }
        @keyframes slideDown { from { transform: translateY(-30px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
        .stat-box { background: #f8f9fa; padding: 15px; border-radius: 6px; text-align: center; border: 1px solid #eee; margin-bottom: 15px;}
        .stat-box h4 { margin: 0 0 8px 0; font-size: 13px; color: #666; text-transform: uppercase; letter-spacing: 0.5px;}
        .stat-box p { margin: 0; font-size: 24px; font-weight: 900; color: #333; }
        .loader { border: 4px solid #f3f3f3; border-top: 4px solid #1b6e76; border-radius: 50%; width: 30px; height: 30px; animation: spin 1s linear infinite; margin: 0 auto 10px auto; }
        @keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }
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
        <a href="user-manager"  class="active"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
        <a href="voucher-manager"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
        <a href="interface-manager"><i class="fa-solid fa-paintbrush"></i> Quản lý Giao diện</a>
        <a href="category-manager"><i class="fa-solid fa-list"></i> Quản lý tìm kiếm</a>
        <a href="warehouse"><i class="fa-solid fa-boxes-stacked"></i> Quản lý Kho</a>
    </c:if>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ web</a>
</div>

<div class="content">
    <div class="page-header">
        <h2 style="border-left: 5px solid #1b6e76; padding-left: 15px; color: #333; margin: 0;">Danh sách khách hàng</h2>

        <form action="user-manager" method="GET" class="search-box">
            <input type="text" name="keyword" placeholder="Tìm theo tên, email hoặc SĐT..." value="${searchKeyword}">
            <button type="submit"><i class="fa-solid fa-magnifying-glass"></i></button>
        </form>
    </div>

    <c:if test="${not empty param.msg}">
        <div style="padding: 12px; margin-bottom: 20px; border-radius: 4px; font-weight: bold;
                    ${param.msg == 'success' ? 'background: #d4edda; color: #155724; border: 1px solid #c3e6cb;' : 'background: #f8d7da; color: #721c24; border: 1px solid #f5c6cb;'}">
            <c:choose>
                <c:when test="${param.msg == 'success'}"><i class="fa-solid fa-circle-check"></i> Thao tác cập nhật thành công!</c:when>
                <c:otherwise><i class="fa-solid fa-circle-xmark"></i> Thao tác thất bại!</c:otherwise>
            </c:choose>
        </div>
    </c:if>

    <div class="table-container">
        <table>
            <thead>
            <tr>
                <th style="width: 50px;">ID</th>
                <th style="width: 60px;">Avatar</th>
                <th>Tài khoản</th>
                <th>Họ và tên</th>
                <th>Quyền hạn</th>
                <th>Ngày đăng ký</th>
                <th>Trạng thái</th>
                <th>SĐT</th>
                <c:if test="${sessionScope.acc.role == 'Admin'}">
                    <th style="text-align: center;">Thao tác</th>
                </c:if>
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
                            <c:choose>
                                <c:when test="${not empty u.username}">
                                    <c:out value="${u.username.toUpperCase().charAt(0)}"/>
                                </c:when>
                                <c:otherwise>U</c:otherwise>
                            </c:choose>
                        </div>
                    </td>

                    <td style="font-weight: bold;">
                        <a href="javascript:void(0)" onclick="viewReputation(${u.id}, '${u.username}')"
                           style="color: #1b6e76; text-decoration: none; transition: 0.2s;"
                           title="Xem chỉ số mua sắm">
                            <i class="fa-solid fa-chart-simple" style="margin-right: 5px;"></i>${u.username}
                        </a>
                    </td>

                    <td>
                            ${u.fullName} <br>
                        <span style="font-size: 12px; color: #888;">${u.email}</span>
                    </td>

                    <td>
                        <c:choose>
                            <%-- Admin được đổi quyền --%>
                            <c:when test="${sessionScope.acc.role == 'Admin'}">
                                <form action="user-manager" method="POST" style="display: flex; align-items: center; margin: 0;">
                                    <input type="hidden" name="action" value="updateRole">
                                    <input type="hidden" name="userId" value="${u.id}">
                                    <select name="role" class="role-select">
                                        <option value="User" ${u.role == 'User' || u.role == 'user' ? 'selected' : ''}>Khách hàng</option>
                                        <option value="Staff" ${u.role == 'Staff' || u.role == 'staff' ? 'selected' : ''}>Nhân viên</option>
                                        <option value="Admin" ${u.role == 'Admin' || u.role == 'admin' ? 'selected' : ''}>Admin</option>
                                    </select>
                                    <button type="submit" class="btn-save-role" title="Lưu thay đổi">
                                        <i class="fa-solid fa-floppy-disk"></i>
                                    </button>
                                </form>
                            </c:when>
                            <%-- Staff chỉ được xem, không có form --%>
                            <c:otherwise>
                                <span class="badge badge-role">${u.role}</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td style="font-size: 13px; color: #555;">
                        <fmt:formatDate value="${u.createdAt}" pattern="dd/MM/yyyy"/>
                    </td>

                    <td>
                        <c:choose>
                            <c:when test="${u.active}">
                                <span class="badge badge-active">Hoạt động</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge badge-locked">Bị khóa</span>
                            </c:otherwise>
                        </c:choose>
                    </td>

                    <td style="font-size: 13px;">${u.phone}</td>

                    <c:if test="${sessionScope.acc.role == 'Admin'}">
                        <td style="text-align: center;">
                            <form action="user-manager" method="POST" style="margin: 0;">
                                <input type="hidden" name="action" value="toggleLock">
                                <input type="hidden" name="userId" value="${u.id}">
                                <input type="hidden" name="currentStatus" value="${u.active}">

                                <button type="submit" class="btn-action ${u.active ? 'btn-lock' : 'btn-unlock'}"
                                        title="${u.active ? 'Khóa tài khoản này' : 'Mở khóa tài khoản này'}"
                                        onclick="return confirm('Bạn chắc chắn muốn ${u.active ? 'KHÓA' : 'MỞ KHÓA'} tài khoản này?');">
                                    <i class="fa-solid ${u.active ? 'fa-lock' : 'fa-unlock'}"></i>
                                </button>
                            </form>
                        </td>
                    </c:if>
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

<div id="reputationModal" class="modal-overlay">
    <div class="modal-content">
        <div class="modal-header">
            <h3 style="margin:0; font-size:16px;"><i class="fa-solid fa-medal"></i> Chỉ số Uy tín: <span id="md-username"></span></h3>
            <button class="modal-close" onclick="closeModal()"><i class="fa-solid fa-xmark"></i></button>
        </div>
        <div class="modal-body">

            <div id="modalLoading" style="text-align: center; padding: 20px 0;">
                <div class="loader"></div>
                <p style="color: #666; font-size: 14px;">Đang trích xuất dữ liệu...</p>
            </div>

            <div id="modalData" style="display:none; grid-template-columns: 1fr 1fr; gap: 15px;">
                <div class="stat-box" style="grid-column: 1 / 3;">
                    <h4>Tổng Tiền Đã Chi</h4>
                    <p id="md-totalSpent" style="color: #28a745;">0 ₫</p>
                </div>
                <div class="stat-box">
                    <h4>Tổng Đơn Hàng</h4>
                    <p id="md-totalOrders" style="color: #1b6e76;">0</p>
                </div>
                <div class="stat-box">
                    <h4>Tỷ Lệ Hủy / Bom</h4>
                    <p id="md-cancelRate">0%</p>
                </div>
            </div>

        </div>
    </div>
</div>

<script>
    function viewReputation(userId, username) {
        document.getElementById('reputationModal').style.display = 'flex';
        document.getElementById('md-username').innerText = username;

        document.getElementById('modalLoading').style.display = 'block';
        document.getElementById('modalData').style.display = 'none';

        fetch('user-manager?action=getReputation&id=' + userId)
            .then(res => res.json())
            .then(data => {
                document.getElementById('modalLoading').style.display = 'none';
                document.getElementById('modalData').style.display = 'grid';

                document.getElementById('md-totalOrders').innerText = data.TotalOrders + " đơn";

                const formatter = new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' });
                document.getElementById('md-totalSpent').innerText = formatter.format(data.TotalSpent);

                const rateEl = document.getElementById('md-cancelRate');
                rateEl.innerText = data.CancelRate + "%";

                // Cảnh báo đỏ nếu tỷ lệ hủy đơn cao hơn 30%
                if(data.CancelRate > 30) {
                    rateEl.style.color = '#dc3545';
                } else {
                    rateEl.style.color = '#333';
                }
            })
            .catch(err => {
                document.getElementById('modalLoading').innerHTML = '<p style="color:red; font-weight:bold;">Lỗi tải dữ liệu!</p>';
            });
    }

    function closeModal() {
        document.getElementById('reputationModal').style.display = 'none';
    }
</script>

</body>
</html>