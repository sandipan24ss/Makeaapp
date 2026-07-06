package com.animeflow.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.animeflow.R;
import com.animeflow.data.model.Anime;
import com.animeflow.ui.DetailActivity;
import com.bumptech.glide.Glide;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.ViewHolder> {
    
    private List<Anime> animeList;
    private Context context;
    private OnAnimeClickListener listener;

    public AnimeAdapter(List<Anime> animeList, Context context, OnAnimeClickListener listener) {
        this.animeList = animeList;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_anime, parent, false);
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
        RatingBar ratingBar;
        TextView status;
        View itemView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            coverImage = itemView.findViewById(R.id.anime_cover_image);
            title = itemView.findViewById(R.id.anime_title);
            ratingBar = itemView.findViewById(R.id.anime_rating);
            status = itemView.findViewById(R.id.anime_status);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAnimeClick(animeList.get(position));
                    // Also start detail activity
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("anime_id", animeList.get(position).getId());
                    context.startActivity(intent);
                }
            });
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

            // Set rating
            float score = anime.getAverageScore() / 20f; // Convert to 5-star scale
            ratingBar.setRating(score);

            // Set status
            String statusText = anime.getStatus() != null ? anime.getStatus() : "Unknown";
            status.setText(statusText);
        }
    }

    public interface OnAnimeClickListener {
        void onAnimeClick(Anime anime);
    }
}
