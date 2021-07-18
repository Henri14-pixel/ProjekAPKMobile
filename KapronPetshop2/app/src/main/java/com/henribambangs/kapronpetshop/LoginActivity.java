package com.henribambangs.kapronpetshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Adapter.AdapterUser;
import com.henribambangs.kapronpetshop.Model.ModelUser;
import com.henribambangs.kapronpetshop.Util.HttpsTrustManager;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.henribambangs.kapronpetshop.Util.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button cirLoginButton;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);

        if (AdapterUser.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(this, DashboardActivity.class));
        }

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        cirLoginButton = findViewById(R.id.cirLoginButton);
        pd = new ProgressDialog(LoginActivity.this);


        //if user presses on login
        //calling the method login
        findViewById(R.id.cirLoginButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    HttpsTrustManager.allowAllSSL();
                    userLogin();
                }
            }

            private boolean validate() {
                boolean temp = true;

                final String username = editTextEmail.getText().toString();
                final String password = editTextPassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(username)) {
                    editTextEmail.setError("Please enter your username");
                    editTextEmail.requestFocus();
                    temp = false;
                } else if (!username.matches(emailPattern)) {
                    editTextEmail.setError("Invalid Email Address");
                    editTextEmail.requestFocus();
                    temp = false;
                } else if (TextUtils.isEmpty(password)) {
                    editTextPassword.setError("Password Tidak Boleh Kosong");
                    editTextPassword.requestFocus();
                    temp = false;
                } else if (password.length() < 6) {
                    editTextPassword.setError("Password minimal terdiri dari 6 karakter");
                    editTextPassword.requestFocus();
                    temp = false;
                }
                return temp;
            }
        });

    }

    private void userLogin() {
        pd.setMessage("Authentication");
        pd.setCancelable(false);
        pd.show();

        final String username = editTextEmail.getText().toString();
        final String password = editTextPassword.getText().toString();

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ServerAPI.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        pd.cancel();

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            //if no error in response
                            if (obj.getBoolean("success")) {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject("data");

                                //creating a new user object
                                ModelUser data = new ModelUser(
                                        userJson.getInt("customer_id"),
                                        userJson.getString("login_id"),
                                        userJson.getString("customer_name"),
                                        userJson.getString("email"),
                                        userJson.getString("gender"),
                                        userJson.getString("address"),
                                        userJson.getString("phone_number"),
                                        userJson.getString("customer_image"),
                                        userJson.getString("province_id"),
                                        userJson.getString("city_id"),
                                        userJson.getString("subdistrict_id")
                                );

                                //storing the user in shared preferences
                                AdapterUser.getInstance(getApplicationContext()).userLogin(data);

                                //starting the profile activity
                                finish();
                                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                            } else {
                                Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("login_id", username);
                params.put("login_password", password);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }
}