package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;

public class ShopGallery {
    private int imageID;
    private String imageUrl;

    public ShopGallery() {}

    public ShopGallery(int imageID, String imageUrl) {
        this.imageID = imageID;
        this.imageUrl = imageUrl;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}