<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 02/05/2026
  Time: 2:19 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý Voucher | VVP Admin</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <style>
        body { font-family: 'Segoe UI', sans-serif; display: flex; margin: 0; background: #f4f6f9; }

        /* Sidebar (Giữ nguyên style của bạn) */
        .sidebar { width: 250px; background: #343a40; color: white; min-height: 100vh; padding: 20px 0; position: fixed; }
        .sidebar h2 { text-align: center; margin-bottom: 30px; color: #1b6e76; }
        .sidebar a { display: block; padding: 15px 25px; color: #c2c7d0; text-decoration: none; border-bottom: 1px solid #4b545c; }
        .sidebar a:hover, .sidebar a.active { background-image: linear-gradient(45deg, #1b6e76, #2c96a0, #0e3e43) ; color: white; }
        .sidebar i { margin-right: 10px; width: 20px; text-align: center; }

        .content { margin-left: 250px; padding: 30px; width: 100%; }

        /* Form Container */
        .card { background: white; padding: 25px; border-radius: 8px; box-shadow: 0 0 15px rgba(0,0,0,0.05); margin-bottom: 30px; }
        .card-title { font-size: 18px; font-weight: bold; color: #333; margin-bottom: 20px; border-left: 5px solid #1b6e76; padding-left: 15px; }

        .voucher-form { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
        .form-group { display: flex; flex-direction: column; }
        .form-group label { font-size: 13px; font-weight: 600; color: #666; margin-bottom: 5px; }
        .form-group input, .form-group select { padding: 10px; border: 1px solid #ddd; border-radius: 5px; outline: none; }
        .form-group input:focus { border-color: #1b6e76; }

        .btn-submit { grid-column: span 3; background: #1b6e76; color: white; border: none; padding: 12px; border-radius: 5px; cursor: pointer; font-weight: bold; margin-top: 10px; transition: 0.3s; }
        .btn-submit:hover { background: #145258; }

        /* Table Style */
        .table-container { background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 0 15px rgba(0,0,0,0.05); }
        table { width: 100%; border-collapse: collapse; }
        th, td { padding: 15px; text-align: left; border-bottom: 1px solid #eee; }
        th { background: #343a40; color: white; font-size: 13px; text-transform: uppercase; }

        .badge-code { background: #eef2f3; color: #1b6e76; padding: 5px 10px; border-radius: 4px; font-family: monospace; font-weight: bold; border: 1px dashed #1b6e76; }
        .status-msg { padding: 10px; margin-bottom: 20px; border-radius: 5px; }
        .status-success { background: #d4edda; color: #155724; }
    </style>
</head>
<body>

<div class="sidebar">
    <h2>VVP ADMIN</h2>
    <a href="dashboard"><i class="fa-solid fa-gauge"></i> Tổng quan</a>
    <a href="product-manager"><i class="fa-solid fa-box"></i> Quản lý Sản phẩm</a>
    <a href="user-manager"><i class="fa-solid fa-users"></i> Quản lý Khách hàng</a>
    <a href="voucher-manager" class="active"><i class="fa-solid fa-ticket"></i> Quản lý Voucher</a>
    <a href="${pageContext.request.contextPath}/home"><i class="fa-solid fa-house"></i> Về trang chủ</a>
</div>

<div class="content">
    <h2 style="color: #333;">Hệ thống mã giảm giá (Voucher)</h2>

    <c:if test="${param.status == 'success'}">
        <div class="status-msg status-success"><i class="fa-solid fa-circle-check"></i> Thêm Voucher mới thành công!</div>
    </c:if>

    <div class="card">
        <div class="card-title">Tạo mã Voucher mới</div>
        <form action="voucher-manager" method="POST" class="voucher-form">
            <div class="form-group">
                <label>Mã Voucher (Code)</label>
                <input type="text" name="code" placeholder="Ví dụ: GIAM50K" required>
            </div>
            <div class="form-group">
                <label>Loại giảm giá</label>
                <select name="discountType">
                    <option value="Fixed">Số tiền mặt (đ)</option>
                    <option value="Percent">Phần trăm (%)</option>
                </select>
            </div>
            <div class="form-group">
                <label>Mức giảm (Số tiền/%)</label>
                <input type="number" name="discountValue" required>
            </div>
            <div class="form-group">
                <label>Giảm tối đa (Nếu là %)</label>
                <input type="number" name="maxDiscount" value="0">
            </div>
            <div class="form-group">
                <label>Tổng lượt sử dụng</label>
                <input type="number" name="usageLimit" required>
            </div>
            <div class="form-group">
                <label>Giá trị đơn hàng tối thiểu</label>
                <input type="number" name="minOrderValue" value="0">
            </div>
            <div class="form-group">
                <label>Ngày bắt đầu</label>
                <input type="datetime-local" name="startDate" required>
            </div>
            <div class="form-group">
                <label>Ngày hết hạn</label>
                <input type="datetime-local" name="endDate" required>
            </div>

            <button type="submit" class="btn-submit">LƯU VOUCHER VÀO HỆ THỐNG</button>
        </form>
    </div>

    <div class="card">
        <div class="card-title">Lịch sử User đã dùng Voucher</div>
        <div class="table-container">
            <table>
                <thead>
                <tr>
                    <th>Mã Voucher</th>
                    <th>Tài khoản</th>
                    <th>Thời điểm dùng</th>
                    <th>Tổng đơn hàng</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty voucherHistory}">
                    <tr>
                        <td colspan="4" style="text-align: center; padding: 30px; color: #999;">Chưa có dữ liệu sử dụng.</td>
                    </tr>
                </c:if>
                <c:forEach items="${voucherHistory}" var="h">
                    <tr>
                        <td><span class="badge-code">${h.code}</span></td>
                        <td><strong>${h.username}</strong></td>
                        <td><fmt:formatDate value="${h.usedAt}" pattern="dd/MM/yyyy HH:mm"/></td>
                        <td style="color: #d0011b; font-weight: bold;">
                            <fmt:formatNumber value="${h.orderTotal}" type="currency" currencySymbol="₫"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

</body>
</html>