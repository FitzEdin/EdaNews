package uk.ac.kent.fe44.edanews;

/**
 * Created by fitzroy on 28/01/2016.
 * Represents an individual article.
 */
public class Article {
    private String imageURL;
    private int recordID;
    private String title;
    private String date;
    private String short_info;
    private String contents;
    private String web_page;

    private boolean isFave = false;
    private boolean isDetailed = false;

    private static final String timThumb = "http://www.efstratiou.info/projects/newsfeed/timthumb.php?w=200&h=200&src=";


    /*Constructor*/
    public Article(String imageURL, int recordID, String title, String short_info, String date) {
        String url = timThumb.concat(imageURL);
        this.imageURL = url;
        this.recordID = recordID;
        this.title = title;
        this.short_info = short_info;
        this.date = date;
    }

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

    public void setIsFave(boolean isFave) {
        this.isFave = isFave;
    }

    public boolean isDetailed() {
        return isDetailed;
    }

    public void setIsDetailed(boolean isDetailed) {
        this.isDetailed = isDetailed;
    }
}
