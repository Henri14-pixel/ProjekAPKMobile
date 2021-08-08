package com.henribambangs.kapronpetshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Adapter.AdapterPaymentMethod;
import com.henribambangs.kapronpetshop.Adapter.AdapterProductCart;
import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelPaymentMethod;
import com.henribambangs.kapronpetshop.Model.ModelProductCart;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.AppController;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends AppCompatActivity implements Spinner.OnItemSelectedListener {

    RecyclerView mRecyclerview, mRecyclerviewPayment;
    static RecyclerView.Adapter mAdapter;
    RecyclerView.Adapter mAdapterPayment;
    RecyclerView.LayoutManager mManager, mManagerPayment;
    static List<ModelProductCart> mItems;
    List<ModelPaymentMethod> mItemsPayment;
    ProgressDialog pd;
    EditText amount;
    private static String paymentID;
    private static ModelUser user;
    TextView name, phone, address, area, totalHarga;
    ImageView profile;
    Button checkout;
    private static int total = 0;
    static boolean keranjangKosong = true;

    private Spinner spinner;

    private ArrayList<String> kurir;

    private JSONArray resultKurir;

    private String tipekurir = "Ambil Di Toko Kapron Petshop";
    private static String hargakurir = "0";

    private static Activity actv;

    @SuppressLint("CutPasteId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        actv = this;

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
        if (!String.valueOf(user.getPhone_number()).equals("null")) {
            phone.setText(String.valueOf(user.getPhone_number()));
        } else {
            phone.setText("-");
        }
        if (!String.valueOf(user.getAddress()).equals("null")) {
            address.setText(String.valueOf(user.getAddress()));
        } else {
            address.setText("Alamat belum diatur!");
        }
        if (!String.valueOf(user.getSubdistrict_name()).equals("null")) {
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
        loadTotal(this);

        mManager = new LinearLayoutManager(
                CartActivity.this, LinearLayoutManager.VERTICAL, false
        );

        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterProductCart(CartActivity.this, mItems);
        mRecyclerview.setAdapter(mAdapter);

        // Recyclerview Payment Method
        mRecyclerviewPayment = findViewById(R.id.recyclerviewTempPayment);
        mItemsPayment = new ArrayList<>();

        loadPaymentMethod();

        mManagerPayment = new LinearLayoutManager(
                CartActivity.this, LinearLayoutManager.VERTICAL, false
        );

        mRecyclerviewPayment.setLayoutManager(mManagerPayment);
        mAdapterPayment = new AdapterPaymentMethod(CartActivity.this, mItemsPayment);
        mRecyclerviewPayment.setAdapter(mAdapterPayment);

        // Spinner Kurir

        //Initializing the ArrayList
        kurir = new ArrayList<String>();

        //Initializing Spinner
        spinner = (Spinner) findViewById(R.id.spinnerKurir);

        //Adding an Item Selected Listener to our Spinner
        //As we have implemented the class Spinner.OnItemSelectedListener to this class iteself we are passing this to setOnItemSelectedListener
        spinner.setOnItemSelectedListener(this);

        getDataKurir();

        checkout = findViewById(R.id.btnCheckout);

        // Button Checkout Listener
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });
    }

    public void backHome(View view) {
        startActivity(new Intent(this, DashboardActivity.class));
    }


    public static void addUpdateProduk(String jml, String idPrdk, Context context) {

        System.out.println(jml);
        System.out.println(idPrdk);
        if (idPrdk != null) {
            StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_CARTADD,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getBoolean("success")) {
                                    Toast.makeText(
                                            context,
                                            "Pesan : " + res.getString("message"),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                    loadProduct();
                                } else {
                                    Toast.makeText(
                                            context,
                                            "Pesan : " + res.getString("message"),
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(
                                    context,
                                    "Pesan : " + error,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<>();
                    // Model User
                    map.put("cart_amount_item", jml);
                    map.put("customer_id", String.valueOf(user.getCustomer_id()));
                    map.put("product_id", idPrdk);

                    return map;
                }
            };

            AppController.getInstance().addToRequestQueue(sendData);

        }
    }

    public static void hapusProduk(String idPrdk, Context context) {
        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_CARTDELETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getBoolean("success")) {
                                Toast.makeText(
                                        context,
                                        "Pesan : " + res.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                                loadProduct();
                            } else {
                                Toast.makeText(
                                        context,
                                        "Pesan : " + res.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                context,
                                "Pesan : " + error,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                // Model User
                map.put("customer_id", String.valueOf(user.getCustomer_id()));
                map.put("product_id", idPrdk);

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);

    }

    private void checkout() {
        if (keranjangKosong) {
            Toast.makeText(CartActivity.this, "Pesan : Tidak ada produk yang dapat dicheckout!", Toast.LENGTH_SHORT).show();
        } else if (tipekurir == null) {
            Toast.makeText(CartActivity.this, "Pesan : Silahkan pilih metode pengiriman terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else if (getPaymentID() == null) {
            Toast.makeText(CartActivity.this, "Pesan : Silahkan pilih metode pembayaran terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else {
            StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_CHECKOUT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            pd.cancel();
                            try {
                                JSONObject res = new JSONObject(response);
                                if (res.getBoolean("success")) {
                                    Toast.makeText(CartActivity.this, "Pesan : " + res.getString("message"), Toast.LENGTH_SHORT).show();

                                    Intent update = new Intent(CartActivity.this, DetailTransaksiActivity.class);
                                    update.putExtra("order_id", res.getString("data"));

                                    finish();
                                    CartActivity.this.startActivity(update);
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
                                    CartActivity.this,
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
                    map.put("payment_method_id", getPaymentID());
                    map.put("jumlah", String.valueOf(hitungTotal()));
                    map.put("ongkir", hargakurir);
                    map.put("nama_kurir", tipekurir);

                    return map;
                }
            };

            AppController.getInstance().addToRequestQueue(sendData);

        }
    }

    private int hitungTotal() {
        return total + Integer.parseInt(hargakurir);
    }

    private static void loadTotal(Activity a) {
        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = total + Integer.parseInt(hargakurir);
        String hargaFormatted = formatter.format(myNumber);

        TextView totalHarga = a.findViewById(R.id.cartTotal);
        totalHarga.setText("Rp " + hargaFormatted);
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

    private static void loadProduct() {

        mItems.clear();
        total = 0;
        keranjangKosong = true;

        StringRequest reqData = new StringRequest(Request.Method.POST, ServerAPI.URL_PRODUCTCART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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

                                    if (Integer.parseInt(data.getString("product_stock")) >= 1) {
                                        keranjangKosong = false;
                                        total += (Integer.parseInt(data.getString("product_price")) * Integer.parseInt(data.getString("cart_amount_item")));

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            loadTotal(actv);
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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

    private void loadPaymentMethod() {
        mItemsPayment.clear();

        StringRequest reqData = new StringRequest(Request.Method.POST, ServerAPI.URL_PAYMENTMETHOD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject data = jsonArray.getJSONObject(i);
                                    ModelPaymentMethod md = new ModelPaymentMethod();
                                    md.setPayment_method_id(data.getString("payment_method_id"));
                                    md.setPayment_method_image(data.getString("payment_method_image"));
                                    md.setPayment_method_name(data.getString("payment_method_name"));
                                    mItemsPayment.add(md);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e2) {
                            e2.printStackTrace();
                        }

                        mAdapterPayment.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
//                map.put("customer_id", String.valueOf(user.getCustomer_id()));

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(reqData);
    }

    private void getDataKurir() {
        StringRequest reqData = new StringRequest(Request.Method.POST, ServerAPI.URL_DELIVERY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            resultKurir = j.getJSONArray("result");

                            //Calling method getStudents to get the students from the JSON Array
                            getKurirText(resultKurir);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley", "error : " + error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer_id", String.valueOf(user.getCustomer_id()));
                map.put("kab_id", String.valueOf(user.getCity_id()));

                return map;
            }
        };

        reqData.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(reqData);
    }

    private void getKurirText(JSONArray j) {
        //Traversing through all the items in the json array
        for (int i = 0; i < j.length(); i++) {
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                kurir.add(json.getString("text"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spinner.setAdapter(new ArrayAdapter<String>(CartActivity.this, android.R.layout.simple_spinner_dropdown_item, kurir));
    }

    //Method get data of a particular position
    private String getKurirName(int position) {
        String name = "";
        try {
            //Getting object of given index
            JSONObject json = resultKurir.getJSONObject(position);

            //Fetching name from that object
            name = json.getString("kurir");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    private String getKurirHarga(int position) {
        String harga = "";
        try {
            //Getting object of given index
            JSONObject json = resultKurir.getJSONObject(position);

            //Fetching name from that object
            harga = json.getString("harga");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return harga;
    }

    //this method will execute when we pic an item from the spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tipekurir = getKurirName(position);
        hargakurir = getKurirHarga(position);
        loadTotal(this);
    }

    //When no item is selected this method would execute
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        tipekurir = "";
        hargakurir = "";
    }

    public static void setPaymentID(String id) {
        paymentID = id;

    }

    public static String getPaymentID() {
        return paymentID;
    }
}