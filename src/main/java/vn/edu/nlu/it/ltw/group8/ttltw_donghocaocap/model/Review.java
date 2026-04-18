package vn.edu.nlu.it.ltw.group8.ttltw_donghocaocap.model;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Review {
    private int id;
    private int productId;
    private int userId;
    private int rating;
    private String comment;
    private String imageUrl;
    private Date createdAt;
    private String username;
    private String userAvatar;
    private int likes;
    private List<ReviewReply> replies = new ArrayList<>();

    public Review() {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getUserAvatar() { return userAvatar; }
    public void setUserAvatar(String userAvatar) { this.userAvatar = userAvatar; }

    public int getLikes() {
        return likes;
    }
    public void setLikes(int likes) {
        this.likes = likes;
    }

    public List<ReviewReply> getReplies() {
        return replies;
    }
    public void setReplies(List<ReviewReply> replies) {
        this.replies = replies;
    }
}