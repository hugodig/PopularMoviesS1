package mx.unam.fca.popularmoviess1;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Hugoro on 23/05/2017.
 */

public class MovieTable {
    // Database table movie
    public static final String TABLE_MOVIE = "FAVORITE_MOVIE";
    public static final String MOVIE_ID = "_id";
    public static final String MOVIE_ID_MOVIE = "_id_movie";
    public static final String MOVIE_DATE_VOTE = "date";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
            + TABLE_MOVIE
            + "("
            + MOVIE_ID + " integer primary key autoincrement, "
            + MOVIE_ID_MOVIE + " integer not null, "
            + MOVIE_DATE_VOTE + " datetime default current_timestamp "
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(MovieTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will drop all data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        onCreate(database);
    }

}
