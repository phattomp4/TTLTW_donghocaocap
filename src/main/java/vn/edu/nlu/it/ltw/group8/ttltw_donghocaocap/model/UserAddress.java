package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class UserAddress {
    private int id;
    private int uID;
    private String name;
    private String phone;
    private String province;
    private String district;
    private String ward;
    private String streetDetail;
    private boolean isDefaultAddress;

    public UserAddress() {}

    public UserAddress(int id, int uID, String name, String phone, String province, String district, String ward, String streetDetail, boolean isDefaultAddress) {
        this.id = id;
        this.uID = uID;
        this.name = name;
        this.phone = phone;
        this.province = province;
        this.district = district;
        this.ward = ward;
        this.streetDetail = streetDetail;
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
    public String getProvince() {
        return province;
    }
    public void setProvince(String province) {
        this.province = province;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getWard() {
        return ward;
    }
    public void setWard(String ward) {
        this.ward = ward;
    }
    public String getStreetDetail() {
        return streetDetail;
    }
    public void setStreetDetail(String streetDetail) {
        this.streetDetail = streetDetail;
    }
    public boolean isDefaultAddress() {
        return isDefaultAddress;
    }
    public void setDefaultAddress(boolean defaultAddress) {
        isDefaultAddress = defaultAddress;
    }
}