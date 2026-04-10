package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class User  implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;
    private String phone;
    private String gender;
    private String address;
    private String avatar;
    private String status;
    //them truong de them chuc nang kich hoat
    private String verificationToken;
    private Timestamp verificationExpiry;
    private int failedAttempts;
    private java.sql.Timestamp lockExpiry;

    public User() {}


    public User(int id, String username, String password, String fullName, String email, String role, String phone, String gender, String address, String avatar, String status, String verificationToken, Timestamp verificationExpiry, int failedAttempts, Timestamp lockExpiry) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.phone = phone;
        this.gender = gender;
        this.address = address;
        this.avatar = avatar;
        this.status = status;
        this.verificationToken = verificationToken;
        this.verificationExpiry = verificationExpiry;
        this.failedAttempts = failedAttempts;
        this.lockExpiry = lockExpiry;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAvatar() { return avatar; }

    public void setAvatar(String avatar) { this.avatar = avatar; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public Timestamp getVerificationExpiry() {
        return verificationExpiry;
    }

    public void setVerificationExpiry(Timestamp verificationExpiry) {
        this.verificationExpiry = verificationExpiry;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public Timestamp getLockExpiry() {
        return lockExpiry;
    }

    public void setLockExpiry(Timestamp lockExpiry) {
        this.lockExpiry = lockExpiry;
    }
}