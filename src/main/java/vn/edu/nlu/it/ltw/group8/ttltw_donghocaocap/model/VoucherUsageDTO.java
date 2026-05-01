package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

import java.sql.Timestamp;

public class VoucherUsageDTO {
    private String code;
    private String username;
    private Timestamp usedAt;
    private double orderTotal;

    public VoucherUsageDTO(){}

    public VoucherUsageDTO(String code, Timestamp usedAt, String username, double orderTotal) {
        this.code = code;
        this.usedAt = usedAt;
        this.username = username;
        this.orderTotal = orderTotal;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getUsedAt() {
        return usedAt;
    }

    public void setUsedAt(Timestamp usedAt) {
        this.usedAt = usedAt;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(double orderTotal) {
        this.orderTotal = orderTotal;
    }
}
