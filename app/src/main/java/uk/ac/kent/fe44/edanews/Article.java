package uk.ac.kent.fe44.edanews;

/**
 * Created by fitzroy on 28/01/2016.
 * Represents an individual article.
 */
public class Article {
    //attributes that define an article
    private String imageURL;
    private int recordID;
    private String title;
    private String date;
    private String short_info;
    private String contents;
    private String web_page;

    // whether or not the article has been...
    private boolean isFave = false;     //favourited
    private boolean isSaved = false;    //saved for later reading
    private boolean isRead = false;     //read
    private boolean isDetailed = false; //downloaded in its entirety

    /*Constructor*/
    public Article(String imageURL, int recordID, String title, String short_info, String date) {
        String resizer = ArticleModel.getInstance().getResizer();
        String url = resizer.concat(imageURL);
        this.imageURL = url;
        this.recordID = recordID;
        this.title = title;
        this.short_info = short_info;
        this.date = date;
    }
    //empty constructor used to create an empty Article object
    public Article(){}

    /*Getters and Setters*/
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortInfo() {
        return short_info;
    }

    public void setShortInfo(String short_info) {
        this.short_info = short_info;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWeb_page() {
        return web_page;
    }

    public void setWeb_page(String web_page) {
        this.web_page = web_page;
    }

    public boolean isFave() {
        return isFave;
    }

    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public boolean isSaved() {
        return isSaved;
    }

    public void setIsFave(boolean isFave) {
        this.isFave = isFave;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isDetailed() {
        return isDetailed;
    }

    public void setIsDetailed(boolean isDetailed) {
        this.isDetailed = isDetailed;
    }
}
