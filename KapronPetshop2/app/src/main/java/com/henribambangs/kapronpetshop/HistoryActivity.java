package com.henribambangs.kapronpetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.henribambangs.kapronpetshop.Adapter.AdapterHistory;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductCart;
import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelHistory;
import com.henribambangs.kapronpetshop.Model.ModelProductCart;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.AppController;
import com.henribambangs.kapronpetshop.Util.ServerAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelHistory> mItems;
    ProgressDialog pd;
    private ModelUser user;
    EditText search;
    private static String URL;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Check user Login
        if (!AdapterUser.getInstance(this).isLoggedIn()) {
            Toast.makeText(
                    HistoryActivity.this,
                    "Silahkan login terlebih dahulu!",
                    Toast.LENGTH_SHORT
            ).show();
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }

        // Status bar colour
        changeStatusBarColor();

        // search
        search = findViewById(R.id.historySearch);

        // Base URL
        URL = ServerAPI.URL_HISTORY;

        // Edit Text Search Listener
        search.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Set Get
                    if (search.getText().toString() != null) {
                        URL = ServerAPI.URL_HISTORY + "?search=" + search.getText().toString();
                        loadHistory();
                    } else {
                        URL = ServerAPI.URL_HISTORY;
                    }
                    return true;
                }
                return false;
            }
        });

        search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Set Get
                    if (search.getText().toString() != null) {
                        URL = ServerAPI.URL_HISTORY + "?search=" + search.getText().toString();
                        loadHistory();
                    } else {
                        URL = ServerAPI.URL_HISTORY;
                    }

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
                        // Set Get
                        if (search.getText().toString() != null) {
                            URL = ServerAPI.URL_HISTORY + "?search=" + search.getText().toString();
                            loadHistory();
                        } else {
                            URL = ServerAPI.URL_HISTORY;
                        }

                        return true;
                    }
                }
                return false;
            }
        });

        // BottomNavigation Handler
        BottomNavigationView mBottomNavigation;

        mBottomNavigation = findViewById(R.id.bottom_navigation);

        Menu menu = mBottomNavigation.getMenu();
        menu.findItem(R.id.navHistory).setChecked(true);

        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navHome:
                        finish();
                        Intent Home = new Intent(HistoryActivity.this, DashboardActivity.class);
                        startActivity(Home);
                        break;
                    case R.id.navCart:
                        Intent Cart = new Intent(HistoryActivity.this, CartActivity.class);
                        startActivity(Cart);
                        break;
                    case R.id.navHistory:
                        Intent History = new Intent(HistoryActivity.this, HistoryActivity.class);
                        startActivity(History);
                        break;
                    case R.id.navProfile:
                        Intent Profile = new Intent(HistoryActivity.this, AppProfileActivity.class);
                        startActivity(Profile);
                        break;
                }

                return true;
            }
        });

        // Model User
        user = AdapterUser.getInstance(this).getUser();

        // Recyclerview Product
        mRecyclerview = findViewById(R.id.recyclerviewTempHistory);
        pd = new ProgressDialog(HistoryActivity.this);
        mItems = new ArrayList<>();

        loadHistory();

        mManager = new LinearLayoutManager(
                HistoryActivity.this, LinearLayoutManager.VERTICAL, false
        );

        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterHistory(HistoryActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.darkTextColor));
        }
    }

    public void onBack(View view) {

        startActivity(new Intent(this, DashboardActivity.class));
    }

    private void loadHistory() {
        pd.setMessage("Sedang Memuat Histori Pembelian");
        pd.setCancelable(false);
        pd.show();

        mItems.clear();

        StringRequest reqData = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pd.cancel();
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelHistory md = new ModelHistory();
                                    md.setOrder_id(data.getString("order_id"));
                                    md.setOrder_bukti(data.getString("order_bukti"));
                                    md.setOrder_date(data.getString("order_date"));
                                    md.setPayment_amount(data.getString("payment_amount"));
                                    md.setOrder_status(data.getString("order_status"));
                                    md.setProduct_name(data.getString("product_name"));
                                    md.setProduct_image(data.getString("product_image"));
                                    md.setJml(data.getString("jml"));
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