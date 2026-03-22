package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class UserAddress {
    private int id;
    private int uID;
    private String name;
    private String phone;
    private String address;
    private String city;
    private boolean isDefaultAddress;

    public UserAddress() {}

    public UserAddress(int id, int uID, String name, String phone, String address, String city, boolean isDefault) {
        this.id = id;
        this.uID = uID;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.city = city;
        this.isDefaultAddress = isDefaultAddress;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getuID() {
        return uID;
    }

    public void setuID(int uID) {
        this.uID = uID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isDefaultAddress() {
        return isDefaultAddress;
    }

    public void setDefaultAddress(boolean isDefaultAddress) {
        this.isDefaultAddress = isDefaultAddress;
    }
}