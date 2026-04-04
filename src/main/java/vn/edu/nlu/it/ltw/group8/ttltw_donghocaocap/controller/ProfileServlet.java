package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.mindrot.jbcrypt.BCrypt;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao.UserDAO;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.EmailUtil;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
@MultipartConfig(maxFileSize = 1024 * 1024 * 5)
public class ProfileServlet extends HttpServlet {

    private final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "dnrpxyuwo",
            "api_key", "261138144329333",
            "api_secret", "beBh1tv2UJYTuS8CWkVmKS48CO4"));

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        UserDAO dao = new UserDAO();
        String action = request.getParameter("action");
        String idRaw = request.getParameter("id");
        if ("delete".equals(action) && idRaw != null) {

            dao.deleteAddress(Integer.parseInt(idRaw));
            session.setAttribute("mess", "Đã xóa địa chỉ thành công!");
            response.sendRedirect("profile");
            return;
        }
        List<UserAddress> listAddress = dao.getAddresses(acc.getId());

        request.setAttribute("listAddress", listAddress);
        request.getRequestDispatcher("user/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        User acc = (User) session.getAttribute("acc");

        if (acc == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        UserDAO dao = new UserDAO();

        if ("updateInfo".equals(action)) {
            String fullName = request.getParameter("fullname");
            String gender = request.getParameter("gender");
            acc.setFullName(fullName);
            acc.setGender(gender);
            dao.updateAccountProfile(acc);
            session.setAttribute("acc", acc);
            session.setAttribute("mess", "Cập nhật hồ sơ thành công!");
        }

        else if ("uploadAvatar".equals(action)) {
            Part filePart = request.getPart("avatarFile");
            if (filePart != null && filePart.getSize() > 0) {
                try {
                    byte[] imageBytes = filePart.getInputStream().readAllBytes();
                    Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.asMap(
                            "folder", "vvp_avatar"
                    ));
                    String avatarUrl = (String) uploadResult.get("secure_url");
                    dao.updateAvatar(acc.getId(), avatarUrl);
                    acc.setAvatar(avatarUrl);
                    session.setAttribute("acc", acc);
                    session.setAttribute("mess", "Cập nhật ảnh đại diện thành công!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        else if ("requestChangeEmail".equals(action)) {
            String currentPassword = request.getParameter("password");
            String newEmail = request.getParameter("newEmail");

            User dbUser = dao.getUserById(acc.getId());

            if (!BCrypt.checkpw(currentPassword, dbUser.getPassword())) {
                session.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
                session.setAttribute("activeModal", "emailOverlayModal");
                response.sendRedirect("profile");
                return;
            }

            if (newEmail.equals(dbUser.getEmail())) {
                session.setAttribute("error", "Email mới không được trùng với Email hiện tại!");
                session.setAttribute("activeModal", "emailOverlayModal");
                response.sendRedirect("profile");
                return;
            }

            if (dao.checkEmailExist(newEmail) != null) {
                session.setAttribute("error", "Email này đã được đăng ký cho một tài khoản khác!");
                response.sendRedirect("profile");
                return;
            }

            long currentTime = System.currentTimeMillis();
            session.setAttribute("otpFirstRequestTime", currentTime);
            session.setAttribute("otpRequestCount", 1);
            session.setAttribute("lastOtpRequestTime", currentTime);

            String plainOtp = String.format("%06d", new Random().nextInt(999999));
            String hashedOtp = BCrypt.hashpw(plainOtp, BCrypt.gensalt());
            session.setAttribute("contactOtpHash", hashedOtp);

            session.setAttribute("pendingEmail", newEmail);
            session.setAttribute("updateType", "email");

            EmailUtil.sendMail(newEmail, "Mã OTP xác thực thay đổi Email",
                    "<h3>Xác thực thay đổi Email VVP Store</h3><p>Mã OTP của bạn là: <b style='color:red; font-size:20px;'>" + plainOtp + "</b></p><p>Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>");
            session.setAttribute("showOtpModal", true);
        }

        else if ("requestChangePhone".equals(action)) {
            String currentPassword = request.getParameter("password");
            String newPhone = request.getParameter("newPhone");

            User dbUser = dao.getUserById(acc.getId());

            if (!BCrypt.checkpw(currentPassword, dbUser.getPassword())) {
                session.setAttribute("error", "Mật khẩu hiện tại không chính xác!");
                session.setAttribute("activeModal", "phoneOverlayModal");
                response.sendRedirect("profile");
                return;
            }

            if (newPhone.equals(dbUser.getPhone())) {
                session.setAttribute("error", "Số điện thoại mới không được trùng với Số điện thoại hiện hành!");
                session.setAttribute("activeModal", "phoneOverlayModal");
                response.sendRedirect("profile");
                return;
            }

            long currentTime = System.currentTimeMillis();
            session.setAttribute("otpFirstRequestTime", currentTime);
            session.setAttribute("otpRequestCount", 1);
            session.setAttribute("lastOtpRequestTime", currentTime);

            String plainOtp = String.format("%06d", new Random().nextInt(999999));
            String hashedOtp = BCrypt.hashpw(plainOtp, BCrypt.gensalt());
            session.setAttribute("contactOtpHash", hashedOtp);

            session.setAttribute("pendingPhone", newPhone);
            session.setAttribute("updateType", "phone");

            EmailUtil.sendMail(acc.getEmail(), "[Giả lập SMS] Xác thực Số điện thoại",
                    "<h3>Xác thực thay đổi Số điện thoại</h3><p>Mã OTP của bạn là: <b style='color:blue; font-size:20px;'>" + plainOtp + "</b></p>");
            session.setAttribute("showOtpModal", true);
        }

        else if ("resendOtp".equals(action)) {
            response.setContentType("text/plain;charset=UTF-8");
            long currentTime = System.currentTimeMillis();

            Long firstRequestTime = (Long) session.getAttribute("otpFirstRequestTime");
            Integer requestCount = (Integer) session.getAttribute("otpRequestCount");
            Long lastRequestTime = (Long) session.getAttribute("lastOtpRequestTime");

            if (firstRequestTime != null && (currentTime - firstRequestTime) > 10 * 60 * 1000) {
                requestCount = 0;
                firstRequestTime = currentTime;
            }
            if (requestCount == null) requestCount = 0;

            if (requestCount >= 3) {
                response.getWriter().write("ERROR: Bạn đã yêu cầu gửi lại quá 3 lần. Vui lòng thử lại sau 10 phút để tránh bị khóa.");
                return;
            }

            if (lastRequestTime != null && (currentTime - lastRequestTime) < 60000) {
                long wait = 60 - (currentTime - lastRequestTime) / 1000;
                response.getWriter().write("ERROR: Vui lòng đợi " + wait + " giây trước khi yêu cầu mã mới.");
                return;
            }

            String plainOtp = String.format("%06d", new Random().nextInt(999999));
            String hashedOtp = BCrypt.hashpw(plainOtp, BCrypt.gensalt());

            session.setAttribute("contactOtpHash", hashedOtp);
            session.setAttribute("lastOtpRequestTime", currentTime);
            session.setAttribute("otpRequestCount", requestCount + 1);
            session.setAttribute("otpFirstRequestTime", firstRequestTime);

            String updateType = (String) session.getAttribute("updateType");
            if ("email".equals(updateType)) {
                String pendingEmail = (String) session.getAttribute("pendingEmail");
                EmailUtil.sendMail(pendingEmail, "Mã OTP xác thực thay đổi Email",
                        "<h3>Xác thực thay đổi Email VVP Store</h3><p>Mã OTP mới của bạn là: <b style='color:red; font-size:20px;'>" + plainOtp + "</b></p><p>Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>");
            } else if ("phone".equals(updateType)) {
                EmailUtil.sendMail(acc.getEmail(), "[Giả lập SMS] Xác thực Số điện thoại",
                        "<h3>Xác thực thay đổi Số điện thoại</h3><p>Mã OTP mới của bạn là: <b style='color:blue; font-size:20px;'>" + plainOtp + "</b></p>");
            }

            response.getWriter().write("SUCCESS: Mã OTP mới đã được gửi!");
            return;
        }

        else if ("verifyContactOtp".equals(action)) {
            String inputOtp = request.getParameter("otp");
            String sessionOtpHash = (String) session.getAttribute("contactOtpHash");
            String updateType = (String) session.getAttribute("updateType");

            if (sessionOtpHash != null && BCrypt.checkpw(inputOtp, sessionOtpHash)) {
                String oldEmail = acc.getEmail();

                if ("email".equals(updateType)) {
                    String pendingEmail = (String) session.getAttribute("pendingEmail");
                    dao.updateContactInfo(acc.getId(), pendingEmail, acc.getPhone());
                    acc.setEmail(pendingEmail);

                    EmailUtil.sendMail(oldEmail, "CẢNH BÁO BẢO MẬT: Thay đổi thông tin",
                            "<p>CẢNH BÁO: Email của tài khoản VVP Store của bạn vừa bị thay đổi. Nếu không phải bạn thực hiện, vui lòng liên hệ Admin ngay lập tức!</p>");
                    session.setAttribute("mess", "Cập nhật Email thành công!");
                } else if ("phone".equals(updateType)) {
                    String pendingPhone = (String) session.getAttribute("pendingPhone");
                    dao.updateContactInfo(acc.getId(), acc.getEmail(), pendingPhone);
                    acc.setPhone(pendingPhone);

                    EmailUtil.sendMail(oldEmail, "CẢNH BÁO BẢO MẬT: Thay đổi thông tin",
                            "<p>CẢNH BÁO: SĐT của tài khoản VVP Store của bạn vừa bị thay đổi. Nếu không phải bạn thực hiện, vui lòng liên hệ Admin ngay lập tức!</p>");
                    session.setAttribute("mess", "Cập nhật Số điện thoại thành công!");
                }

                session.setAttribute("acc", acc);

                session.removeAttribute("contactOtpHash");
                session.removeAttribute("otpRequestCount");
                session.removeAttribute("otpFirstRequestTime");
                session.removeAttribute("lastOtpRequestTime");
                session.removeAttribute("pendingEmail");
                session.removeAttribute("pendingPhone");
                session.removeAttribute("updateType");

            } else {
                session.setAttribute("error", "Mã OTP không chính xác hoặc đã bị hủy!");
                session.setAttribute("showOtpModal", true);
            }
        }

        else if ("changePassword".equals(action)) {
            String oldPass = request.getParameter("oldPassword");
            String newPass = request.getParameter("newPassword");
            String confirmPass = request.getParameter("confirmPassword");

            User dbUser = dao.getUserById(acc.getId());
            if (!BCrypt.checkpw(oldPass, dbUser.getPassword())) {
                session.setAttribute("error", "Mật khẩu hiện tại không đúng!");
            } else if (!newPass.equals(confirmPass)) {
                session.setAttribute("error", "Mật khẩu xác nhận không khớp!");
            } else {
                String newHash = BCrypt.hashpw(newPass, BCrypt.gensalt(12));
                dao.updatePassword(acc.getId(), newHash);
                session.setAttribute("mess", "Đổi mật khẩu thành công!");
            }
        }

        else if ("addAddress".equals(action)) {
            String name = request.getParameter("new_name");
            String phone = request.getParameter("new_phone");

            String province = request.getParameter("provinceName");
            String district = request.getParameter("districtName");
            String ward = request.getParameter("wardName");
            String streetDetail = request.getParameter("streetDetail");

            String phoneRegex = "^0(3|5|7|8|9)[0-9]{8}$";
            if (phone == null || !phone.matches(phoneRegex)) {
                session.setAttribute("error", "Số điện thoại không hợp lệ! Vui lòng nhập lại.");
                response.sendRedirect("profile");
                return;
            }

            dao.addAddress(acc.getId(), name, phone, province, district, ward, streetDetail);
            session.setAttribute("mess", "Thêm địa chỉ mới thành công!");
        }

        else if ("editAddress".equals(action)) {
            try {
                int addrId = Integer.parseInt(request.getParameter("edit_id"));
                String name = request.getParameter("edit_name");
                String phone = request.getParameter("edit_phone");

                String province = request.getParameter("provinceName");
                String district = request.getParameter("districtName");
                String ward = request.getParameter("wardName");
                String streetDetail = request.getParameter("streetDetail");

                dao.updateUserAddress(addrId, name, phone, province, district, ward, streetDetail);
                session.setAttribute("mess", "Cập nhật địa chỉ thành công!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("profile");
    }
}