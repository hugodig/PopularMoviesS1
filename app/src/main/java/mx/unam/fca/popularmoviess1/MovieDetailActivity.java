package mx.unam.fca.popularmoviess1;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieDetailActivity extends AppCompatActivity implements TrailerAdapter.ListItemClickListener {
    private final String LOG_MD = MovieDetailActivity.class.getSimpleName();
    private TrailerAdapter mTrailerAdapter;
    private Button mFavorite;
    private RecyclerView mTrailers;
    final private TrailerAdapter.ListItemClickListener mOnClickListener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        this.setTitle("MovieDetail");

        TextView tvTitle = (TextView) findViewById(R.id.tvTitleMovie);
        ImageView ivImage = (ImageView) findViewById(R.id.ivImageMovie);
        TextView tvDate = (TextView) findViewById(R.id.tvYearMovie);
        TextView tvDuration = (TextView) findViewById(R.id.tvDurationMovie);
        TextView tvRating = (TextView) findViewById(R.id.tvRatingMovie);
        TextView tvSynopsis = (TextView) findViewById(R.id.tvSynopsisMovie);
        mFavorite = (Button) findViewById(R.id.btFavorite);
        mTrailers = (RecyclerView) findViewById(R.id.rvTrailersMovie);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mTrailers.setLayoutManager(layoutManager);
        mTrailers.setHasFixedSize(true);

        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra("MOVIE_DATA");
        tvTitle.setText(movie.getTitle());
        tvDate.setText(movie.getDate());
        tvDuration.setText(movie.getPopularity());
        tvRating.setText(movie.getAverage()+"/10");
        tvSynopsis.setText(movie.getOverview());

        Context context = this;
        Picasso.with(context).load(movie.getImage()).into(ivImage);

        getTrailers(movie.getId());

        mFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values = new ContentValues();
                values.put(MovieTable.MOVIE_ID_MOVIE, movie.getId());
                Uri mNewUri = getContentResolver().insert(MyMovieContentProvider.CONTENT_URI, values);

                Toast.makeText(MovieDetailActivity.this,
                        "Thank you for marked this favorite movie",
                        Toast.LENGTH_SHORT).show();
            }
        });

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    private void getTrailers(int idMovie) {
        Uri builtUri = Uri.parse("http://api.themoviedb.org/3/movie/"+idMovie+"/videos").buildUpon()
                .appendQueryParameter("api_key", BuildConfig.MOVIEDB_API_KEY)
                .build();
        String url = builtUri.toString();
        Log.d(LOG_MD, url);
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray items = response.getJSONArray("results");
                            JSONObject trailerObj;
                            List<Trailer> lTrailers = new ArrayList();
                            for (int i=0; i<items.length(); i++){
                                trailerObj = items.getJSONObject(i);
                                lTrailers.add(new Trailer(trailerObj.getString("id"),
                                                          trailerObj.getString("key"),
                                                          trailerObj.getString("name")));
                            }

                            mTrailerAdapter = new TrailerAdapter(lTrailers, mOnClickListener);
                            mTrailers.setAdapter(mTrailerAdapter);

                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(LOG_MD, "Error in JSON Parsing");
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        onBackPressed();
        return true;
    }

    @Override
    public void onListItemClick(int clickedItemIndex, Trailer trailer) {
        Log.i(String.valueOf(this),  "Item #" +clickedItemIndex);
    }

}