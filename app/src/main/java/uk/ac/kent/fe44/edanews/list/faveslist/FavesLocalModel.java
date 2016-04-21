package uk.ac.kent.fe44.edanews.list.faveslist;

import java.util.ArrayList;

import uk.ac.kent.fe44.edanews.model.Article;
import uk.ac.kent.fe44.edanews.model.ArticleModel;
import uk.ac.kent.fe44.edanews.model.ListLocalModel;

/**
 * Created by fe44 on 20/04/16.
 */
public class FavesLocalModel implements ListLocalModel {
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

        //if the model is in the masterList
        if( model.isInMaster(article.getRecordID()) ) {
            //..and it is not marked as saved
            if( article.isSaved() == false) {
                //..throw it away
                model.removeFromMaster(article.getRecordID());
            }else { //it is marked as saved
                //unmark it as favourite
                model.getArticleFromMaster(article.getRecordID()).setIsFave(false);
            }
        }
        //notify whoever is listening
        notifyFavesListener();
    }

    public void add(Article article) {
        //mark article as favourite in particular List
        ArticleModel model = ArticleModel.getInstance();

        article.setIsFave(true);
        //update the masterList
        //if the article was in Masterlist before
        if(model.isInMaster(article.getRecordID())){
            //..then mark it as a fave
            model.getArticleFromMaster(article.getRecordID()).setIsFave(true);
        }else { //since it's not in the masterlist before
            //add a new copy
            model.addToMaster(article);
        }
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
