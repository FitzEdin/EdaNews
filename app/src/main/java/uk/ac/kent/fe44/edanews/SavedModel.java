package uk.ac.kent.fe44.edanews;

import java.util.ArrayList;

/**
 * Created by fe44 on 20/04/16.
 */
public class SavedModel {
    private ArticleModel.OnSavedUpdateListener savedUpdateListener;

    public void remove(Article article) {
        //get position of this article in the ArticleList
        int position;
        ArticleModel model = ArticleModel.getInstance();

        position = model.getArticleList().indexOf(article);
        if(position != -1) {
            //unmark article as saved in ArticleList
            (model.getArticleList().get(position)).setIsSaved(false);
        }

        position = model.getSearchList().indexOf(article);
        if(position != -1) {
            //unmark article as saved in SearchList
            (model.getSearchList().get(position)).setIsSaved(false);
        }

        //if the model is in the masterList
        if( model.isInMaster(article.getRecordID()) ) {
            //..and it is not marked as fave
            if( article.isFave() == false) {
                //..throw it away
                model.removeFromMaster(article.getRecordID());
            }else { //it is marked as fave
                //unmark as saved
                model.getArticleFromMaster(article.getRecordID()).setIsSaved(false);
            }
        }
        //notify whoever is listening
        notifySavedListener();
    }

    public void add(Article article) {
        //mark article as saved in particular List
        ArticleModel model = ArticleModel.getInstance();

        article.setIsSaved(true);
        //update the masterList
        if(model.isInMaster(article.getRecordID())){
            model.getArticleFromMaster(article.getRecordID()).setIsSaved(true);
        }else{
            model.addToMaster(article);
        }
        //notify the view that is listening
        notifySavedListener();
    }

    public ArrayList<Article> getList() {
        ArticleModel model = ArticleModel.getInstance();
        ArrayList<Article> savedList = new ArrayList<>();
        for(Article a : model.getMasterList()) {
            if(a.isSaved()) {
                savedList.add(a);
            }
        }
        return savedList;
    }

    private void notifySavedListener() {
        if(savedUpdateListener != null) {
            savedUpdateListener.onSavedUpdate();
        }
    }

    public void setSavedUpdateListener(ArticleModel.OnSavedUpdateListener lstnr) {
        this.savedUpdateListener = lstnr;
    }
}
