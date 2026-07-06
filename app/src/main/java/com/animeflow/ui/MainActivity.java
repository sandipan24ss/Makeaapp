package com.animeflow.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.animeflow.R;
import com.animeflow.data.model.Anime;
import com.animeflow.data.repository.AnimeRepository;
import com.animeflow.ui.adapter.AnimeAdapter;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TabLayout tabLayout;
    private AnimeAdapter adapter;
    private AnimeRepository repository;
    private List<Anime> currentAnimeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        recyclerView = findViewById(R.id.recycler_view);
        progressBar = findViewById(R.id.progress_bar);
        tabLayout = findViewById(R.id.tab_layout);

        // Setup RecyclerView
        currentAnimeList = new ArrayList<>();
        adapter = new AnimeAdapter(currentAnimeList, this, anime -> {});
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Initialize repository
        repository = new AnimeRepository(this);

        // Setup tabs
        setupTabs();

        // Load trending anime by default
        loadTrendingAnime();
    }

    private void setupTabs() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        loadTrendingAnime();
                        break;
                    case 1:
                        loadTopRatedAnime();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void loadTrendingAnime() {
        showLoading(true);
        repository.getTrendingAnime(new AnimeRepository.AnimeCallback() {
            @Override
            public void onSuccess(List<Anime> animeList) {
                currentAnimeList.clear();
                currentAnimeList.addAll(animeList);
                adapter.notifyDataSetChanged();
                showLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadTopRatedAnime() {
        showLoading(true);
        repository.getTopRatedAnime(new AnimeRepository.AnimeCallback() {
            @Override
            public void onSuccess(List<Anime> animeList) {
                currentAnimeList.clear();
                currentAnimeList.addAll(animeList);
                adapter.notifyDataSetChanged();
                showLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchAnime(String query) {
        if (query.trim().isEmpty()) {
            loadTrendingAnime();
            return;
        }
        showLoading(true);
        repository.searchAnime(query, new AnimeRepository.AnimeCallback() {
            @Override
            public void onSuccess(List<Anime> animeList) {
                currentAnimeList.clear();
                currentAnimeList.addAll(animeList);
                adapter.notifyDataSetChanged();
                showLoading(false);
            }

            @Override
            public void onError(String errorMessage) {
                showLoading(false);
                Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLoading(boolean show) {
        progressBar.setVisibility(show ? android.view.View.VISIBLE : android.view.View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAnime(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        MenuItem watchlistItem = menu.findItem(R.id.action_watchlist);
        watchlistItem.setOnMenuItemClickListener(item -> {
            startActivity(new Intent(MainActivity.this, WatchlistActivity.class));
            return true;
        });

        return true;
    }
}
