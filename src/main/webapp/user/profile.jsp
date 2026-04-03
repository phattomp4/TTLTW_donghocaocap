<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Hồ sơ cá nhân | VVP Store</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/footer.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
</head>
<body>
<jsp:include page="../WEB-INF/tags/header.jsp" />

<div class="profile-container" style="margin-top: 5px;">
    <div class="profile-sidebar">
        <div class="user-brief">
            <img src="${not empty sessionScope.acc.avatar ? sessionScope.acc.avatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}" class="user-avatar-img" alt="Avatar">
            <div>
                <strong>${sessionScope.acc.username}</strong>
            </div>
        </div>
        <div class="sidebar-menu">
            <a href="${pageContext.request.contextPath}/profile" class="active"><i class="fa-regular fa-user"></i> Hồ sơ của tôi</a>
            <a href="${pageContext.request.contextPath}/order-history"><i class="fa-solid fa-clock-rotate-left"></i> Lịch sử đơn hàng</a>
            <a href="logout"><i class="fa-solid fa-arrow-right-from-bracket"></i> Đăng xuất</a>
        </div>
    </div>

    <div class="profile-content">
        <div class="profile-header">
            <h2>Hồ sơ của tôi</h2>
            <p>Quản lý thông tin hồ sơ để bảo mật tài khoản</p>
        </div>

        <c:if test="${not empty mess}">
            <div class="success-message"><i class="fa-solid fa-check-circle"></i> ${mess}</div>
        </c:if>

        <div class="profile-body-grid">
            <c:if test="${not empty sessionScope.error}">
                <div class="error-message" style="color: red; margin-bottom: 15px;"><i class="fa-solid fa-triangle-exclamation"></i> ${sessionScope.error}</div>
                <c:remove var="error" scope="session"/>
            </c:if>

            <div class="info-column">
                <div class="avatar-section" style="text-align: center; margin-bottom: 20px; padding-bottom: 20px; border-bottom: 1px solid #eee;">
                    <img src="${not empty sessionScope.acc.avatar ? sessionScope.acc.avatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}"
                         alt="Avatar" style="width: 100px; height: 100px; border-radius: 50%; object-fit: cover; border: 2px solid #1b6e76;">
                    <form action="profile" method="POST" enctype="multipart/form-data" style="margin-top: 10px;">
                        <input type="hidden" name="action" value="uploadAvatar">
                        <input type="file" name="avatarFile" accept="image/*" required style="font-size: 13px;">
                        <button type="submit" class="btn-save" style="padding: 5px 10px; font-size: 13px;">Cập nhật Ảnh</button>
                    </form>
                </div>

                <form action="profile" method="POST">
                    <input type="hidden" name="action" value="updateInfo">

                    <div class="form-group">
                        <label>Tên đăng nhập</label>
                        <input type="text" class="form-control" value="${sessionScope.acc.username}" disabled>
                    </div>
                    <div class="form-group">
                        <label>Họ và tên</label>
                        <input type="text" name="fullname" class="form-control" value="${sessionScope.acc.fullName}">
                    </div>
                    <div class="form-group">
                        <label>Giới tính</label>
                        <div class="radio-group">
                            <label><input type="radio" name="gender" value="Nam" ${sessionScope.acc.gender == 'Nam' ? 'checked' : ''}> Nam</label>
                            <label><input type="radio" name="gender" value="Nữ" ${sessionScope.acc.gender == 'Nữ' ? 'checked' : ''}> Nữ</label>
                            <label><input type="radio" name="gender" value="Khác" ${sessionScope.acc.gender == 'Khác' ? 'checked' : ''}> Khác</label>
                        </div>
                    </div>
                    <button type="submit" class="btn-save">Lưu thay đổi cơ bản</button>
                </form>

                <hr style="margin: 30px 0; border-top: 1px dashed #ccc;">

                <h4>Thông tin liên hệ</h4>
                <div class="form-group" style="display: flex; gap: 10px; align-items: center;">
                    <div style="flex-grow: 1;">
                        <label>Email hiện tại:</label>
                        <input type="text" class="form-control" value="${sessionScope.acc.email}" disabled>
                    </div>
                    <div style="flex-grow: 1;">
                        <label>SĐT hiện tại:</label>
                        <input type="text" class="form-control" value="${sessionScope.acc.phone}" disabled>
                    </div>
                </div>
                <button type="button" class="btn-save" style="background-color: #f39c12;" onclick="document.getElementById('securityModal').style.display='block'">
                    <i class="fa-solid fa-shield-halved"></i> Đổi Email / SĐT
                </button>

                <hr style="margin: 30px 0; border-top: 1px dashed #ccc;">

                <h4 style="margin-top: 20px;">Đổi mật khẩu</h4>
                <form action="profile" method="POST">
                    <input type="hidden" name="action" value="changePassword">
                    <div class="form-group">
                        <label>Mật khẩu hiện tại</label>
                        <input type="password" name="oldPassword" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Mật khẩu mới</label>
                        <input type="password" name="newPassword" class="form-control" required pattern="^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d@$!%*?&]{8,}$" title="Tối thiểu 8 ký tự, gồm cả chữ và số">
                    </div>
                    <div class="form-group">
                        <label>Xác nhận mật khẩu mới</label>
                        <input type="password" name="confirmPassword" class="form-control" required>
                    </div>
                    <button type="submit" class="btn-save" style="background-color: #d0011b;">Cập nhật Mật khẩu</button>
                </form>
            </div>

            <div id="securityModal" class="add-address-box" style="display:none; margin-top: 20px; border: 2px solid #f39c12; background: #fffdfa;">
                <h4 style="color: #f39c12;"><i class="fa-solid fa-lock"></i> Xác thực thay đổi liên hệ</h4>
                <form action="profile" method="POST">
                    <input type="hidden" name="action" value="requestChangeContact">
                    <div class="form-group">
                        <label>Mật khẩu hiện tại:</label>
                        <input type="password" name="password" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Email mới:</label>
                        <input type="email" name="newEmail" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Số điện thoại mới:</label>
                        <input type="text" name="newPhone" class="form-control" required pattern="^(03|05|07|08|09)\d{8}$">
                    </div>
                    <div class="btn-row">
                        <button type="submit" class="btn-submit-addr" style="background-color: #f39c12;">Nhận mã OTP</button>
                        <button type="button" class="btn-cancel-addr" onclick="document.getElementById('securityModal').style.display='none'">Hủy</button>
                    </div>
                </form>
            </div>

            <c:if test="${sessionScope.showOtpModal}">
                <div id="otpModal" class="add-address-box" style="margin-top: 20px; border: 2px solid #27ae60; background: #f4fbf7;">
                    <h4 style="color: #27ae60;"><i class="fa-solid fa-key"></i> Nhập mã OTP</h4>
                    <p style="font-size: 13px; color: #555;">Mã OTP đã được gửi đến: <b>${sessionScope.pendingEmail}</b></p>
                    <form action="profile" method="POST">
                        <input type="hidden" name="action" value="verifyContactOtp">
                        <div class="form-group">
                            <input type="text" name="otp" class="form-control" placeholder="Nhập mã 6 số" required maxlength="6" style="text-align: center; font-size: 20px; letter-spacing: 5px;">
                        </div>
                        <div class="btn-row">
                            <button type="submit" class="btn-submit-addr" style="background-color: #27ae60;">Xác nhận OTP</button>
                            <a href="profile" class="btn-cancel-addr" style="text-decoration: none; text-align: center;">Hủy</a>
                        </div>
                    </form>
                </div>
                <c:remove var="showOtpModal" scope="session"/>
            </c:if>

            <div class="address-column">
                <div class="address-header">
                    <h3>Địa chỉ nhận hàng</h3>
                    <button type="button" class="btn-add-address" onclick="document.getElementById('addAddressForm').style.display='block'">
                        <i class="fa-solid fa-plus"></i> Thêm địa chỉ mới
                    </button>
                </div>

                <div id="addAddressForm" class="add-address-box" style="display:none;">
                    <form action="profile" method="POST">
                        <input type="hidden" name="action" value="addAddress">
                        <input type="text" name="new_name" placeholder="Tên người nhận" required>
                        <input type="text" name="new_phone" placeholder="Số điện thoại" required>
                        <textarea name="new_address" placeholder="Địa chỉ cụ thể (Số nhà, Phường, Quận, TP)" required></textarea>
                        <div class="btn-row">
                            <button type="submit" class="btn-submit-addr">Hoàn thành</button>
                            <button type="button" class="btn-cancel-addr" onclick="document.getElementById('addAddressForm').style.display='none'">Hủy</button>
                        </div>
                    </form>
                </div>

                <div id="editAddressModal" class="add-address-box" style="display:none; margin-top: 20px; border: 1px solid #1b6e76;">
                    <h4 style="margin-top: 0; color: #1b6e76;">Cập nhật địa chỉ</h4>
                    <form action="profile" method="POST">
                        <input type="hidden" name="action" value="editAddress">
                        <input type="hidden" id="edit_id" name="edit_id"> <input type="text" id="edit_name" name="edit_name" placeholder="Tên người nhận" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                        <input type="text" id="edit_phone" name="edit_phone" placeholder="Số điện thoại" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                        <textarea id="edit_address" name="edit_address" placeholder="Địa chỉ cụ thể" required class="form-control" style="width: 100%; margin-bottom: 10px;"></textarea>

                        <div class="btn-row">
                            <button type="submit" class="btn-submit-addr">Lưu thay đổi</button>
                            <button type="button" class="btn-cancel-addr" onclick="document.getElementById('editAddressModal').style.display='none'">Hủy</button>
                        </div>
                    </form>
                </div>

                <div class="address-list">
                    <c:if test="${not empty listAddress}">
                        <c:forEach items="${listAddress}" var="addr">
                            <div class="address-card">
                                <div class="addr-header">
                    <span class="addr-name" style="font-weight: bold; font-size: 15px;">
                        ${addr.name}
                        <c:if test="${addr.defaultAddress}">
                            <span class="badge-default" style="margin-left: 10px; font-size: 11px; color: #d0011b; border: 1px solid #d0011b; padding: 1px 5px; border-radius: 3px;">Mặc định</span>
                        </c:if>
                    </span>

                                    <span class="addr-phone" style="color: #666; font-size: 14px;">
                        <i class="fa-solid fa-phone" style="font-size: 12px;"></i> ${addr.phone}
                    </span>
                                </div>

                                <div class="addr-body" style="margin-top: 8px;">
                                    <p style="color: #444; margin: 0;">${addr.address}</p>
                                </div>

                                <div class="addr-footer" style="margin-top: 10px; border-top: 1px dashed #eee; padding-top: 8px; display: flex; justify-content: flex-end; gap: 15px; align-items: center;">

                                    <c:if test="${!addr.defaultAddress}">
                                        <a href="address?action=set-default&id=${addr.id}"
                                           style="font-size: 13px; color: #007bff; text-decoration: none; margin-right: auto;">
                                            <i class="fa-regular fa-star"></i> Đặt làm mặc định
                                        </a>
                                    </c:if>
                                    <a href="javascript:void(0)"
                                       onclick="openEditModal('${addr.id}', '${addr.name}', '${addr.phone}', '${addr.address}')"
                                       class="btn-edit-addr" style="font-size: 13px; color: #1b6e76; text-decoration: none;">
                                        <i class="fa-solid fa-pen"></i> Sửa
                                    </a>

                                    <a href="profile?action=delete&id=${addr.id}"
                                       onclick="return confirm('Bạn có chắc chắn muốn xóa địa chỉ này?')"
                                       class="btn-delete-addr" style="font-size: 13px; color: #d0011b; text-decoration: none;">
                                        <i class="fa-solid fa-trash"></i> Xóa
                                    </a>
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>

                    <c:if test="${empty listAddress}">
                        <div style="text-align: center; padding: 20px; color: #999; background: #fafafa; border-radius: 4px; border: 1px dashed #ddd;">
                            <i class="fa-solid fa-map-location-dot" style="font-size: 30px; margin-bottom: 10px; display: block;"></i>
                            Bạn chưa lưu địa chỉ nhận hàng nào.
                        </div>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    function openEditModal(id, name, phone, address) {
        // 1. Hiển thị modal sửa
        document.getElementById('editAddressModal').style.display = 'block';

        // 2. Điền dữ liệu cũ vào form
        document.getElementById('edit_id').value = id;
        document.getElementById('edit_name').value = name;
        document.getElementById('edit_phone').value = phone;
        document.getElementById('edit_address').value = address;
    }
</script>

<jsp:include page="../WEB-INF/tags/footer.jsp" />
</body>
</html>