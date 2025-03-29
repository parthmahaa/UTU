package com.example.utu;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        loadFragment(new EventCalendarFragment());

        // Use if-else instead of switch to handle navigation item selection
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId(); // Get the ID of the selected item

            if (itemId == R.id.nav_calendar) {
                loadFragment(new EventCalendarFragment());
            } else if (itemId == R.id.nav_directory) {
                loadFragment(new DirectoryFragment());
            } else if (itemId == R.id.nav_lost_found) {
                loadFragment(new LostFoundFragment());
            } else if (itemId == R.id.nav_news) {
                loadFragment(new NewsFragment());
            }
            return true;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}