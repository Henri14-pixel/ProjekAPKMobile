package com.henribambangs.kapronpetshop.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.henribambangs.kapronpetshop.CartActivity;
import com.henribambangs.kapronpetshop.DashboardActivity;
import com.henribambangs.kapronpetshop.Model.ModelProductCart;
import com.henribambangs.kapronpetshop.R;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

public class AdapterProductCart extends RecyclerView.Adapter<AdapterProductCart.HolderData> {
    private List<ModelProductCart> mItems;
    private Context context;

    public AdapterProductCart(Context context, List<ModelProductCart> items) {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterProductCart.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_cart, parent, false
        );
        AdapterProductCart.HolderData holderData = new AdapterProductCart.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterProductCart.HolderData holder, int position) {
        ModelProductCart md = mItems.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = Integer.parseInt(md.getPrice());
        String formattedNumber = formatter.format(myNumber);

        holder.tvname.setText(md.getName());
        holder.tvharga.setText("Rp " + formattedNumber);

        if (Integer.parseInt(md.getStock()) >= 1) {
            holder.etjumlah.setText(md.getAmount());
        } else {
            holder.etjumlah.setText(Html.fromHtml("<font color=red>" + "Habis!" + "</font>"));
            holder.etjumlah.setFocusable(false);
            holder.etjumlah.setEnabled(false);
        }
        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/produk/" + md.getImage());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.get()
                .load(String.valueOf(url))
                .into(holder.ivimage);

        holder.md = md;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class HolderData extends RecyclerView.ViewHolder {
        TextView tvname, tvharga, tvhapus;
        EditText etjumlah;
        ImageView ivimage;
        ModelProductCart md;

        public HolderData(View view) {
            super(view);

            tvname = view.findViewById(R.id.productNameCart);
            tvharga = view.findViewById(R.id.productPriceCart);
            ivimage = view.findViewById(R.id.productImageCart);

            etjumlah = view.findViewById(R.id.productTotalCart);
            // Edit Text Search Listener
            etjumlah.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                        // Set Get
                        String idPrdk = md.getId();
                        CartActivity.addUpdateProduk(etjumlah.getText().toString(), idPrdk, context);
                        return true;
                    }
                    return false;
                }
            });
            etjumlah.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    boolean handled = false;
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        // Set Get
                        String idPrdk = md.getId();
                        CartActivity.addUpdateProduk(etjumlah.getText().toString(), idPrdk, context);

                        handled = true;
                    }
                    return handled;
                }
            });

            tvhapus = view.findViewById(R.id.cartHapus);
            tvhapus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idPrdk = md.getId();
                    CartActivity.hapusProduk(idPrdk, context);
                }
            });
        }
    }
}
