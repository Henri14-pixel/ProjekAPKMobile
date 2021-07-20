package com.henribambangs.kapronpetshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.HttpsTrustManager;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ProfileActivity extends AppCompatActivity {

    TextView nama, email, jk, phone, alamat, daerah;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //if the user is not logged in
        //starting the login activity
        if (!AdapterUser.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        nama = (TextView) findViewById(R.id.textNama);
        email = (TextView) findViewById(R.id.textEmail);
        jk = (TextView) findViewById(R.id.textKelamin);
        phone = (TextView) findViewById(R.id.textPhone);
        alamat = (TextView) findViewById(R.id.textAlamat);
        daerah = (TextView) findViewById(R.id.textDaerah);
        image = findViewById(R.id.imgProfil);


        //getting the current user
        ModelUser user = AdapterUser.getInstance(this).getUser();

        //setting the values to the textviews
        nama.setText(String.valueOf(user.getCustomer_name()));
        email.setText(user.getEmail());
        if(!user.getGender().matches("")){
            jk.setText(user.getGender());
        } else {
            jk.setText("-");
        }
        if(!user.getPhone_number().matches("")){
            phone.setText(user.getPhone_number());
        } else {
            phone.setText("-");
        }
        if(!user.getAddress().matches("")){
            alamat.setText(user.getAddress());
        } else {
            alamat.setText("-");
        }
        if(!user.getSubdistrict_id().matches("")){
            daerah.setText(user.getSubdistrict_name()+", "+user.getCity_name()+", "+user.getProvince_name());
        } else {
            daerah.setText("-");
        }

        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/customer/"+user.getCustomer_image());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.get()
                .load(String.valueOf(url))
                .resize(400,400).into(image);

        //Intent to Edit Profile Activity
        findViewById(R.id.btneditdata).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
            }
        });

        //calling the logout method
        findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Anda Berhasil Logout!", Toast.LENGTH_SHORT).show();
                finish();
                AdapterUser.getInstance(getApplicationContext()).logout();
            }
        });

    }

    public void onBack(View view) {

        startActivity(new Intent(this, DashboardActivity.class));
    }
}
