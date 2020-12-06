package com.sakibuddinbhuiyan.zikir.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sakibuddinbhuiyan.zikir.utils.PublicVariables;
import com.sakibuddinbhuiyan.zikir.R;
import com.sakibuddinbhuiyan.zikir.database.DatabaseHandler;
import com.sakibuddinbhuiyan.zikir.fragments.FragmentFavourites;
import com.sakibuddinbhuiyan.zikir.fragments.FragmentHome;
import com.sakibuddinbhuiyan.zikir.fragments.FragmentSettings;

public class MainActivity extends AppCompatActivity {

    private FrameLayout frameLayout;
    public static BottomNavigationView bottomNavigationView;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = (FrameLayout)findViewById(R.id.fragmentContainer);
        bottomNavigationView = (BottomNavigationView)findViewById(R.id.bottomNavigation);

        databaseHandler = new DatabaseHandler(getApplicationContext(), DatabaseHandler.DATABASE_NAME, null, DatabaseHandler.DATABASE_VERSION);

        //getting settings state from database
        PublicVariables.vibrate = databaseHandler.getVibrateStatus();
        PublicVariables.selectedLanguage = databaseHandler.getLanguageStatus();

        if(PublicVariables.selectedLanguage.equals("English")){
            bottomNavigationView.getMenu().getItem(0).setTitle("Home");
            bottomNavigationView.getMenu().getItem(1).setTitle("Favourites");
            bottomNavigationView.getMenu().getItem(2).setTitle("Settings");
        }
        else{
            bottomNavigationView.getMenu().getItem(0).setTitle("হোম");
            bottomNavigationView.getMenu().getItem(1).setTitle("ফেভারিটস");
            bottomNavigationView.getMenu().getItem(2).setTitle("সেটিংস");
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        //I added this if statement to keep the selected fragment when rotating the device
        //and to initialise the first fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    new FragmentHome()).commit();
        }

    }

    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.homemenu:
                    selectedFragment = new FragmentHome();
                    break;
                case R.id.favouritemenu:
                    selectedFragment = new FragmentFavourites();
                    break;
                case R.id.settingsmenu:
                    selectedFragment = new FragmentSettings();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                    selectedFragment).commit();

            return true;
        }
    };
}