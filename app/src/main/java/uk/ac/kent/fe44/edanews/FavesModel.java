package uk.ac.kent.fe44.edanews;

/**
 * Created by fe44 on 20/04/16.
 */
public class FavesModel {
    private ArticleModel.OnFavesUpdateListener favesUpdateListener;

    private void notifyFavesListener() {
        if(favesUpdateListener != null) {
            favesUpdateListener.onFavesUpdate();
        }
    }

    public void setFavesUpdateListener(ArticleModel.OnFavesUpdateListener lstnr) {
        this.favesUpdateListener = lstnr;
    }
}
