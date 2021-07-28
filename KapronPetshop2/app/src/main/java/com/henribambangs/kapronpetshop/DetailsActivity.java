package com.henribambangs.kapronpetshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.AppController;
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
    Button btnKeranjang, btnBeli;
    ProgressDialog pd;
    private String intent_idProduct, intent_stok;
    private ModelUser user;
    private boolean addStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        changeStatusBarColor();

        //getting the current user
        user = AdapterUser.getInstance(this).getUser();

        // Get Data From Intent
        Intent data = getIntent();
        intent_idProduct = data.getStringExtra("id");
        String intent_kategori = data.getStringExtra("productCategory");
        String intent_nama = data.getStringExtra("productName");
        String intent_harga = data.getStringExtra("productPrice");
        String intent_gambar = data.getStringExtra("productImage");
        intent_stok = data.getStringExtra("productStock");
        String intent_berat = data.getStringExtra("productWeight");
        String intent_deskripsi = data.getStringExtra("productDesc");

        kategori = findViewById(R.id.productCategory);
        nama = findViewById(R.id.productName);
        harga = findViewById(R.id.productPrice);
        berat = findViewById(R.id.productWeight);
        stok = findViewById(R.id.productStock);
        deskripsi = findViewById(R.id.productDescription);
        gambar = findViewById(R.id.productImage);
        btnKeranjang = findViewById(R.id.btnKeranjang);
        btnBeli = findViewById(R.id.btnBeli);
        pd = new ProgressDialog(DetailsActivity.this);

        // Set Data From Intent

        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = Integer.parseInt(intent_harga);
        String formattedHarga = formatter.format(myNumber);

        nama.setText(intent_nama);
        harga.setText("Rp " + formattedHarga);
        kategori.setText("Kategori\t\t: " + intent_kategori);
        berat.setText("Berat\t\t\t\t\t: " + intent_berat + "gr");
        if (Integer.parseInt(intent_stok) == 0) {
            stok.setText(Html.fromHtml("Stok : " + "<font color=red>" + "Habis!" + "</font>"));
        } else {
            stok.setText("Stok : " + intent_stok);
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

        // Button Keranjang Listener
        btnKeranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check user Login
                if (!AdapterUser.getInstance(DetailsActivity.this).isLoggedIn()) {
                    Toast.makeText(
                            DetailsActivity.this,
                            "Silahkan login terlebih dahulu!",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                } else {
                    addToCart();
                }
            }
        });

        // Button Beli Listener
        btnBeli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check user Login
                if (!AdapterUser.getInstance(DetailsActivity.this).isLoggedIn()) {
                    Toast.makeText(
                            DetailsActivity.this,
                            "Silahkan login terlebih dahulu!",
                            Toast.LENGTH_SHORT
                    ).show();
                    finish();
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                } else {
                    addToCart();
                    if (addStatus) {
                        finish();
                        startActivity(new Intent(DetailsActivity.this, CartActivity.class));
                    }
                }
            }
        });
    }

    public void onBack(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.white));
        }
    }

    private void addToCart() {
        if (Integer.parseInt(intent_stok) <= 0) {
            Toast.makeText(getApplicationContext(), Html.fromHtml("<font color='#dc3545'>Maaf stok produk ini habis</font>"), Toast.LENGTH_SHORT).show();
            addStatus = false;
        } else {
            addStatus = true;
            pd.setMessage("Sedang menambahkan barang ke keranjang");
            pd.setCancelable(false);
            pd.show();

            StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_ADDTOCART,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getBoolean("success")) {
                                    Toast.makeText(getApplicationContext(), "Pesan : " + res.getString("message"), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Pesan : " + Html.fromHtml("<font color='#dc3545'>" + res.getString("message") + "</font>"), Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            pd.cancel();
                            Toast.makeText(
                                    DetailsActivity.this,
                                    "Pesan : Maaf server sedang sibuk, silahkan coba lagi.",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    map.put("customer_id", String.valueOf(user.getCustomer_id()));
                    map.put("product_id", String.valueOf(intent_idProduct));

                    return map;
                }
            };

            AppController.getInstance().addToRequestQueue(sendData);

        }
    }
}