package mx.unam.fca.popularmoviess1;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugoro on 13/05/2017.
 */

public class MovieAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> urls = new ArrayList<String>();
    private final String LOG_NC = MovieAdapter.class.getSimpleName();

    public MovieAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = new ImageView(context);
        }
        ImageView imageView = (ImageView) convertView;
        String url = getItem(position);
        Log.d(LOG_NC, " URL" + url);

        Picasso.with(context).load(url).into(imageView);

        return convertView;
    }

    public void replace(List<String> urls) {
        this.urls.clear();
        this.urls.addAll(urls);
        notifyDataSetChanged();
    }
}
