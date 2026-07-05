package com.fakeffpanel;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button activateButton;
    private GridLayout categoriesGrid;
    private boolean isActivated = false;

    private final String[] categories = {
        "🎯 Aim Bot",
        "💚 Unlimited Health",
        "🎯 Headshot Rate",
        "⚡ Speed Hack",
        "🔫 No Recoil",
        "👁️ Wall Hack",
        "💀 One Shot Kill",
        "🛡️ Auto Shield"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activateButton = findViewById(R.id.activateButton);
        categoriesGrid = findViewById(R.id.categoriesGrid);

        activateButton.setOnClickListener(v -> handleActivation());
        setupCategories();
    }

    private void handleActivation() {
        if (isActivated) return;

        // Show activation popup
        new AlertDialog.Builder(this)
            .setTitle("⚠️ Panel Activated!")
            .setMessage("Select your cheat categories below.")
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
            .setCancelable(false)
            .show();

        // Update button state
        isActivated = true;
        activateButton.setText("✓ ACTIVATED");
        activateButton.setEnabled(false);
        activateButton.setAlpha(0.7f);

        // Reveal categories with animation
        categoriesGrid.setVisibility(View.VISIBLE);
        categoriesGrid.setAlpha(0f);
        categoriesGrid.animate()
            .alpha(1f)
            .setDuration(500)
            .start();
    }

    private void setupCategories() {
        categoriesGrid.setVisibility(View.GONE);

        for (String category : categories) {
            Button catButton = new Button(this);
            catButton.setText(category);
            catButton.setTextColor(getResources().getColor(android.R.color.white));
            catButton.setBackgroundResource(R.drawable.button_red);
            catButton.setAllCaps(false);
            catButton.setPadding(24, 24, 24, 24);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f);
            params.setMargins(16, 16, 16, 16);
            catButton.setLayoutParams(params);

            catButton.setOnClickListener(v -> showCategoryPopup(category));
            categoriesGrid.addView(catButton);
        }
    }

    private void showCategoryPopup(String categoryName) {
        String cleanName = categoryName.replaceAll("[\\p{So}\\p{Cn}]", "").trim();

        new AlertDialog.Builder(this)
            .setTitle("🔧 " + cleanName + " ENABLED!")
            .setMessage("This is for entertainment only. No actual cheating functionality.")
            .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
            .setCancelable(true)
            .show();
    }
}
