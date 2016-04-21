package uk.ac.kent.fe44.edanews.model;

import java.util.ArrayList;

/**
 * Created by fe44 on 21/04/16.
 *
 * A "local list" is populated from the database stored
 * on the device. It is a subset of the MasterList desribed
 * in ArticleModel, and must implement this interface.
 */
public interface ListLocalModel {
    void remove(Article article);
    void add(Article article);
    ArrayList<Article> getList();
}
