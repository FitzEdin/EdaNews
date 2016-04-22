package uk.ac.kent.fe44.edanews.model;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.list.articlelist.ArticleListNetworkModel;
import uk.ac.kent.fe44.edanews.db.DBModel;
import uk.ac.kent.fe44.edanews.details.DetailsModel;
import uk.ac.kent.fe44.edanews.list.faveslist.FavesLocalModel;
import uk.ac.kent.fe44.edanews.list.savedlist.SavedLocalModel;
import uk.ac.kent.fe44.edanews.list.searchlist.SearchListNetworkModel;

/**
 * Created by fitzroy on 28/01/2016.
 *
 * The main model class for the application. Control over
 * specific parts of the model are delegated to subclasses
 * of ListNetworkModel. Access to these subclasses is controlled
 * via this class.
 */
public class ArticleModel {

    private static ArticleModel ourInstance = new ArticleModel();

    /*Constructor*/
    private ArticleModel() {
    }
    public static ArticleModel getInstance() {
        return ourInstance;
    }


    /**
     * Array list mirroring the DB.
     */
    private ArrayList<Article> masterList = new ArrayList<Article>();
    /**
     * instance of the dbModel
     */
    private DBModel dbModel;

    /**
     * Used to get individual articles from the dbCursor when populating the masterList
     * @param cursor
     * @param position
     * @return
     */
    private Article getArticle(Cursor cursor, int position) {
        cursor.moveToPosition(position);
        int fave = cursor.getInt(7);
        int save = cursor.getInt(8);
        boolean isFave = ( (fave == 1)?(true):(false) );
        boolean isSaved = ( (save == 1)?(true):(false) );
        Article article = new Article(
                cursor.getString(0),    //imageURL
                cursor.getInt(1),       //recordID
                cursor.getString(2),    //title
                cursor.getString(3),    //short_info
                cursor.getString(4),    //date
                cursor.getString(5),    //contents
                cursor.getString(6),    //web_page
                isFave,                 //isFave
                isSaved                 //isSaved
        );
        return article;
    }
    /**
     * Grab all the articles in the database and place into the masterList
     * If the db is empty, or the operation fails then return false; otherwise return true
     */
    public boolean loadMasterList(Context context) {
        masterList.clear(); //may be in memory when the app is invisible
        dbModel = DBModel.getInstance(context);
        try {
            dbModel.open();
            if(dbModel.isEmpty()) {
                return false;
            }else{
                Cursor cursor = dbModel.getArticles();
                for(int i = 0; i < cursor.getCount(); i++) {
                    masterList.add(getArticle(cursor, i));
                }
                return true;
            }
        } catch (SQLException e){
            Log.e("Error opening database", e.toString());
            return false;
        }
    }

    /**
     *
     * @param context
     */
    public void saveMasterList(Context context) {
        dbModel = DBModel.getInstance(context);
        dbModel.deleteAll();
        for(Article a : getMasterList()) {
            dbModel.addArticle(a);
        }
    }
    /**
     * Get the master list
     */
    public ArrayList<Article> getMasterList() {
        return masterList;
    }
    /**
     * Add an article to master list.
     */
    public void addToMaster(Article article) {
        if(isInMaster(article.getRecordID())) {
            //ignore
        }else {
            masterList.add(article);
            dbModel.deleteArticle(article.getRecordID());
        }
    }
    /**
     * Remove an article from the masterList based on its recordId
     * @param recordId The recordId of the article to be removed from the masterList.
     */
    public void removeFromMaster(int recordId) {
        Article a;
        int index = 0;
        for(int i = 0; i < masterList.size() ; i++) {
            a = masterList.get(i);
            if(a.getRecordID() == recordId) {    index = i;    }
        }
        masterList.remove(index);
        dbModel.deleteArticle(recordId);
    }
    /**
     * determine whether an article is already in the master list
     * @param recordId The recordID of the article under question
     * @return boolean whether or not the target article is already in masterList
     */
    public boolean isInMaster(int recordId) {
        for(Article a:getMasterList()){
            if(a.getRecordID() == recordId){ return true;    }
        }
        return false;
    }
     /**
     * Returns an article in the masterList
     * @param recordId Unique recordId of the article in the masterList
     * @return The target article
     */
    public Article getArticleFromMaster(int recordId) {
        for(Article a:getMasterList()) {
            if(a.getRecordID() == recordId){ return a;    }
        }
        return null;
    }



