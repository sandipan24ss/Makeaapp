package com.animeflow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.animeflow.R;
import com.animeflow.data.model.Anime;
import com.animeflow.data.repository.AnimeRepository;
import com.bumptech.glide.Glide;
import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {
    
    private ImageView bannerImage;
    private ImageView coverImage;
    private TextView title;
    private TextView description;
    private TextView status;
    private TextView episodes;
    private TextView genres;
    private RatingBar ratingBar;
    private Button addWatchlistBtn;
    private Button removeWatchlistBtn;
    private AnimeRepository repository;
    private int animeId;
    private Anime currentAnime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Initialize repository
        repository = new AnimeRepository(this);

        // Initialize views
        bannerImage = findViewById(R.id.banner_image);
        coverImage = findViewById(R.id.cover_image);
        title = findViewById(R.id.anime_title);
        description = findViewById(R.id.anime_description);
        status = findViewById(R.id.anime_status);
        episodes = findViewById(R.id.anime_episodes);
        genres = findViewById(R.id.anime_genres);
        ratingBar = findViewById(R.id.rating_bar);
        addWatchlistBtn = findViewById(R.id.btn_add_watchlist);
        removeWatchlistBtn = findViewById(R.id.btn_remove_watchlist);

        // Get anime ID from intent
        animeId = getIntent().getIntExtra("anime_id", 0);

        // Setup buttons
        addWatchlistBtn.setOnClickListener(v -> addToWatchlist());
        removeWatchlistBtn.setOnClickListener(v -> removeFromWatchlist());

        // Load anime details from intent or fetch from API
        loadAnimeDetails();
    }

    private void loadAnimeDetails() {
        // Try to get anime object from intent
        currentAnime = (Anime) getIntent().getSerializableExtra("anime_object");
        
        if (currentAnime != null) {
            displayAnimeDetails(currentAnime);
        } else {
            // Fetch from API if needed (implement if necessary)
            Toast.makeText(this, "Loading anime details...", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayAnimeDetails(Anime anime) {
        currentAnime = anime;

        // Load banner
        if (anime.getBannerImage() != null) {
            Glide.with(this)
                    .load(anime.getBannerImage())
                    .centerCrop()
                    .into(bannerImage);
        }

        // Load cover
        if (anime.getCoverImage() != null && anime.getCoverImage().getLarge() != null) {
            Glide.with(this)
                    .load(anime.getCoverImage().getLarge())
                    .into(coverImage);
        }

        // Set title
        title.setText(anime.getTitle().getDisplayTitle());

        // Set description
        String desc = anime.getDescription();
        if (desc != null) {
            // Remove HTML tags for cleaner display
            desc = desc.replaceAll("<br>", "\n").replaceAll("<[^>]*>", "");
            description.setText(desc);
        }

        // Set status
        status.setText("Status: " + (anime.getStatus() != null ? anime.getStatus() : "N/A"));

        // Set episodes
        if (anime.getEpisodes() != null) {
            episodes.setText("Episodes: " + anime.getEpisodes());
        } else {
            episodes.setText("Episodes: TBA");
        }

        // Set genres
        if (anime.getGenres() != null && anime.getGenres().length > 0) {
            StringBuilder genreList = new StringBuilder();
            for (String genre : anime.getGenres()) {
                genreList.append(genre).append(", ");
            }
            if (genreList.length() > 0) {
                genreList.setLength(genreList.length() - 2);
            }
            genres.setText("Genres: " + genreList.toString());
        }

        // Set rating
        float score = anime.getAverageScore() / 20f;
        ratingBar.setRating(score);

        // Update watchlist button states
        updateWatchlistButtons();
    }

    private void updateWatchlistButtons() {
        boolean inWatchlist = repository.isInWatchlist(animeId);
        addWatchlistBtn.setEnabled(!inWatchlist);
        removeWatchlistBtn.setEnabled(inWatchlist);
        addWatchlistBtn.setAlpha(inWatchlist ? 0.5f : 1.0f);
        removeWatchlistBtn.setAlpha(inWatchlist ? 1.0f : 0.5f);
    }

    private void addToWatchlist() {
        if (currentAnime != null) {
            repository.addToWatchlist(currentAnime);
            updateWatchlistButtons();
            Toast.makeText(this, "Added to watchlist!", Toast.LENGTH_SHORT).show();
        }
    }

    private void removeFromWatchlist() {
        repository.removeFromWatchlist(animeId);
        updateWatchlistButtons();
        Toast.makeText(this, "Removed from watchlist!", Toast.LENGTH_SHORT).show();
    }
}
