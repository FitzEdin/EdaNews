package uk.ac.kent.fe44.edanews;

import java.util.ArrayList;

/**
 * Created by fe44 on 20/04/16.
 */
public class FavesModel {
    private ArticleModel.OnFavesUpdateListener favesUpdateListener;

    public void remove(Article article) {
        //get position of this article in the ArticleList
        int position;
        ArticleModel model = ArticleModel.getInstance();

        position = model.getArticleList().indexOf(article);
        if(position != -1) {
            //unmark article as favourite in ArticleList
            (model.getArticleList().get(position)).setIsFave(false);
        }

        position = model.getSearchList().indexOf(article);
        if(position != -1) {
            //unmark article as favourite in SearchList
            (model.getSearchList().get(position)).setIsFave(false);
        }

        //both the saved list and the faves list are sublists of the masterList

        //remove it from list of Faves
        model.removeFromMaster(article.getRecordID());
        //notify whoever is listening
        notifyFavesListener();
    }

    public void add(int position, int adapterId) {
        //mark article as favourite in particular List
        ArticleModel model = ArticleModel.getInstance();

        Article article;
        switch (adapterId) {
            case ArticlesApp.SEARCH_CALLER_ID:     //searchList
                article = model.getSearchList().get(position);
                article.setIsFave(true);
                break;
            default:     //newsfeed
                article = model.getArticleList().get(position);
                article.setIsFave(true);
                break;
        }
        //update the masterList
        model.addToMaster(article);
        //notify the view that is listening
        notifyFavesListener();
    }

    public ArrayList<Article> getList() {
        ArticleModel model = ArticleModel.getInstance();
        ArrayList<Article> favesList = new ArrayList<>();
        for(Article a : model.getMasterList()) {
            if(a.isFave()) {
                favesList.add(a);
            }
        }
        return favesList;
    }

    private void notifyFavesListener() {
        if(favesUpdateListener != null) {
            favesUpdateListener.onFavesUpdate();
        }
    }

    public void setFavesUpdateListener(ArticleModel.OnFavesUpdateListener lstnr) {
        this.favesUpdateListener = lstnr;
    }
}
