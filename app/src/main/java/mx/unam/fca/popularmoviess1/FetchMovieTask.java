package mx.unam.fca.popularmoviess1;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Hugoro on 29/05/2017.
 */

public class FetchMovieTask extends AsyncTask<String, Void, List<String>> {
    private final String LOG_FC = FetchMovieTask.class.getSimpleName();
    private Context context;
    private AsyncTaskCompleteListener<List> mCallback;
    private LayoutInflater inflater;
    private ProgressBar mLoading;
    private MovieAdapter mMovieAdapter;
    private List<Movie> mListMovies;

    public interface AsyncTaskCompleteListener<List> {
        public void onTaskComplete(java.util.List result);
    }

    public FetchMovieTask(Context context, MovieAdapter mMovieAdapter, AsyncTaskCompleteListener<List> mCallback) {
        this.context = context;
        this.mCallback = mCallback;
        this.mMovieAdapter = mMovieAdapter;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);
        mLoading = (ProgressBar) view.findViewById(R.id.pbLoading);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoading.setVisibility(View.VISIBLE);
    }

    @Override
    protected List<String> doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String strJSON = null;
        String strOrder = params[0];
        try {
            Uri builtUri = Uri.parse("http://api.themoviedb.org/3/discover/movie").buildUpon()
                    .appendQueryParameter("sort_by", strOrder)
                    .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                    .build();
            String address = builtUri.toString();
            URL url = new URL(address);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            if(inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer buffer = new StringBuffer();
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");

                }
                if(buffer.length() == 0) {
                    return null;
                } else {
                    strJSON = buffer.toString();
                    return getMovieDataJSON(strJSON);
                }
            }
        } catch (IOException ioe) {
            Log.d(LOG_FC, "Error ", ioe);
            return null;
        } catch (JSONException je) {
            Log.e(LOG_FC, "JSON Error", je);
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> urls) {
        mLoading.setVisibility(View.INVISIBLE);
        if(urls != null) {
            mMovieAdapter.replace(urls);
        }
        mCallback.onTaskComplete(mListMovies);
    }

    private List<String> getMovieDataJSON(String forecastJSON) throws JSONException {
        //JSON objects that need to be extracted
        final String MOVIE_RESULT = "results";
        final String MOVIE_ID = "id";
        final String MOVIE_TITLE = "original_title";
        final String MOVIE_RELEASE_DATE = "release_date";
        final String MOVIE_POSTER_PATH = "poster_path";
        final String MOVIE_POPULARITY = "popularity";
        final String MOVIE_RATING = "vote_average";
        final String MOVIE_SYNOPSIS = "overview";
        // final String MOVIE_GENRE = "genre_ids";
        final String MOVIE_POSTER_SIZE = "w342";

        JSONObject movieJson = new JSONObject(forecastJSON);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_RESULT);
        List<String> urls = new ArrayList<>();
        List<Movie> listMovies = new ArrayList<>();    // mListMovies = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            JSONObject movieJSON = movieArray.getJSONObject(i);
            urls.add("http://image.tmdb.org/t/p/"+ MOVIE_POSTER_SIZE + movieJSON.getString(MOVIE_POSTER_PATH));

            listMovies.add(new Movie("http://image.tmdb.org/t/p/"+ MOVIE_POSTER_SIZE + movieJSON.getString(MOVIE_POSTER_PATH),
                                    movieJSON.getInt(MOVIE_ID),
                                    movieJSON.getString(MOVIE_TITLE),
                                    movieJSON.getString(MOVIE_RELEASE_DATE),
                                    movieJSON.getString(MOVIE_POPULARITY),
                                    movieJSON.getString(MOVIE_RATING),
                                    movieJSON.getString(MOVIE_SYNOPSIS) ));
        }
        this.mListMovies = listMovies;
        return urls;
    }

}
