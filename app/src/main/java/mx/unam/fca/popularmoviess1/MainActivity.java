package mx.unam.fca.popularmoviess1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
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
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements FetchMovieTask.AsyncTaskCompleteListener, AdapterView.OnItemClickListener {
    private final String LOG_MA = MainActivity.class.getSimpleName();
    private MovieAdapter mMovieAdapter;
    private FetchMovieTask movieTask;
    // private ProgressBar mLoading;
    private static final String SORT_REVENUE = "revenue.desc";
    private static final String SORT_POPULAR = "popularity.desc";
    private static final String SORT_AVERAGE = "vote_average.desc";
    private List<Movie> mListMovies;
    private FetchMovieTask.AsyncTaskCompleteListener<List> listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("PopularMovies");

        GridView mMoviesGrid = (GridView) findViewById(R.id.gridMovies);
        mMovieAdapter = new MovieAdapter(this);
        mMoviesGrid.setAdapter(mMovieAdapter);
        // mLoading = (ProgressBar) findViewById(R.id.pbLoading);

        updateMovies(SORT_REVENUE);
        mMoviesGrid.setOnItemClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.popular:
                updateMovies(SORT_POPULAR);
                return true;
            case R.id.top_rated:
                updateMovies(SORT_AVERAGE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentMovieDetail = new Intent(context, destinationClass);

        Movie movie = (Movie) mListMovies.get(position);
        intentMovieDetail.putExtra("MOVIE_DATA", movie);
        startActivity(intentMovieDetail);
    }

    private void updateMovies(String order) {
        Context context = this;
        movieTask = new FetchMovieTask(context, mMovieAdapter, this);
        movieTask.execute(order);
    }

    @Override
    public void onTaskComplete(List result) {
        Log.d(LOG_MA, "Los resultados: "+result);
        if(listener!=null) {
            // listener.onTaskComplete(result);

            mListMovies = result;
        }
    }

}