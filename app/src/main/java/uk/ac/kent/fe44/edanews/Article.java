package uk.ac.kent.fe44.edanews;

/**
 * Created by fitzroy on 28/01/2016.
 * Represents an individual article. Articles
 * are created using data from the network or
 * generated from the data stored on the device.
 */
public class Article {
    //attributes that define an article
    private String imageURL;
    private int recordID;
    private String title;
    private String short_info;
    private String date;
    private String contents;
    private String web_page;

    // whether or not the article has been...
    private boolean isFave = false;     //favourited
    private boolean isSaved = false;     //favourited
    private boolean isDetailed = false; //downloaded in its entirety

    /*Constructors*/
    /* Used when loading an article from the disk,
     * with all its details, into the ArticleList
     * or into the FavesList */
    public Article(
            String imageURL, int recordID, String title, String short_info,
            String date, String contents, String web_page, boolean isFave) {
        /* This constructor is used when creating articles
         * from the data stored on the device. A stored
         * article would have all its attributes.*/
        this.imageURL = imageURL;
        this.recordID = recordID;
        this.title = title;
        this.short_info = short_info;
        this.date = date;
        this.contents = contents;
        this.web_page = web_page;
        this.isFave = isFave;

        // all saved articles would have been downloaded
        // in their entirety once they came into view.
        this.isDetailed = true;
    }
    /* Used when loading an article from the network */
    public Article(String imageURL, int recordID, String title, String short_info, String date) {
        /* A version of the original picture, resized
         * to a height and width defined in the
         * ArticleModel class, is displayed in the article
         * list */
        String resizer = ArticleModel.getInstance().getResizer();
        String url = "http://www.efstratiou.info/projects/newsfeed/timthumb.php?w=200&h=200&src="
                + imageURL;

        //resizer.concat(imageURL);

        /* The first network call responds with a
         * few of the Article's attributes; a second
          * network call will get the details*/
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
    public void setIsFave(boolean isFave) {
        this.isFave = isFave;
    }

    public boolean isSaved() {
        return isSaved;
    }
    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    public boolean isDetailed() {
        return isDetailed;
    }
    public void setIsDetailed(boolean isDetailed) {
        this.isDetailed = isDetailed;
    }
}
