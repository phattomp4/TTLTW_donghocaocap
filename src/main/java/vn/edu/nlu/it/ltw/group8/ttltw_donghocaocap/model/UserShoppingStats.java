package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class UserShoppingStats {
    private double totalOrders;
    private double totalSpent;
    private int completedCount;
    private int cancelledCount;

    public UserShoppingStats(double totalOrders, double totalSpent, int completedCount, int cancelledCount) {
        this.totalOrders = totalOrders;
        this.totalSpent = totalSpent;
        this.completedCount = completedCount;
        this.cancelledCount = cancelledCount;

    }

    public UserShoppingStats(){};
    public double getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(double totalOrders) {
        this.totalOrders = totalOrders;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(double totalSpent) {
        this.totalSpent = totalSpent;
    }

    public int getCompletedCount() {
        return completedCount;
    }

    public void setCompletedCount(int completedCount) {
        this.completedCount = completedCount;
    }

    public int getCancelledCount() {
        return cancelledCount;
    }

    public void setCancelledCount(int cancelledCount) {
        this.cancelledCount = cancelledCount;
    }
}
