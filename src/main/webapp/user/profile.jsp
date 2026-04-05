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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/avatarProfile.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.2/css/all.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.css">
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

        <c:if test="${not empty sessionScope.error && empty sessionScope.activeModal && empty sessionScope.showOtpModal}">
            <div class="error-message" style="color: red; margin-bottom: 15px;"><i class="fa-solid fa-triangle-exclamation"></i> ${sessionScope.error}</div>
            <c:remove var="error" scope="session"/>
        </c:if>

        <div class="profile-body-grid">

            <div class="info-column">
                <div class="avatar-section" style="text-align: center; margin-bottom: 30px; padding-bottom: 20px; border-bottom: 1px dashed #ccc;">
                    <div class="avatar-wrapper" onclick="document.getElementById('avatarInput').click();">
                        <img src="${not empty sessionScope.acc.avatar ? sessionScope.acc.avatar : 'https://cdn-icons-png.flaticon.com/512/149/149071.png'}" alt="Avatar">
                        <div class="avatar-overlay"><i class="fa-solid fa-camera"></i> Đổi ảnh</div>
                    </div>
                    <input type="file" id="avatarInput" accept="image/*" style="display: none;">
                </div>

                <div id="cropperModal">
                    <div class="cropper-container-box">
                        <h4 style="color: #1b6e76; margin-top: 0;">Chỉnh sửa ảnh đại diện</h4>
                        <div class="img-container">
                            <img id="imageToCrop" src="" alt="Picture">
                        </div>
                        <div class="btn-row" style="justify-content: center; gap: 10px;">
                            <button type="button" class="btn-save" onclick="zoomIn()" style="padding: 8px 15px;"><i class="fa-solid fa-magnifying-glass-plus"></i></button>
                            <button type="button" class="btn-save" onclick="zoomOut()" style="padding: 8px 15px;"><i class="fa-solid fa-magnifying-glass-minus"></i></button>
                            <button type="button" class="btn-cancel-addr" onclick="closeCropperModal()">Hủy</button>
                            <button type="button" class="btn-submit-addr" id="btnUpload" onclick="uploadAvatar()">Cập nhật</button>
                        </div>
                    </div>
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

                <div class="input-with-icon">
                    <div style="flex-grow: 1;">
                        <label>Email hiện tại:</label>
                        <input type="text" class="form-control" value="${sessionScope.acc.email}" disabled style="margin-bottom: 0;">
                    </div>
                    <a href="javascript:void(0)" onclick="openOverlayModal('emailOverlayModal')" title="Đổi Email" style="color: rgb(27, 110, 118); font-size: 22px; margin-top: 25px;">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </a>
                </div>

                <div class="input-with-icon">
                    <div style="flex-grow: 1;">
                        <label>SĐT hiện tại:</label>
                        <input type="text" class="form-control" value="${sessionScope.acc.phone}" disabled style="margin-bottom: 0;">
                    </div>
                    <a href="javascript:void(0)" onclick="openOverlayModal('phoneOverlayModal')" title="Đổi Số điện thoại" style="color: rgb(27, 110, 118); font-size: 22px; margin-top: 25px;">
                        <i class="fa-solid fa-pen-to-square"></i>
                    </a>
                </div>

                <div id="emailOverlayModal" class="overlay-modal">
                    <div class="overlay-modal-content" style="border-top: 5px solid rgb(27, 110, 118);">
                        <i class="fa-solid fa-xmark close-modal-btn" onclick="closeOverlayModal('emailOverlayModal')"></i>
                        <h4 style="color: rgb(27, 110, 118); margin-top: 0;"><i class="fa-solid fa-envelope"></i> Cập nhật Email</h4>
                        <form action="profile" method="POST">
                            <input type="hidden" name="action" value="requestChangeEmail">

                            <div class="form-group">
                                <label>Mật khẩu hiện tại:</label>
                                <div style="position: relative;">
                                    <input type="password" id="emailOldPassword" name="password" class="form-control" required style="padding-right: 40px; margin-bottom: 0;">
                                    <i class="fa-solid fa-eye toggle-password" onclick="togglePasswordVisibility('emailOldPassword', this)"
                                       style="position: absolute; right: 15px; top: 12px; cursor: pointer; color: #888;"></i>
                                </div>
                                <c:if test="${sessionScope.activeModal == 'emailOverlayModal' && not empty sessionScope.error}">
                                    <span style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"><i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}</span>
                                    <c:remove var="error" scope="session"/>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Email mới:</label>
                                <input type="email" id="newEmailInput" name="newEmail" class="form-control" required placeholder="Nhập địa chỉ email mới...">
                                <span id="emailAjaxError" style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"></span>
                            </div>
                            <button type="submit" id="btnSubmitEmail" class="btn-submit-addr" style="background-color: rgb(27, 110, 118); width: 100%;" disabled>Nhận mã OTP qua Email</button>
                        </form>
                    </div>
                </div>

                <div id="phoneOverlayModal" class="overlay-modal">
                    <div class="overlay-modal-content" style="border-top: 5px solid rgb(27, 110, 118);">
                        <i class="fa-solid fa-xmark close-modal-btn" onclick="closeOverlayModal('phoneOverlayModal')"></i>
                        <h4 style="color: rgb(27, 110, 118); margin-top: 0;"><i class="fa-solid fa-phone"></i> Cập nhật Số điện thoại</h4>
                        <form action="profile" method="POST">
                            <input type="hidden" name="action" value="requestChangePhone">

                            <div class="form-group">
                                <label>Mật khẩu hiện tại:</label>
                                <div style="position: relative;">
                                    <input type="password" id="phoneOldPassword" name="password" class="form-control" required style="padding-right: 40px; margin-bottom: 0;">
                                    <i class="fa-solid fa-eye toggle-password" onclick="togglePasswordVisibility('phoneOldPassword', this)"
                                       style="position: absolute; right: 15px; top: 12px; cursor: pointer; color: #888;"></i>
                                </div>
                                <c:if test="${sessionScope.activeModal == 'phoneOverlayModal' && not empty sessionScope.error}">
                                    <span style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"><i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}</span>
                                    <c:remove var="error" scope="session"/>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Số điện thoại mới:</label>
                                <input type="text" id="newPhoneInput" name="newPhone" class="form-control" required placeholder="Nhập số điện thoại mới...">
                                <span id="phoneAjaxError" style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"></span>
                            </div>
                            <button type="submit" id="btnSubmitPhone" class="btn-submit-addr" style="background-color: rgb(27, 110, 118); width: 100%;" disabled>Nhận mã OTP xác thực</button>
                        </form>
                    </div>
                </div>

                <c:if test="${sessionScope.showOtpModal}">
                    <div id="otpOverlayModal" class="overlay-modal" style="display: flex;">
                        <div class="overlay-modal-content" style="border-top: 5px solid rgb(27, 110, 118);">
                            <h4 style="color: rgb(27, 110, 118); margin-top: 0;"><i class="fa-solid fa-key"></i> Xác thực OTP</h4>
                            <p style="font-size: 13px; color: #555;">Mã OTP đã được gửi đến: <b style="color: #d0011b;">
                                    ${sessionScope.updateType == 'email' ? sessionScope.pendingEmail : sessionScope.acc.email}
                            </b></p>
                            <form action="profile" method="POST">
                                <input type="hidden" name="action" value="verifyContactOtp">
                                <div class="form-group">
                                    <input type="text" name="otp" class="form-control" placeholder="Nhập mã 6 số" required maxlength="6" style="text-align: center; font-size: 22px; font-weight: bold;">
                                    <c:if test="${not empty sessionScope.error}">
                                        <span style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block; text-align: center;"><i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}</span>
                                        <c:remove var="error" scope="session"/>
                                    </c:if>
                                </div>

                                <div style="text-align: center; margin-bottom: 15px;">
                                    <button type="button" id="btnResendOtp" style="background: none; border: none; color: #999; cursor: not-allowed; font-size: 13px; text-decoration: underline;" disabled>Gửi lại mã (60s)</button>
                                    <span id="resendMessage" style="display: block; font-size: 12px; margin-top: 5px;"></span>
                                </div>

                                <button type="submit" class="btn-submit-addr" style="background-color: #27ae60; width: 100%; margin-bottom: 10px;">Xác nhận</button>
                                <a href="profile" style="display: block; text-align: center; color: #888; text-decoration: underline; font-size: 13px;">Hủy</a>
                            </form>
                        </div>
                    </div>
                    <c:remove var="showOtpModal" scope="session"/>
                </c:if>

                <hr style="margin: 30px 0; border-top: 1px dashed #ccc;">
                <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 20px;">
                    <h4 style="margin: 0;">Đổi mật khẩu</h4>
                    <button type="button" class="btn-add-address" onclick="openOverlayModal('passwordOverlayModal')" style="background-color: #d0011b; border-color: #d0011b;">
                        <i class="fa-solid fa-lock"></i> Đổi Mật Khẩu Mới
                    </button>
                </div>

                <div id="passwordOverlayModal" class="overlay-modal">
                    <div class="overlay-modal-content" style="border-top: 5px solid #d0011b;">
                        <i class="fa-solid fa-xmark close-modal-btn" onclick="closeOverlayModal('passwordOverlayModal')"></i>
                        <h4 style="color: #d0011b; margin-top: 0;"><i class="fa-solid fa-shield-halved"></i> Cập Nhật Mật Khẩu</h4>
                        <form action="profile" method="POST">
                            <input type="hidden" name="action" value="changePassword">

                            <div class="form-group">
                                <label>Mật khẩu hiện tại:</label>
                                <div style="position: relative;">
                                    <input type="password" id="oldPasswordInput" name="oldPassword" class="form-control" required style="padding-right: 40px; margin-bottom: 0;">
                                    <i class="fa-solid fa-eye toggle-password" onclick="togglePasswordVisibility('oldPasswordInput', this)" style="position: absolute; right: 15px; top: 12px; cursor: pointer; color: #888;"></i>
                                </div>
                                <c:if test="${sessionScope.activeModal == 'passwordOverlayModal' && not empty sessionScope.error}">
                                    <span style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"><i class="fa-solid fa-circle-exclamation"></i> ${sessionScope.error}</span>
                                    <c:remove var="error" scope="session"/>
                                </c:if>
                            </div>

                            <div class="form-group">
                                <label>Mật khẩu mới:</label>
                                <div style="position: relative;">
                                    <input type="password" id="newPasswordInput" name="newPassword" class="form-control" required placeholder="Tối thiểu 8 ký tự, gồm chữ và số" style="padding-right: 40px; margin-bottom: 0;">
                                    <i class="fa-solid fa-eye toggle-password" onclick="togglePasswordVisibility('newPasswordInput', this)" style="position: absolute; right: 15px; top: 12px; cursor: pointer; color: #888;"></i>
                                </div>
                                <span id="newPasswordError" style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"></span>
                            </div>

                            <div class="form-group">
                                <label>Xác nhận mật khẩu mới:</label>
                                <div style="position: relative;">
                                    <input type="password" id="confirmPasswordInput" name="confirmPassword" class="form-control" required placeholder="Nhập lại mật khẩu mới" style="padding-right: 40px; margin-bottom: 0;">
                                    <i class="fa-solid fa-eye toggle-password" onclick="togglePasswordVisibility('confirmPasswordInput', this)" style="position: absolute; right: 15px; top: 12px; cursor: pointer; color: #888;"></i>
                                </div>
                                <span id="confirmPasswordError" style="color: #d0011b; font-size: 13px; margin-top: 5px; display: block;"></span>
                            </div>

                            <button type="submit" id="btnSubmitPassword" class="btn-submit-addr" style="background-color: #d0011b; width: 100%;" disabled>Nhận mã OTP Xác Thực</button>
                        </form>
                    </div>
                </div>
            </div>


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

                        <input type="text" name="new_name" placeholder="Họ và tên người nhận" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                        <input type="text" name="new_phone" placeholder="Số điện thoại" required class="form-control"
                               pattern="^0(3|5|7|8|9)[0-9]{8}$"
                               title="Vui lòng nhập đúng số điện thoại Việt Nam (10 số, bắt đầu bằng 03, 05, 07, 08 hoặc 09)">

                        <select id="province" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Tỉnh/Thành phố</option>
                        </select>

                        <select id="district" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Quận/Huyện</option>
                        </select>

                        <select id="ward" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Phường/Xã</option>
                        </select>

                        <input type="text" name="streetDetail" placeholder="Số nhà, Tên đường" required class="form-control" style="width: 100%; margin-bottom: 10px;">

                        <input type="hidden" id="provinceName" name="provinceName">
                        <input type="hidden" id="districtName" name="districtName">
                        <input type="hidden" id="wardName" name="wardName">

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
                        <input type="hidden" id="edit_id" name="edit_id">

                        <input type="text" id="edit_name" name="edit_name" placeholder="Tên người nhận" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                        <input type="text" id="edit_phone" name="edit_phone" placeholder="Số điện thoại" required class="form-control" style="width: 100%; margin-bottom: 10px;" pattern="^0(3|5|7|8|9)[0-9]{8}$" title="Vui lòng nhập đúng số điện thoại Việt Nam">

                        <select id="edit_province" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Tỉnh/Thành phố</option>
                        </select>

                        <select id="edit_district" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Quận/Huyện</option>
                        </select>

                        <select id="edit_ward" required class="form-control" style="width: 100%; margin-bottom: 10px;">
                            <option value="">Chọn Phường/Xã</option>
                        </select>

                        <input type="text" id="edit_streetDetail" name="streetDetail" placeholder="Số nhà, Tên đường" required class="form-control" style="width: 100%; margin-bottom: 10px;">

                        <input type="hidden" id="edit_provinceName" name="provinceName">
                        <input type="hidden" id="edit_districtName" name="districtName">
                        <input type="hidden" id="edit_wardName" name="wardName">

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
                                    <p style="color: #444; margin: 0;">${addr.streetDetail}, ${addr.ward}, ${addr.district}, ${addr.province}</p>
                                </div>

                                <div class="addr-footer" style="margin-top: 10px; border-top: 1px dashed #eee; padding-top: 8px; display: flex; justify-content: flex-end; gap: 15px; align-items: center;">

                                    <c:if test="${!addr.defaultAddress}">
                                        <a href="address?action=set-default&id=${addr.id}"
                                           style="font-size: 13px; color: #007bff; text-decoration: none; margin-right: auto;">
                                            <i class="fa-regular fa-star"></i> Đặt làm mặc định
                                        </a>
                                    </c:if>
                                    <a href="javascript:void(0)"
                                       onclick="openEditModal('${addr.id}', '${addr.name}', '${addr.phone}', '${addr.streetDetail}', '${addr.ward}', '${addr.district}', '${addr.province}')"
                                       class="btn-edit-addr" style="font-size: 13px; color: #1b6e76; text-decoration: none;">
                                        <i class="fa-solid fa-pen"></i> Sửa
                                    </a>

                                    <a href="address?action=delete&id=${addr.id}"
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


<jsp:include page="../WEB-INF/tags/footer.jsp" />

<script>
    const contextPath = '${pageContext.request.contextPath}';
    const activeModalFromJava = '${sessionScope.activeModal}';
</script>
<c:remove var="activeModal" scope="session"/>

<script src="https://cdnjs.cloudflare.com/ajax/libs/cropperjs/1.5.13/cropper.min.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/addressesProfile.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/avatarProfile.js"></script>
<script src="${pageContext.request.contextPath}/assets/js/profile.js"></script>

</body>
</html>