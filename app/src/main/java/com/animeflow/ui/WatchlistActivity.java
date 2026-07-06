package com.animeflow.ui;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.animeflow.R;
import com.animeflow.data.model.Anime;
import com.animeflow.data.repository.AnimeRepository;
import com.animeflow.ui.adapter.WatchlistAdapter;
import java.util.ArrayList;
import java.util.List;

public class WatchlistActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private TextView emptyView;
    private WatchlistAdapter adapter;
    private AnimeRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watchlist);

        // Set title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("My Watchlist");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_watchlist);
        emptyView = findViewById(R.id.empty_watchlist_view);

        // Initialize repository
        repository = new AnimeRepository(this);

        // Setup RecyclerView
        List<Anime> watchlist = repository.getWatchlist();
        adapter = new WatchlistAdapter(watchlist, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Update empty view
        updateEmptyView(watchlist);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh watchlist when returning to activity
        List<Anime> watchlist = repository.getWatchlist();
        adapter.updateList(watchlist);
        updateEmptyView(watchlist);
    }

    private void updateEmptyView(List<Anime> watchlist) {
        if (watchlist.isEmpty()) {
            emptyView.setVisibility(android.view.View.VISIBLE);
            recyclerView.setVisibility(android.view.View.GONE);
        } else {
            emptyView.setVisibility(android.view.View.GONE);
            recyclerView.setVisibility(android.view.View.VISIBLE);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
