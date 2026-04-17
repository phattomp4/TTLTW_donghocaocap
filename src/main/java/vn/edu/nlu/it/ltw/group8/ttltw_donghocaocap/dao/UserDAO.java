package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;

import org.mindrot.jbcrypt.BCrypt;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;


    public User login(String account, String pass) {

        String query = "SELECT * FROM users WHERE Username = ? OR Email = ? OR Phone = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, account);
            ps.setString(2, account);
            ps.setString(3, account);
            rs = ps.executeQuery();

            if (rs.next()) {
                String dbPass = rs.getString("PasswordHash");
                if (BCrypt.checkpw(pass, dbPass)) {
                    return mapUser(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }
    public void updateRememberToken(String username, String token) {
        String query = "UPDATE Users SET RememberToken = ? WHERE Username = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            ps.setString(2, username);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public User getUserByToken(String token) {
        String query = "SELECT * FROM Users WHERE RememberToken = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }
    public boolean signup(String user, String pass, String fullName, String email, String phone, String token) {
        // role mặc định là 'user'
        // Status mặc định là 'Unverified' mã kích hoạt có hạn 24h
        String query = "INSERT INTO users (Username, PasswordHash, Email, FullName, Role, CreatedAt, Phone, Status, VerificationToken, VerificationExpiry) " +
                "VALUES (?, ?, ?, ?, 'user', NOW(), ?, 'Unverified', ?, DATE_ADD(NOW(), INTERVAL 24 HOUR))";
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = new DBContext().getConnection();
            // Bước 1: Tắt AutoCommit để bắt đầu quản lý Transaction
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, BCrypt.hashpw(pass, BCrypt.gensalt(12))); //
            ps.setString(3, email);
            ps.setString(4, fullName);
            ps.setString(5, phone);
            ps.setString(6, token); // Lưu mã token ngẫu nhiên để kích hoạt

            int result = ps.executeUpdate();
            if (result > 0) {
                // Bước 2: Commit khi lệnh INSERT thành công
                conn.commit();
                return true;
            } else {
                // Rollback nếu không có dòng nào được tác động
                conn.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Bước 3: Nếu xảy ra lỗi, thực hiện Rollback để đảm bảo toàn vẹn dữ liệu
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    // Bước 4: Trả lại trạng thái AutoCommit và giải phóng tài nguyên
                    conn.setAutoCommit(true);
                    conn.close();
                }
                if (ps != null) ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    // Hàm cập nhật lại Token mới cho tài khoản chưa kích hoạt
    public void refreshVerificationToken(String email, String newToken) {
        String query = "UPDATE users SET VerificationToken = ?, VerificationExpiry = DATE_ADD(NOW(), INTERVAL 24 HOUR) WHERE Email = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newToken);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
    }
    public boolean activateAccount(String token) {
        String query = "UPDATE users SET Status = 'Active', VerificationToken = NULL, VerificationExpiry = NULL WHERE VerificationToken = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, token);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    public boolean isAccountExist(String accountInfo) {
        String query = "SELECT COUNT(*) FROM users WHERE Email = ? OR Phone = ? OR Username = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, accountInfo);
            ps.setString(2, accountInfo);
            ps.setString(3, accountInfo);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }
    public User checkUserExist(String user) {
        String query = "SELECT * FROM users WHERE Username = ?";
        return getSingleUser(query, user);
    }

    public User checkEmailExist(String email) {
        String query = "SELECT * FROM users WHERE Email = ?";
        return getSingleUser(query, email);
    }

    public User checkPhoneExist(String phone) {
        String query = "SELECT * FROM users WHERE Phone = ?";
        return getSingleUser(query, phone);
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM users WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    public void updateAccountProfile(User a) {
        String query = "UPDATE users SET FullName=?, Gender=? WHERE UserID=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, a.getFullName());
            ps.setString(2, a.getGender());
            ps.setInt(3, a.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public List<UserAddress> getAddresses(int userId) {
        List<UserAddress> list = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE UserID = ? ORDER BY IsDefault DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapAddress(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return list;
    }

    public void addAddress(int userId, String name, String phone, String province, String district, String ward, String streetDetail) {
        String query = "INSERT INTO addresses (UserID, ReceiverName, Phone, Province, District, Ward, StreetDetail, IsDefault) VALUES (?,?,?,?,?,?,?,0)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, province);
            ps.setString(5, district);
            ps.setString(6, ward);
            ps.setString(7, streetDetail);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void deleteAddress(int addressId) {
        String query = "DELETE FROM addresses WHERE AddressID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    public void updateUserAddress(int addressId, String name, String phone, String province, String district, String ward, String streetDetail) {
        String query = "UPDATE addresses SET ReceiverName=?, Phone=?, Province=?, District=?, Ward=?, StreetDetail=? WHERE AddressID=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, province);
            ps.setString(4, district);
            ps.setString(5, ward);
            ps.setString(6, streetDetail);
            ps.setInt(7, addressId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    public UserAddress getAddressById(int addressId) {
        String query = "SELECT * FROM addresses WHERE AddressID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();
            if (rs.next()) return mapAddress(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    public void setDefaultAddress(int userId, int addressId) {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false);
            PreparedStatement ps1 = conn.prepareStatement("UPDATE addresses SET IsDefault = 0 WHERE UserID = ?");
            ps1.setInt(1, userId);
            ps1.executeUpdate();
            PreparedStatement ps2 = conn.prepareStatement("UPDATE addresses SET IsDefault = 1 WHERE AddressID = ? AND UserID = ?");
            ps2.setInt(1, addressId);
            ps2.setInt(2, userId);
            ps2.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            try { if(conn!=null) conn.rollback(); } catch(SQLException ex){}
            e.printStackTrace();
        } finally {
            try { if(conn!=null) { conn.setAutoCommit(true); conn.close(); } } catch(SQLException ex){}
        }
    }

    public boolean resetPassword(String accountInfo, String newPassword) {
        String sql = "UPDATE users SET PasswordHash = ? WHERE Email = ? OR Phone = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            ps.setString(2, accountInfo);
            ps.setString(3, accountInfo);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    private User getSingleUser(String query, String param) {
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, param);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return null;
    }

    private User mapUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("UserID"));
        u.setUsername(rs.getString("Username"));
        u.setPassword(rs.getString("PasswordHash"));
        u.setFullName(rs.getString("FullName"));
        u.setEmail(rs.getString("Email"));
        u.setRole(rs.getString("Role"));
        u.setPhone(rs.getString("Phone"));
        u.setGender(rs.getString("Gender"));
        u.setAddress(rs.getString("Address"));
        u.setAvatar(rs.getString("Avatar"));

        u.setStatus(rs.getString("Status"));
        u.setVerificationToken(rs.getString("VerificationToken"));
        u.setVerificationExpiry(rs.getTimestamp("VerificationExpiry"));
        return u;
    }

    private UserAddress mapAddress(ResultSet rs) throws SQLException {
        UserAddress userAddress = new UserAddress(
                rs.getInt("AddressID"),
                rs.getInt("UserID"),
                rs.getString("ReceiverName"),
                rs.getString("Phone"),
                rs.getString("Province"),
                rs.getString("District"),
                rs.getString("Ward"),
                rs.getString("StreetDetail"),
                rs.getBoolean("IsDefault")
        );
        return userAddress;
    }

    private void closeResources() {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User getUserByEmail(String email){
        User user = null;
        String query = "SELECT * FROM Users WHERE Email = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }

    public void insertGoogleUser(User user) {
        // tự động cắt chuỗi email làm Username
        String email = user.getEmail();
        String autoUsername = email.substring(0, email.indexOf("@"));

        // tạo một mật khẩu ngẫu nhiên (bằng UUID) và mã hóa nó bằng BCrypt
        String randomPass = java.util.UUID.randomUUID().toString();
        String hashedPassword = org.mindrot.jbcrypt.BCrypt.hashpw(randomPass, org.mindrot.jbcrypt.BCrypt.gensalt(12));

        String query = "INSERT INTO users (Username, PasswordHash, Email, FullName, Role, CreatedAt, Avatar) VALUES (?, ?, ?, ?, 'User', NOW(), ?)";


        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, autoUsername);
            ps.setString(2, hashedPassword);
            ps.setString(3, email);
            ps.setString(4, user.getFullName());
            ps.setString(5, user.getAvatar());

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println(">>> Đăng ký thành công tài khoản Google cho: " + email);
            }

        } catch (Exception e) {
            System.out.println(">>> LỖI KHI INSERT GOOGLE USER:");
            e.printStackTrace();
        }
    }

    // hàm cập nhật avatar
    public void updateAvatar (int userId, String avatarUrl){
        String query = "UPDATE Users SET avatar = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, avatarUrl);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // hàm cập nhật email và sdt sau khi xác thực OTP
    public void updateContactInfo(int userId, String email, String phone) {
        String query = "UPDATE users SET Email = ?, Phone = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ps.setString(2, phone);
            ps.setInt(3, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }

    // hàm đổi mật khẩu
    public void updatePassword(int userId, String newPasswordHash) {
        String query = "UPDATE users SET PasswordHash = ? WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, newPasswordHash);
            ps.setInt(2, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
    }
    public User getUserByAccount(String account) {
        String query = "SELECT * FROM users WHERE Username = ? OR Email = ? OR Phone = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, account);
            ps.setString(2, account);
            ps.setString(3, account);
            rs = ps.executeQuery();
            if (rs.next()) return mapUser(rs); //
        } catch (Exception e) { e.printStackTrace(); }
        finally { closeResources(); }
        return null;
    }
}