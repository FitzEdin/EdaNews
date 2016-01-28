package uk.ac.kent.fe44.edanews;

import java.util.ArrayList;

/**
 * Created by fitzroy on 28/01/2016.
 * Interacts with the Article class
 */
public class ArticleModel {
    private static ArticleModel ourInstance = new ArticleModel();

    //list of Articles
    private ArrayList<Article> articleList = new ArrayList<Article>();

    /*Constructor*/
    private ArticleModel() {
        //TODO: remove dummy data
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Because Articles Have", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "School", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "And Long Names", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "Info", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Into a Proper Text View", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Home", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "Some Very Weird", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "Hearts", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "That Don't Really Fit", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Search", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Because Articles Have", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "School", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "And Long Names", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "Info", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Into a Proper Text View", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Home", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "Some Very Weird", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "Hearts", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "That Don't Really Fit", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Search", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Because Articles Have", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "School", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "And Long Names", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "Info", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Into a Proper Text View", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Home", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "Some Very Weird", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "Hearts", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "That Don't Really Fit", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Search", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Because Articles Have", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "It", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "Just", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "Fitz!", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Into a Proper Text View", "14/12/2015"));
        articleList.add(new Article(R.drawable.ic_home_black_24dp, 123, "Home", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_school_black_24dp, 456, "Some Very Weird", "19/09/2015"));
        articleList.add(new Article(R.drawable.ic_favorite_black_24dp, 789, "Hearts", "12/09/2015"));
        articleList.add(new Article(R.drawable.ic_info_outline_black_24dp, 124, "That Don't Really Fit", "21/6/1994"));
        articleList.add(new Article(R.drawable.ic_search_black_24dp, 457, "Search", "14/12/2015"));
    }

    public static ArticleModel getInstance() {
        return ourInstance;
    }

    public ArrayList<Article> getArticleList() {
        return articleList;
    }
}
