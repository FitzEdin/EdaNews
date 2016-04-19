package uk.ac.kent.fe44.edanews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fe44 on 17/04/16.
 *
 * Class for values that are used in
 * the creation and modification of
 * the database.
 */
public class ArticlesDBHelper extends SQLiteOpenHelper {
    public final static String TABLENAME = "articles";

    public final static String COLUMN_ID = "_id";
    public final static String COLUMN_TITLE = "title";
    public final static String COLUMN_SHORT_INFO = "short_info";
    public final static String COLUMN_DATE = "date";
    public final static String COLUMN_CONTENTS = "contents";
    public final static String COLUMN_WEB_PAGE = "web_page";
    public final static String COLUMN_IMAGE_URL = "imageURL";
    public final static String COLUMN_IS_FAVE = "isFave";
    public final static String COLUMN_IS_SAVED = "isSaved";

    public final static String DATABASE_NAME = "articles.db";
    public final static int DATABASE_VERSION = 1;

    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLENAME + "("
            + COLUMN_IMAGE_URL + " TEXT NOT NULL, "
            + COLUMN_ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT NOT NULL, "
            + COLUMN_SHORT_INFO + " TEXT NOT NULL, "
            + COLUMN_DATE + " TEXT NOT NULL, "
            + COLUMN_CONTENTS + " TEXT, "
            + COLUMN_WEB_PAGE + " TEXT NOT NULL, "
            + COLUMN_IS_FAVE + " INTEGER NOT NULL, "
            + COLUMN_IS_SAVED + " INTEGER NOT NULL);";

    /**
     * Constructor for the DBHelper class
     * @param context The context in which the dbHelper is being created
     */
    public ArticlesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Create the database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    /**
     * Drop any pre-existing tables with the same name
     * and create a new one.
     * @param db The database to be upgraded.
     * @param oldVersion The previous version of the database.
     * @param newVersion The new definition of the database.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        onCreate(db);
    }
}
