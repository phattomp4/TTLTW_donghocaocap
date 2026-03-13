package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class ShopInfo {
    private String brandName;
    private String subtitle;
    private String mainImageUrl;
    private String history1;
    private String history2;
    private String history3;
    private String footerDesc;
    private String address;
    private String hotline;
    private String email;
    private String copyright;

    public ShopInfo() {}

    public ShopInfo(String brandName, String subtitle, String mainImageUrl, String history1, String history2, String history3) {
        this.brandName = brandName;
        this.subtitle = subtitle;
        this.mainImageUrl = mainImageUrl;
        this.history1 = history1;
        this.history2 = history2;
        this.history3 = history3;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getMainImageUrl() {
        return mainImageUrl;
    }

    public void setMainImageUrl(String mainImageUrl) {
        this.mainImageUrl = mainImageUrl;
    }

    public String getHistory1() {
        return history1;
    }

    public void setHistory1(String history1) {
        this.history1 = history1;
    }

    public String getHistory2() {
        return history2;
    }

    public void setHistory2(String history2) {
        this.history2 = history2;
    }

    public String getHistory3() {
        return history3;
    }

    public void setHistory3(String history3) {
        this.history3 = history3;
    }

    public String getFooterDesc() {
        return footerDesc;
    }

    public void setFooterDesc(String footerDesc) {
        this.footerDesc = footerDesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHotline() {
        return hotline;
    }

    public void setHotline(String hotline) {
        this.hotline = hotline;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}