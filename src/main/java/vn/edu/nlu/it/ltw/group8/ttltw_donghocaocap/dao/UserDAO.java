package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.dao;


import org.mindrot.jbcrypt.BCrypt;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.context.DBContext;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.User;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model.UserAddress;
import vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.util.PasswordUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    public User login(String user, String pass) {
        String query = "SELECT * FROM Users WHERE Username = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                String dbPass = rs.getString("PasswordHash");

                if (BCrypt.checkpw(pass, dbPass)) {
                    User u = new User();
                    u.setId(rs.getInt("UserID")); // Khớp UserID
                    u.setUsername(rs.getString("Username")); // Khớp Username
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
        }
        return null;
    }

    public void signup(String user, String pass, String fullName, String email) {
        String query = "INSERT INTO Users(Username, PasswordHash, FullName, Email, Role) VALUES(?,?,?,?,?)";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            ps.setString(2, BCrypt.hashpw(pass, BCrypt.gensalt(12)));
            ps.setString(3, fullName);
            ps.setString(4, email);
            ps.setString(5, "User");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public User checkUserExist(String user) {
        String query = "SELECT * FROM Users WHERE Username = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setString(1, user);
            rs = ps.executeQuery();
            if (rs.next()) {
                return new User(
                        rs.getInt("UserID"),
                        rs.getString("Username"),
                        rs.getString("PasswordHash"),
                        rs.getString("FullName"),
                        rs.getString("Email"),
                        rs.getString("Role")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public void updateAccountProfile(User a) {
        String query = "UPDATE Users SET Email=?, FullName=?, Phone=?, Gender=?, Address=? WHERE UserID=?";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);

            ps.setString(1, a.getEmail());
            ps.setString(2, a.getFullName()); // Đảm bảo hỗ trợ tiếng Việt
            ps.setString(3, a.getPhone());
            ps.setString(4, a.getGender());
            ps.setString(5, a.getAddress());
            ps.setInt(6, a.getId()); // Điều kiện WHERE UserID = ...

            int rowCount = ps.executeUpdate();

            if (rowCount > 0) {
                System.out.println("Update thành công cho UserID: " + a.getId());
            } else {
                System.out.println("Update THẤT BẠI. Không tìm thấy UserID: " + a.getId());
            }

        } catch (Exception e) {
            System.out.println("LỖI UPDATE SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<UserAddress> getAddresses(int userId) {
        List<UserAddress> listAddress = new ArrayList<>();
        String query = "SELECT * FROM Addresses WHERE UserID = ? ORDER BY IsDefault DESC";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                listAddress.add(new UserAddress(
                        rs.getInt("AddressID"),
                        rs.getInt("UserID"),
                        rs.getString("ReceiverName"),
                        rs.getString("Phone"),
                        rs.getString("Street"),
                        rs.getString("City"),
                        rs.getBoolean("IsDefault")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAddress;
    }

    public void addAddress(int userId, String name, String phone, String address) {
        String query = "INSERT INTO Addresses (UserID, ReceiverName, Phone, Street, IsDefault) VALUES (?,?,?,?,0)";
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
        }
    }

    public void deleteAddress(int addressId) {
        String query = "DELETE FROM Addresses WHERE AddressID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateUserAddress(int addressId, String name, String phone, String street) {
        String query = "UPDATE Addresses SET ReceiverName=?, Phone=?, Street=? WHERE AddressID=?";
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
        }
    }

    public UserAddress getAddressById(int addressId) {
        String query = "SELECT * FROM Addresses WHERE AddressID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();
            if (rs.next()) {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public User getUserById(int userId) {
        String query = "SELECT * FROM Users WHERE UserID = ?";
        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("UserID"));
                u.setUsername(rs.getString("Username"));
                u.setFullName(rs.getString("FullName"));
                u.setEmail(rs.getString("Email"));
                u.setPhone(rs.getString("Phone"));
                u.setRole(rs.getString("Role"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setDefaultAddress(int userId, int addressId) {
        try {
            conn = new DBContext().getConnection();
            conn.setAutoCommit(false); // Bắt đầu Transaction


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

    public List<UserAddress> getListAddress(int userId) {
        List<UserAddress> list = new ArrayList<>();
        String query = "SELECT * FROM addresses WHERE UserID = ? ORDER BY IsDefault DESC";

        try {
            conn = new DBContext().getConnection();
            ps = conn.prepareStatement(query);
            ps.setInt(1, userId);
            rs = ps.executeQuery();
            while (rs.next()) {
                UserAddress a = new UserAddress();
                a.setId(rs.getInt("AddressID"));
                a.setName(rs.getString("ReceiverName"));
                a.setPhone(rs.getString("Phone"));
                a.setAddress(rs.getString("Street"));
                a.setCity(rs.getString("City"));

                a.setDefaultAddress(rs.getBoolean("IsDefault"));

                list.add(a);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // funtion forgetpass

    //1. Reset Password
    public boolean resetPassword(String accountInfo, String newPassword) {
        String sql = "UPDATE users SET password = ?, reset_token = NULL, token_expiry = NULL " +
                "WHERE email = ? OR phone = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, PasswordUtil.hashPassword(newPassword));
            ps.setString(2, accountInfo.trim());
            ps.setString(3, accountInfo.trim());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //2. verifyCode
    public boolean verifyCode(String accountInfo, String code) {
        String sql = "SELECT id FROM users WHERE (email = ? OR phone = ?) " +
                "AND reset_token = ? AND token_expiry > NOW()";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, accountInfo.trim());
            ps.setString(2, accountInfo.trim());
            ps.setString(3, code);
            return ps.executeQuery().next();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    //3. setResetCode
    public boolean setResetCode(String accountInfo, String code) {
        String sql = "UPDATE users SET reset_token = ?, token_expiry = DATE_ADD(NOW(), INTERVAL 5 MINUTE) " +
                "WHERE email = ? OR phone = ?";

        try (Connection conn = new DBContext().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, code);
            ps.setString(2, accountInfo.trim());
            ps.setString(3, accountInfo.trim());
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}