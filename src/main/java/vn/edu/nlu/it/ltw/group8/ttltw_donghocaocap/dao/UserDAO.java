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

                    return u;
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
    public boolean signup(String user, String pass, String fullName, String email, String phone) {
        // Không cần check isValid ở đây vì Servlet đã check rồi, tránh lặp code và lỗi logic
        String query = "INSERT INTO users (Username, PasswordHash, Email, FullName, Role, CreatedAt, Phone) VALUES (?, ?, ?, ?, 'User', NOW(), ?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, BCrypt.hashpw(pass, BCrypt.gensalt(12)));
            ps.setString(3, email);
            ps.setString(4, fullName);
            ps.setString(5, phone);
            return ps.executeUpdate() > 0;
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
        String query = "UPDATE users SET Email=?, FullName=?, Phone=?, Gender=?, Address=? WHERE UserID=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, a.getEmail());
            ps.setString(2, a.getFullName());
            ps.setString(3, a.getPhone());
            ps.setString(4, a.getGender());
            ps.setString(5, a.getAddress());
            ps.setInt(6, a.getId());
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

    public void addAddress(int userId, String name, String phone, String address) {
        String query = "INSERT INTO addresses (UserID, ReceiverName, Phone, Street, IsDefault) VALUES (?,?,?,?,0)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            ps.setString(2, name);
            ps.setString(3, phone);
            ps.setString(4, address);
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

    public void updateUserAddress(int addressId, String name, String phone, String street) {
        String query = "UPDATE addresses SET ReceiverName=?, Phone=?, Street=? WHERE AddressID=?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, street);
            ps.setInt(4, addressId);
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

    public boolean setResetCode(String accountInfo, String code) {
        String sql = "UPDATE users SET ResetToken = ?, TokenExpiry = DATE_ADD(NOW(), INTERVAL 5 MINUTE) WHERE Email = ? OR Phone = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, code);
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

    public boolean verifyCode(String accountInfo, String code) {
        String sql = "SELECT UserID FROM users WHERE (Email = ? OR Phone = ?) AND ResetToken = ? AND TokenExpiry > NOW()";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, accountInfo);
            ps.setString(2, accountInfo);
            ps.setString(3, code);
            rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeResources();
        }
        return false;
    }

    public boolean resetPassword(String accountInfo, String newPassword) {
        String sql = "UPDATE users SET PasswordHash = ?, ResetToken = NULL, TokenExpiry = NULL WHERE Email = ? OR Phone = ?";
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
        return u;
    }

    private UserAddress mapAddress(ResultSet rs) throws SQLException {
        return new UserAddress(
                rs.getInt("AddressID"),
                rs.getInt("UserID"),
                rs.getString("ReceiverName"),
                rs.getString("Phone"),
                rs.getString("Street"),
                rs.getString("City"),
                rs.getBoolean("IsDefault")
        );
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
        String query = "SELECT * FROM users WHERE Email = ?";

        try{
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, email);
            ResultSet resultSet = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setFullName(rs.getString("name"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
    public void insertGoogleUser(User user) {
        String query = "INSERT INTO Users (email, name, avatar, auth_provider) VALUES (?, ?, ?, 'GOOGLE')";

        try {
            conn = new DBContext().getConnection();
            PreparedStatement ps = conn.prepareStatement(query);

                ps.setString(1, user.getEmail());
                ps.setString(2, user.getFullName());

                ps.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}