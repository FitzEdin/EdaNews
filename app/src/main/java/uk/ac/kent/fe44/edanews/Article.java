package uk.ac.kent.fe44.edanews;

/**
 * Created by fitzroy on 28/01/2016.
 *
 * <p>An object of this class represents an individual
 * article downloaded from the network. Articles
 * are created using data from the network or
 * generated from the data stored on the device.
 * </p>
 *
 */
public class Article {
    /**
     * The url of the image associated with this
     * particular article.
     */
    private String imageURL;
    /**
     * The recordId of the article. It is used
     * throughout the app to uniquely identify
     * articles across different lists.
     */
    private int recordID;
    /**
     * The title of the article.
     */
    private String title;
    /**
     * A short description of the subject matter
     * covered by the article. It is the first
     * few words of the article's contents.
     */
    private String short_info;
    /**
     * The date of the article presented as a
     * string in DD/MM/YYYY format.
     */
    private String date;

    /**
     * The full contents of the article.
     */
    private String contents;
    /**
     * The url of the web page for the article.
     */
    private String web_page;

    /**
     * Articles may be marked as a favourite by
     * the user; it is saved to memory if this
     * is the case.
     */
    private boolean isFave = false;     //favourited
    /**
     * An article may be saved on the device for
     * later reading.
     */
    private boolean isSaved = false;     //saved
    /**
     * The full contents of an Article is
     * downloaded after a second network call.
     * This boolean is flipped to {@code true} if
     * the full contents have been saved, and is
     * {@code false} otherwise.
     */
    private boolean isDetailed = false; //downloaded in its entirety

    /*Constructors*/
    /**
     * This constructor is used when creating articles
     * from the data stored on the device. A stored
     * article should have all its attributes.
     * @param imageURL String Url of the article's image
     * @param recordID int Article's unique recordId
     * @param title String Title of the article
     * @param short_info String The short version of the article's contents
     * @param date String Date of the article in DD/MM/YYYY format
     * @param contents String Full contents of the article
     * @param web_page String Url of the article's web page
     * @param isFave boolean Whether or not the article has been favourited.
     * @param isSaved boolean Whether or not the article has been saved for reading later.
     * TODO: Change final line below to fix bug 5
     */
    public Article(
            String imageURL, int recordID, String title, String short_info,
            String date, String contents, String web_page, boolean isFave, boolean isSaved) {
        this.imageURL = imageURL;
        this.recordID = recordID;
        this.title = title;
        this.short_info = short_info;
        this.date = date;
        this.contents = contents;
        this.web_page = web_page;
        this.isFave = isFave;
        this.isSaved = isSaved;

        //a saved/favourited article may not have been detailed before; if
        //this is the case, then contents will be empty/null. Once this is
        //the case, the article will be detailed once it is opened.
        this.isDetailed = ( (contents != null) ? true : false );
    }

    /**
     * Used when loading an article from the network for the first time.
     * @param imageURL String Url of the article's image
     * @param recordID int Article's unique recordId
     * @param title String Title of the article
     * @param short_info String The short version of the article's contents
     * @param date String Date of the article in DD/MM/YYYY format
     */
    public Article(String imageURL, int recordID, String title, String short_info, String date) {
        this.imageURL = imageURL;
        this.recordID = recordID;
        this.title = title;
        this.short_info = short_info;
        this.date = date;
    }
    /**
     * Empty constructor used to create an empty Article object.
     */
    public Article(){}

    /**
     * Getters and Setters*/
    /**
     * Get the url for the Article's image.
     * @return String Url of the article's image.
     */
    public String getImageURL() {
        return imageURL;
    }
    /**
     * Set the url for the Article's image.
     * @param imageURL String Url of the Article's image
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * Get the recordId for this Article.
     * @return int Unique recordId for the Article.
     */
    public int getRecordID() {
        return recordID;
    }

    /**
     * Gte the title of the Article.
     * @return String Title of the Article.
     */
    public String getTitle() {
        return title;
    }
    /**
     * Set the title for the Article.
     * @param title String Value to set the title of the Article to.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Get the date of the Article.
     * @return String Date of the Article in DD/MM/YYYY format.
     */
    public String getDate() {
        return date;
    }
    /**
     * Set the date of the Article.
     * @param date String A value to set as the date of the
     *             Article in DD/MM/YYYY format.
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Get the value of the Article's shortInfo.
     * @return String The Article's shortInfo.
     */
    public String getShortInfo() {
        return short_info;
    }

    /**
     * Get the contents of the Article.
     * @return String The contents of the Article.
     */
    public String getContents() {
        return contents;
    }
    /**
     * Set the contents of the Article.
     * @param contents String The value to set the contents of the Article to.
     */
    public void setContents(String contents) {
        this.contents = contents;
    }

    /**
     * Get the url of the Article's web page.
     * @return String The url of the Article's web page.
     */
    public String getWeb_page() {
        return web_page;
    }
    /**
     * Set the url of the Article's web page.
     * @param web_page String The value to set as the Article's web page url.
     */
    public void setWeb_page(String web_page) {
        this.web_page = web_page;
    }

    /**
     * Whether or not the Article has been marked as a favourite by the user.
     * @return boolean {@code true} if the Article has
     * been favourited, {@code false} otherwise
     */
    public boolean isFave() {
        return isFave;
    }
    /**
     * Set whether or not the Article has been marked as a favourite by the user.
     * @param isFave boolean {@code true} if the Article has
     * been favourited, {@code false} otherwise
     */
    public void setIsFave(boolean isFave) {
        this.isFave = isFave;
    }

    /**
     * Whether or not the Article has been saved for later reading.
     * @return boolean {@code true} if the Article has
     * been saved for later reading, {@code false} otherwise
     */
    public boolean isSaved() {
        return isSaved;
    }
    /**
     * Set whether or not the Article has been saved for later reading.
     * @param isSaved boolean {@code true} if the Article has
     * been saved for later reading, {@code false} otherwise
     */
    public void setIsSaved(boolean isSaved) {
        this.isSaved = isSaved;
    }

    /**
     * Determine whether or not the full contents of the Article has been downloaded.
     * @return boolean {@code true} if the full contents of the Article has
     * been downloaded, {@code false} otherwise
     */
    public boolean isDetailed() {
        return isDetailed;
    }
    /**
     * Set whether or not the full contents of the Article has been downloaded.
     * @param isDetailed boolean {@code true} if the full contents
     *                   of the Article has been downloaded, {@code false} otherwise
     */
    public void setIsDetailed(boolean isDetailed) {
        this.isDetailed = isDetailed;
    }
}
