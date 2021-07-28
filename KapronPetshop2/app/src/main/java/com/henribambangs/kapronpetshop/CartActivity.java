package com.henribambangs.kapronpetshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductCart;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductList;
import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelProductCart;
import com.henribambangs.kapronpetshop.Model.ModelProductCategory;
import com.henribambangs.kapronpetshop.Model.ModelProductList;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.AppController;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelProductCart> mItems;
    ProgressDialog pd;
    EditText amount;
    private static String URL;
    private ModelUser user;
    TextView name, phone, address, area;
    ImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        changeStatusBarColor();

        // Check user Login
        if (!AdapterUser.getInstance(this).isLoggedIn()) {
            Toast.makeText(
                    CartActivity.this,
                    "Silahkan login terlebih dahulu!",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // User Information
        name = findViewById(R.id.cartPersonName);
        phone = findViewById(R.id.cartPersonPhone);
        address = findViewById(R.id.cartPersonAddress);
        area = findViewById(R.id.cartPersonAddressCourir);
        profile = findViewById(R.id.cartImageProfil);

        // Model User
        user = AdapterUser.getInstance(this).getUser();

        // Set User Information
        name.setText(String.valueOf(user.getCustomer_name()));
        if(!String.valueOf(user.getPhone_number()).equals("null")){
            phone.setText(String.valueOf(user.getPhone_number()));
        } else {
            phone.setText("-");
        }
        if(!String.valueOf(user.getAddress()).equals("null")){
            address.setText(String.valueOf(user.getAddress()));
        } else {
            address.setText("Alamat belum diatur!");
        }
        if(!String.valueOf(user.getSubdistrict_name()).equals("null")){
            area.setText(user.getProvince_name() + ", " + user.getCity_name() + ", " + user.getSubdistrict_name());
        } else {
            area.setText("Area pengiriman belum diatur!");
        }

        String urlImage = "http://192.168.43.29/KapronPetshop/assets/img/customer/" + user.getCustomer_image();
        Picasso.get()
                .load(String.valueOf(urlImage))
                .resize(400, 400).into(profile);

        // Recyclerview Product
        mRecyclerview = findViewById(R.id.recyclerviewTempCart);
        pd = new ProgressDialog(CartActivity.this);
        mItems = new ArrayList<>();

        loadProduct();

        mManager = new LinearLayoutManager(
                CartActivity.this, LinearLayoutManager.VERTICAL, false
        );

        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterProductCart(CartActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    public void backHome(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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

    private void loadProduct() {
        pd.setMessage("Sedang Memuat Produk");
        pd.setCancelable(false);
        pd.show();

        mItems.clear();

        StringRequest reqData = new StringRequest(Request.Method.POST, ServerAPI.URL_PRODUCTCART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelProductCart md = new ModelProductCart();
                                    md.setId(data.getString("product_id"));
                                    md.setName(data.getString("product_name"));
                                    md.setPrice(data.getString("product_price"));
                                    md.setImage(data.getString("product_image"));
                                    md.setStock(data.getString("product_stock"));
                                    md.setAmount(data.getString("cart_amount_item"));
                                    mItems.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer_id", String.valueOf(user.getCustomer_id()));

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(reqData);
    }
}