package com.henribambangs.kapronpetshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity extends AppCompatActivity {
    TextView nama, harga, berat, stok, deskripsi, kategori;
    ImageView gambar;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);

        /*get data from intent*/
        Intent data = getIntent();
        final int id = data.getIntExtra("id", 0);
        String intent_kategori = data.getStringExtra("productCategory");
        String intent_nama = data.getStringExtra("productName");
        String intent_harga = data.getStringExtra("productPrice");
        String intent_gambar = data.getStringExtra("productImage");
        String intent_stok = data.getStringExtra("productStock");
        String intent_berat = data.getStringExtra("productWeight");
        String intent_deskripsi = data.getStringExtra("productDesc");
        /*end get data from intent*/

        kategori = findViewById(R.id.productCategory);
        nama = findViewById(R.id.productName);
        harga = findViewById(R.id.productPrice);
        berat = findViewById(R.id.productWeight);
        stok = findViewById(R.id.productStock);
        deskripsi = findViewById(R.id.productDescription);
        gambar = findViewById(R.id.productImage);
        pd = new ProgressDialog(DetailsActivity.this);

        // Set Data From Intent

        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = Integer.parseInt(intent_harga);
        String formattedHarga = formatter.format(myNumber);

        nama.setText(intent_nama);
        harga.setText("Rp " + formattedHarga);
        kategori.setText("Kategori\t\t: " + intent_kategori);
        berat.setText("Berat\t\t\t\t\t: " + intent_berat + "gr");
        if(Integer.parseInt(intent_stok) == 0){
            stok.setText(Html.fromHtml("Stok\t\t\t\t\t: " + "<font color=red>" + "Habis!" + "</font>"));
        } else {
            stok.setText("Stok\t\t\t\t\t: " + intent_stok);
        }
        if (intent_deskripsi.isEmpty()) {
            deskripsi.setText("Tidak Ada Deskripsi....");
        } else {
            deskripsi.setText(intent_deskripsi);
        }

        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/produk/" + intent_gambar);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Picasso.get()
                .load(String.valueOf(url))
                .into(gambar);
    }

    public void onBack(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }
}