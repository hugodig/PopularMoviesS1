package mx.unam.fca.popularmoviess1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View.OnClickListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hugoro
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private static final String TAG = TrailerAdapter.class.getSimpleName();
    private Context context;
    private List<Trailer> trailers = new ArrayList<Trailer>();
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, Trailer trailer);
    }

    public TrailerAdapter(List<Trailer> trailers, ListItemClickListener listener) {
        this.trailers =  trailers;
        mOnClickListener = listener;
    }

    @Override
    public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.trailer_description;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        TrailerViewHolder viewHolder = new TrailerViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerViewHolder holder, int position) {
        Log.d(TAG, "#" + position);
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    class TrailerViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        ImageView imgPlay;
        TextView tvName;

        public TrailerViewHolder(View itemView) {
            super(itemView);

            imgPlay = (ImageView) itemView.findViewById(R.id.ivPlay);
            tvName = (TextView) itemView.findViewById(R.id.tvNameTrailer);

            imgPlay.setOnClickListener(this);
            tvName.setOnClickListener(this);
        }

        void bind(int position) {
            Trailer trailer = trailers.get(position);
            tvName.setText(trailer.getName());
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Trailer trailerSelected = trailers.get(clickedPosition);
            mOnClickListener.onListItemClick(clickedPosition, trailerSelected);

            context = v.getContext();
            String key = trailerSelected.getKey();
            String app = "vnd.youtube:" + key;
            Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(app));
            String web = "https://www.youtube.com/watch?v="+key;
            Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(web));

            String title = context.getResources().getString(R.string.chooser_title);
            Intent chooser = Intent.createChooser(appIntent, title);
            if (chooser.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(appIntent);
            } else {
                context.startActivity(webIntent);
            }
        }
    }

}