    /**
     * The ArticleListNetworkModel class manages the main list of articles that are shown in the
     * newsfeed. It is first populated by copying articles from the MasterListModel, then
     * via a network call as the user scrolls, OR newer articles are found online.
     */
    public ArticleListNetworkModel articleModel = new ArticleListNetworkModel();
    /**
     * Returns the list of articles that will show
     * in the newsfeed; this list is managed by
     * the ArticleListNetworkModel class.
     * @return ArrayList List of articles
     */
    public ArrayList<Article> getArticleList() {
        return articleModel.getList();
    }
    /**
     * Instructs the ArticleListNetworkModel object to
     * load a set of articles into the newsfeed.
     * The object decides where this data should
     * be loaded from.
     * @param listUpdateListener A class that is listening for changes
     *                           to the list of articles being managed by the
     *                           ArticleListNetworkModel object. This class should implement
     *                           the OnListUpdateListener interface.
     */
    public void loadData(OnListUpdateListener listUpdateListener, boolean refresh) {
        articleModel.load(listUpdateListener, refresh);
    }
    /**
     * Each class that uses data from the newsfeed should
     * implement this interface to be informed when changes
     * are made to the list of articles.
     */
    public interface OnListUpdateListener {
        void onListUpdate();
    }


    /**
     * Populates the details of particular articles.
     */
    DetailsModel detailsModel = new DetailsModel();
    /**
     * Instructs a DetailsModel object to load data for a given article.
     * @param articleId The record ID of the article; this identifies
     *                  the article in the network resource.
     * @param articleIndex The index of the article in the particular list.
     * @param detailsUpdateListener The class that is reacting to changes to
     *                              the article's details (may be null).
     */
    public void loadArticleDetails(int callerId, int articleId, int articleIndex, OnDetailsUpdateListener detailsUpdateListener) {
        detailsModel.load(callerId, articleId, articleIndex, detailsUpdateListener);
    }
    public interface OnDetailsUpdateListener {
        void onDetailsUpdate();
    }


    /**
     * The SearchListNetworkModel class manages the list of articles that appear in the search results.
     */
    public SearchListNetworkModel searchModel = new SearchListNetworkModel();
    /**
     * Returns the list of articles that will show
     * in the search results; this list is managed by
     * the SearchListNetworkModel class.
     * @return ArrayList List of articles
     */
    public ArrayList<Article> getSearchList() {
        return searchModel.getList();
    }
    /**
     * Passes its parameters to the SearchListNetworkModel object
     * for it to perform the search for articles with titles
     * containing the key parameter.
     * @param key String The string for searching titles.
     * @param searchListUpdateListener A class that is listening for changes to the
     *                                 list of articles being managed by the
     *                                 SearchListNetworkModel object. This class should
     *                                 implement the OnSearchListUpdateListener interface.
     */
    public void loadSearchData(String key, OnSearchListUpdateListener searchListUpdateListener) {
        searchModel.load(key, searchListUpdateListener);
    }
    /**
     * Each class that uses data from the search results should
     * implement this interface to be informed when the list of
     * results are available.
     */
    public interface OnSearchListUpdateListener {
        void  onSearchListUpdate();
    }


    /** Manages the faves list; no network requests are
     * involved, and the list itself is transient. It is
     * built dynamically using getFavesList(). Any additions
     * and deletions are done using addToFaves() and
     * removeFromFaves(), respectively. Any listeners are
     * notified after each addition and deletion.
     *  */
    private FavesLocalModel favesModel = new FavesLocalModel();

    public ArrayList<Article> getFavesList() {
        return favesModel.getList();
    }
    public void addToFaves(Article article){
        //mark article as favourite in particular List
        favesModel.add(article);
    }
    public void removeFromFaves(Article article) {
        favesModel.remove(article);
    }
    /*receive subscriptions for notifications from classes*/
    public void setOnFavesUpdateListener(OnFavesUpdateListener favesUpdateListener) {
        favesModel.setFavesUpdateListener(favesUpdateListener);
    }
    public interface OnFavesUpdateListener {
        void onFavesUpdate();
    }


    /** Manages the read later/saved list; no network requests are
     * involved, and the list itself is transient. It is
     * built dynamically using getSavedList(). Any additions
     * and deletions are done using addToSaved() and
     * removeFromSaved(), respectively. Any listeners are
     * notified after each addition and deletion.
     *  */
    private SavedLocalModel savedModel = new SavedLocalModel();

    public ArrayList<Article> getSavedList() {
        return savedModel.getList();
    }
    public void addToSaved(Article article){
        //mark article as saved in particular List
        savedModel.add(article);
    }
    public void removeFromSaved(Article article) {
        savedModel.remove(article);
    }
    /*receive subscriptions for notifications from classes*/
    public void setOnSavedUpdateListener(OnSavedUpdateListener savedUpdateListener) {
        savedModel.setSavedUpdateListener(savedUpdateListener);
    }
    public interface OnSavedUpdateListener {
        void onSavedUpdate();
    }
}
