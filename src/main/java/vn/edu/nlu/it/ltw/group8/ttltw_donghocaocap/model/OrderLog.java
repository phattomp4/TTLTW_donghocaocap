package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

import java.sql.Timestamp;
public class OrderLog {
    private int logId;
    private int orderId;
    private String status;
    private String note;
    private Timestamp createdAt;
    
    public OrderLog() {}
    public OrderLog(int logId, int orderId, String status, String note, Timestamp createdAt) {
        this.logId = logId;
        this.orderId = orderId;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
