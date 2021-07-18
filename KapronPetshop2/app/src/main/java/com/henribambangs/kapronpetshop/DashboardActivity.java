package com.henribambangs.kapronpetshop;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.henribambangs.kapronpetshop.Util.HttpsTrustManager;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }

    public void onDetails(View view) {
        startActivity(new Intent(this, DetailsActivity.class));
    }
}
