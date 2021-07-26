package com.henribambangs.kapronpetshop;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductCategory;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductList;
import com.henribambangs.kapronpetshop.Model.ModelProductCategory;
import com.henribambangs.kapronpetshop.Model.ModelProductList;
import com.henribambangs.kapronpetshop.Util.AppController;
import com.henribambangs.kapronpetshop.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView mRecyclerview, mRecyclerviewCategory;
    RecyclerView.Adapter mAdapter, mAdapterCategory;
    RecyclerView.LayoutManager mManager, mManagerCategory;
    List<ModelProductList> mItems;
    List<ModelProductCategory> mItemsCategory;
    ProgressDialog pd;
    EditText search;
    String intent_category, intent_search, URL;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        changeStatusBarColor();

        search = findViewById(R.id.productSearch);

        // Intent Data
        Intent data = getIntent();
        intent_search = data.getStringExtra("search");
        intent_category = data.getStringExtra("idCategory");

        // Set Get
        if (intent_search != null) {
            URL = ServerAPI.URL_PRODUCTLIST + "?search=" + intent_search;
            search.setText(intent_search);
        } else if (intent_category != null) {
            URL = ServerAPI.URL_PRODUCTLIST + "?kategori=" + intent_category;
        } else {
            URL = ServerAPI.URL_PRODUCTLIST;
        }

        // Edit Text Search Listener
        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    Intent update = new Intent(DashboardActivity.this, DashboardActivity.class);
                    update.putExtra("search", search.getText().toString());

                    startActivity(update);

                    handled = true;
                }
                return handled;
            }
        });

        search.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (search.getRight() - search.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        Intent update = new Intent(DashboardActivity.this, DashboardActivity.class);
                        update.putExtra("search", search.getText().toString());

                        startActivity(update);

                        return true;
                    }
                }
                return false;
            }
        });

        // Recyclerview Category
        mRecyclerviewCategory = findViewById(R.id.recyclerviewTempCategory);
        mItemsCategory = new ArrayList<>();

        loadCategory();

        mManagerCategory = new LinearLayoutManager(
                DashboardActivity.this, LinearLayoutManager.HORIZONTAL, false
        );

        mRecyclerviewCategory.setLayoutManager(mManagerCategory);
        mAdapterCategory = new AdapterProductCategory(DashboardActivity.this, mItemsCategory);
        mRecyclerviewCategory.setAdapter(mAdapterCategory);

        // Recyclerview Product
        mRecyclerview = findViewById(R.id.recyclerviewTemp);
        pd = new ProgressDialog(DashboardActivity.this);
        mItems = new ArrayList<>();

        loadProduct();

        mManager = new GridLayoutManager(
                DashboardActivity.this, 3
        );

        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterProductList(DashboardActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);

        // Bottom Navigation
        BottomNavigationView mBottomNavigation;

        mBottomNavigation = findViewById(R.id.bottom_navigation);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navHome:
                        Intent Home = new Intent(DashboardActivity.this, DashboardActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.navCart:
                        Intent Cart = new Intent(DashboardActivity.this, CartActivity.class);
                        startActivity(Cart);
                        break;
                    case R.id.navProfile:
                        Intent Profile = new Intent(DashboardActivity.this, ProfileActivity.class);
                        startActivity(Profile);
                        break;
                }

                return true;
            }
        });

    }

    private void loadProduct() {
        pd.setMessage("Sedang Memuat Produk");
        pd.setCancelable(false);
        pd.show();

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, URL,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("Length :" + response.length());
                pd.cancel();
                Log.d("volley", "response : " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ModelProductList md = new ModelProductList();
                        md.setId(data.getString("product_id"));
                        md.setCategory(data.getString("product_type_name"));
                        md.setName(data.getString("product_name"));
                        md.setPrice(data.getString("product_price"));
                        md.setImage(data.getString("product_image"));
                        md.setStock(data.getString("product_stock"));
                        md.setWeight(data.getString("product_weight"));
                        md.setDescription(data.getString("product_short_description"));
                        mItems.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Length :" + response.length());
                mAdapter.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }

    private void loadCategory() {

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.POST, ServerAPI.URL_PRODUCTCATEGORY,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                System.out.println("Length :" + response.length());
                Log.d("volley", "response : " + response.toString());
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject data = response.getJSONObject(i);
                        ModelProductCategory md = new ModelProductCategory();
                        md.setId(data.getString("product_type_id"));
                        md.setName(data.getString("product_type_name"));
                        md.setImage(data.getString("product_type_image"));
                        md.setDescription(data.getString("product_type_description"));
                        mItemsCategory.add(md);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Length :" + response.length());
                mAdapterCategory.notifyDataSetChanged();
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : " + error.getMessage());
                    }
                });

        AppController.getInstance().addToRequestQueue(reqData);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }
}
