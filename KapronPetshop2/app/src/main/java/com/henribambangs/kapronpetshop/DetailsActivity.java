package com.henribambangs.kapronpetshop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
    }

    public void onDetails(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void onBack(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}