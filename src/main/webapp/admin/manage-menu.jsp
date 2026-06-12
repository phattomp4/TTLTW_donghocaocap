<%--
  Created by IntelliJ IDEA.
  User: HP
  Date: 12/06/2026
  Time: 10:25 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Quản Lý Menu Động</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.min.js"></script>
    <style>
        .sortable-item { cursor: move; }
        .ui-state-highlight { height: 50px; background-color: #f8f9fa; border: 1px dashed #ccc; }
    </style>
</head>
<body class="container mt-5">

<h2 class="mb-4">Quản Lý Vị Trí & Ẩn Hiện Menu</h2>
<p class="text-muted">* Kéo thả các dòng để thay đổi thứ tự menu hiển thị ở trang chủ.</p>

<table class="table table-bordered table-striped">
    <thead>
    <tr>
        <th width="80">Di chuyển</th>
        <th>Tên Mục Menu (Danh mục)</th>
        <th width="150">Trạng thái (Bật/Tắt)</th>
    </tr>
    </thead>
    <tbody id="sortable-menu">
    <c:forEach items="${listMenus}" var="menu">
        <tr class="sortable-item" data-id="${menu.id}">
            <td class="text-center text-secondary">☰</td>
            <td><strong>${menu.name}</strong></td>
            <td>
                <div class="form-check form-switch">
                    <input class="form-check-input toggle-status" type="checkbox"
                           data-id="${menu.id}" ${menu.isActive ? 'checked' : ''}>
                    <label class="form-check-label">${menu.isActive ? 'Đang hiện' : 'Đang ẩn'}</label>
                </div>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<script>
    $(document).ready(function () {
        // 1. KÍCH HOẠT TÍNH NĂNG KÉO THẢ VỊ TRÍ
        $("#sortable-menu").sortable({
            placeholder: "ui-state-highlight",
            update: function (event, ui) {
                // Lấy ra danh sách ID theo thứ tự mới sau khi kéo
                let orderedIds = [];
                $("#sortable-menu tr").each(function () {
                    orderedIds.push($(this).data("id"));
                });

                // Gửi danh sách ID mới này lên Servlet xử lý cập nhật SortOrder
                $.ajax({
                    url: "menu-manager",
                    type: "POST",
                    data: {
                        action: "updateOrder",
                        ids: orderedIds
                    },
                    success: function (response) {
                        if(response === "success") {
                            console.log("Đã cập nhật vị trí mới!");
                        }
                    }
                });
            }
        });

        // 2. KÍCH HOẠT TÍNH NĂNG BẬT/TẮT NHANH (AJAX)
        $(".toggle-status").change(function () {
            let id = $(this).data("id");
            let isChecked = $(this).is(":checked");
            let label = $(this).next(".form-check-label");

            $.ajax({
                url: "menu-manager",
                type: "POST",
                data: {
                    action: "toggleStatus",
                    id: id,
                    status: isChecked
                },
                success: function (response) {
                    if (response === "success") {
                        if (isChecked) {
                            label.text("Đang hiện");
                        } else {
                            label.text("Đang ẩn");
                        }
                    }
                }
            });
        });
    });
</script>
</body>
</html>