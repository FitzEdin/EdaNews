package uk.ac.kent.fe44.edanews;

/**
 * Created by fitzroy on 28/01/2016.
 * Represents an individual article.
 */
public class Article {
    private String imageURL;
    private int imgResource;
    private int recordID;
    private String title;
    private String date;
    private String contents;
    private String web_page;

    /*Constructor*/
    public Article(int imgResource, int recordID, String title, String date) {
        this.imgResource = imgResource;
        this.recordID = recordID;
        this.title = title;
        this.date = date;
    }

    /*Getters and Setters*/
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getImgResource() {
        return imgResource;
    }

    public void setImgResource(int imgResource) {
        this.imgResource = imgResource;
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
}
