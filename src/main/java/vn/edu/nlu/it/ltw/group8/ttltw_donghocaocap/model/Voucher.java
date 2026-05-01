package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;


import java.sql.Timestamp;

public class Voucher {
    private int id;
    private String code;
    private String discountType;
    private double discountValue;
    private double maxDiscount;
    private int usageLimit;
    private int usedCount;
    private Timestamp startDate;
    private Timestamp endDate;

    public Voucher(){}

    public Voucher(int id, String code, String discountType, double discountValue, double maxDiscount, int usageLimit, int usedCount, Timestamp startDate, Timestamp endDate) {
        this.id = id;
        this.code = code;
        this.discountType = discountType;
        this.discountValue = discountValue;
        this.maxDiscount = maxDiscount;
        this.usageLimit = usageLimit;
        this.usedCount = usedCount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public double getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(double maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public int getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(int usageLimit) {
        this.usageLimit = usageLimit;
    }

    public int getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(int usedCount) {
        this.usedCount = usedCount;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public boolean isValid(double totalMoney){
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return usedCount < usageLimit && now.after(startDate) && now.before(endDate);
    }

}
