package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.controller;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.servlet.ServletException;
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
                byte[] imageBytes = filePart.getInputStream().readAllBytes();
                Map uploadResult = cloudinary.uploader().upload(imageBytes, ObjectUtils.emptyMap());
                String avatarUrl = uploadResult.get("url").toString();

                dao.updateAvatar(acc.getId(), avatarUrl);
                acc.setAvatar(avatarUrl);
                session.setAttribute("acc", acc);
                session.setAttribute("mess", "Cập nhật ảnh đại diện thành công!");
            }
        }

        else if("requestChangeContact".equals(action)){
            String currentPassword = request.getParameter("password");
            String newEmail = request.getParameter("newEmail");
            String newPhone = request.getParameter("newPhone");

            User dbUser = dao.getUserById(acc.getId());
            if(!BCrypt.checkpw(currentPassword, dbUser.getPassword())){
                session.setAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
                response.sendRedirect("profile");
                return;
            }

            if(!newEmail.equals(dbUser.getEmail()) && dao.checkEmailExist(newEmail) !=null){
                session.setAttribute("error", "Email đã được sử dụng bởi tài khoản khác!");
                response.sendRedirect("profile");
                return;
            }

            String otp = String.format("%6d", new Random().nextInt(999999));
            session.setAttribute("contactOtp", otp);
            session.setAttribute("pendingEmail", newEmail);
            session.setAttribute("pendingPhone", newPhone);

            EmailUtil.sendMail(newEmail, "Mã OTP xác thực thay đổi thông tin của bạn là: " + otp);
            session.setAttribute("mess", "Mã OTP đã được gửi đến " + newEmail);
            session.setAttribute("showOtpModal", true);
        }

        else if("verifyContactOtp".equals(action)){
            String inputOtp = request.getParameter("otp");
            String sessionOtp = (String) session.getAttribute("contactOtp");

            if (sessionOtp != null && sessionOtp.equals(inputOtp)) {
                String pendingEmail = (String) session.getAttribute("pendingEmail");
                String pendingPhone = (String) session.getAttribute("pendingPhone");
                String oldEmail = acc.getEmail();

                dao.updateContactInfo(acc.getId(), pendingEmail, pendingPhone);
                acc.setEmail(pendingEmail);
                acc.setPhone(pendingPhone);
                session.setAttribute("acc", acc);

                EmailUtil.sendMail(oldEmail, "CẢNH BÁO: Email tài khoản VVP Store của bạn vừa được đổi thành " + pendingEmail + ". Nếu không phải bạn thực hiện, vui lòng liên hệ Admin ngay lập tức!");

                session.removeAttribute("contactOtp");
                session.removeAttribute("pendingEmail");
                session.removeAttribute("pendingPhone");
                session.removeAttribute("showOtpModal");

                session.setAttribute("mess", "Cập nhật Email/SĐT thành công!");
            } else {
                session.setAttribute("error", "Mã OTP không chính xác!");
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
            String addr = request.getParameter("new_address");

            dao.addAddress(acc.getId(), name, phone, addr);

            session.setAttribute("mess", "Thêm địa chỉ mới thành công!");
        }

        else if ("editAddress".equals(action)) {
            try {
                int addrId = Integer.parseInt(request.getParameter("edit_id"));
                String name = request.getParameter("edit_name");
                String phone = request.getParameter("edit_phone");
                String street = request.getParameter("edit_address");

                dao.updateUserAddress(addrId, name, phone, street);
                session.setAttribute("mess", "Cập nhật địa chỉ thành công!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        response.sendRedirect("profile");
    }
}