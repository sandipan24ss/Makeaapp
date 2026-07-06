package com.animeflow.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.animeflow.R;
import com.animeflow.data.model.Anime;
import com.bumptech.glide.Glide;
import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.ViewHolder> {
    
    private List<Anime> animeList;
    private Context context;

    public WatchlistAdapter(List<Anime> animeList, Context context) {
        this.animeList = animeList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_watchlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Anime anime = animeList.get(position);
        holder.bind(anime);
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public void updateList(List<Anime> newList) {
        animeList.clear();
        animeList.addAll(newList);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView coverImage;
        TextView title;
        TextView comingSoonBadge;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            coverImage = itemView.findViewById(R.id.watchlist_cover_image);
            title = itemView.findViewById(R.id.watchlist_title);
            comingSoonBadge = itemView.findViewById(R.id.coming_soon_badge);
        }

        void bind(Anime anime) {
            // Load cover image
            if (anime.getCoverImage() != null && anime.getCoverImage().getLarge() != null) {
                Glide.with(context)
                        .load(anime.getCoverImage().getLarge())
                        .centerCrop()
                        .placeholder(R.drawable.ic_placeholder)
                        .error(R.drawable.ic_error)
                        .into(coverImage);
            }

            // Set title
            String displayTitle = anime.getTitle().getDisplayTitle();
            title.setText(displayTitle);

            // Show "Coming Soon" badge for all watchlist items
            comingSoonBadge.setVisibility(View.VISIBLE);
            comingSoonBadge.setText("COMING SOON");
        }
    }
}
