package com.henribambangs.kapronpetshop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.henribambangs.kapronpetshop.Util.HttpsTrustManager;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        BottomNavigationView mBottomNavigation;

        mBottomNavigation = findViewById(R.id.bottom_navigation);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navHome:

                        break;
                    case R.id.navCart:
                        Intent Cart = new Intent(DashboardActivity.this, CartActivity.class);
                        startActivity(Cart);
                        break;
                }

                return true;
            }
        });

    }

    public void onDetails(View view) {
        startActivity(new Intent(this, DetailsActivity.class));
    }


}
