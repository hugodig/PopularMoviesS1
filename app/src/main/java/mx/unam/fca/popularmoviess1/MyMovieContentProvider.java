package mx.unam.fca.popularmoviess1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

import java.util.Arrays;
import java.util.HashSet;

/**
 * Created by Hugoro
 */

public class MyMovieContentProvider extends ContentProvider {

    private MovieDatabaseHelper database;   // database
    private static final String BASE_PATH = "movies";
    private static final String AUTHORITY = "mx.unam.fca.popularmoviess1";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

    @Override
    public boolean onCreate() {
        database = new MovieDatabaseHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        // Uisng SQLiteQueryBuilder instead of query() method
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        checkColumns(projection);
        // Set the table
        queryBuilder.setTables(MovieTable.TABLE_MOVIE);
        queryBuilder.appendWhere(MovieTable.MOVIE_ID + "=" + uri.getLastPathSegment());

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection,
                                        selectionArgs, null, null, sortOrder);
        // make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = sqlDB.insert(MovieTable.TABLE_MOVIE, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH +"/"+ id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        String id = uri.getLastPathSegment();
        int rowsDeleted = sqlDB.delete(MovieTable.TABLE_MOVIE,
                                    MovieTable.MOVIE_ID + "=" + id,
                                    null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        String id = uri.getLastPathSegment();
        int rowsUpdated = sqlDB.update(MovieTable.TABLE_MOVIE,
                                    values, MovieTable.MOVIE_ID + "=" + id,
                                    null);
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    private void checkColumns(String[] projection) {
        String[] available = { MovieTable.MOVIE_ID,
                MovieTable.MOVIE_ID_MOVIE, MovieTable.MOVIE_DATE_VOTE };
        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(
                    Arrays.asList(projection));
            HashSet<String> availableColumns = new HashSet<String>(
                    Arrays.asList(available));
            // check if all columns which are requested are available
            if (!availableColumns.containsAll(requestedColumns)) {
                throw new IllegalArgumentException(
                        "Unknown columns in projection");
            }
        }
    }

}
