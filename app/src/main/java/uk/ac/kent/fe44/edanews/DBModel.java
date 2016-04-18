package uk.ac.kent.fe44.edanews;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;

/**
 * Created by fe44 on 17/04/16.
 */
public class DBModel {
    private static DBModel ourInstance = new DBModel();

    private SQLiteDatabase database;
    private static ArticlesDBHelper dbHelper;
    private Cursor dbCursor;

    public static DBModel getInstance(Context context) {
        dbHelper = new ArticlesDBHelper(context);
        return ourInstance;
    }

    /**
     *
     */
    private DBModel() {
    }

    /**
     * Retrieve a writable instance of the database.
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }
    /**
     * Close the database.
     */
    public void close() {
        database.close();
    }

    /**
     * Add an article to the database.
     * @param article The Article object to be added to the database
     */
    public void addArticle(Article article) {
        int fave = ( article.isFave() ) ? 1 : 0;
        int save = ( article.isSaved() ) ? 1 : 0;
        ContentValues values = new ContentValues();
        values.put(ArticlesDBHelper.COLUMN_IMAGE_URL, article.getImageURL());
        values.put(ArticlesDBHelper.COLUMN_ID, article.getRecordID());
        values.put(ArticlesDBHelper.COLUMN_TITLE, article.getTitle());
        values.put(ArticlesDBHelper.COLUMN_SHORT_INFO, article.getShortInfo());
        values.put(ArticlesDBHelper.COLUMN_DATE, article.getDate());
        values.put(ArticlesDBHelper.COLUMN_CONTENTS, article.getContents());
        values.put(ArticlesDBHelper.COLUMN_WEB_PAGE, article.getWeb_page());
        values.put(ArticlesDBHelper.COLUMN_IS_FAVE, fave);
        values.put(ArticlesDBHelper.COLUMN_IS_SAVED, save);

        long insertId = database.insert(ArticlesDBHelper.TABLENAME, null, values);
    }
    /**
     * Retrieve all articles from the database; this is called
     * whenever the application is opened to populate the
     * Master List.
     * @return A Cursor for navigating what was returned by the query.
     */
    public Cursor getArticles() {
        String[] allColumns = {
                ArticlesDBHelper.COLUMN_IMAGE_URL,
                ArticlesDBHelper.COLUMN_ID,
                ArticlesDBHelper.COLUMN_TITLE,
                ArticlesDBHelper.COLUMN_SHORT_INFO,
                ArticlesDBHelper.COLUMN_DATE,
                ArticlesDBHelper.COLUMN_CONTENTS,
                ArticlesDBHelper.COLUMN_WEB_PAGE,
                ArticlesDBHelper.COLUMN_IS_FAVE,
                ArticlesDBHelper.COLUMN_IS_SAVED
        };

        dbCursor = database.query(
                ArticlesDBHelper.TABLENAME,
                allColumns, null, null, null, null, null);
        return dbCursor;
    }
    /**
     * Check if the database is empty.
     * @return boolean, describing whether or not the
     * database has articles in it.
     */
    public boolean isEmpty() {
        dbCursor = getArticles();
        long size = dbCursor.getCount();
        dbCursor.close();
        return (size == 0);
    }
}
