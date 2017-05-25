package mx.unam.fca.popularmoviess1;

import java.io.Serializable;

/**
 * Created by Hugoro on 20/05/2017.
 */

public class Movie implements Serializable {
    String imagePath;
    int id;
    String title;
    String date;
    String popularity;
    String voteAverage;
    String overview;

    public Movie(String imagePath, int id, String title, String date, String popularity,
                 String voteAverage, String overview) {
        this.imagePath = imagePath;
        this.id = id;
        this.title = title;
        this.date = date;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.overview = overview;
    }

    public String getImage() {
        return imagePath;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }
}
