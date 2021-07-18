package com.henribambangs.kapronpetshop;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.henribambangs.kapronpetshop.Util.AppController;
import com.henribambangs.kapronpetshop.Util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextName, editTextEmail, editTextPassword, editTextRePassword;
    Button cirRegisterButton;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRePassword = findViewById(R.id.editTextRePassword);
        cirRegisterButton = findViewById(R.id.cirRegisterButton);
        pd = new ProgressDialog(RegisterActivity.this);


        cirRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validate()) {
                    simpanData();
                }
            }

            private boolean validate() {
                boolean temp = true;
                String checkname = editTextName.getText().toString();
                String checkemail = editTextEmail.getText().toString();
                String pass = editTextPassword.getText().toString();
                String cpass = editTextRePassword.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(checkname)) {
                    editTextName.setError("Nama Tidak Boleh Kosong");
                    editTextName.requestFocus();
                    temp = false;
                } else if (TextUtils.isEmpty(checkemail)) {
                    editTextEmail.setError("Email Tidak Boleh Kosong");
                    editTextEmail.requestFocus();
                    temp = false;
                } else if (!checkemail.matches(emailPattern)) {
                    editTextEmail.setError("Invalid Email Address");
                    editTextEmail.requestFocus();
                    temp = false;
                } else if (TextUtils.isEmpty(pass)) {
                    editTextPassword.setError("Password Tidak Boleh Kosong");
                    editTextPassword.requestFocus();
                    temp = false;
                } else if (pass.length() < 6) {
                    editTextPassword.setError("Password minimal terdiri dari 6 karakter");
                    editTextPassword.requestFocus();
                    temp = false;
                } else if (!pass.equals(cpass)) {
                    editTextRePassword.setError("Password Tidak Sama");
                    editTextRePassword.requestFocus();
                    temp = false;
                }
                return temp;
            }
        });
    }


    private void simpanData() {

        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();

        StringRequest sendData = new StringRequest(Request.Method.POST, ServerAPI.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        boolean status = false;

                        pd.cancel();
                        try {
                            JSONObject res = new JSONObject(response);
                            if (res.getBoolean("success")) {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Pesan : " + res.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();

                                status = true;
                            } else {
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Pesan : " + res.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                                if (res.getString("message").contains("Error Username / Email Sudah Ada")){
                                    editTextEmail.setError("Error Username / Email Sudah Ada");
                                    editTextEmail.requestFocus();
                                }
                                status = false;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        if (status) {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
                        Toast.makeText(
                                RegisterActivity.this,
                                "Pesan : Maaf server sedang sibuk, silahkan coba lagi.",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("customer_name", editTextName.getText().toString());
                map.put("login_id", editTextEmail.getText().toString());
                map.put("password", editTextPassword.getText().toString());

                return map;
            }
        };

        AppController.getInstance().addToRequestQueue(sendData);
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
