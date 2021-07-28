package com.henribambangs.kapronpetshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class AdapterProductCart  extends RecyclerView.Adapter<AdapterProductCart.HolderData> {
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
        holder.etjumlah.setText(md.getAmount());

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
        TextView tvname, tvharga;
        EditText etjumlah;
        ImageView ivimage;
        ModelProductCart md;

        public HolderData(View view) {
            super(view);

            tvname = view.findViewById(R.id.productNameCart);
            tvharga = view.findViewById(R.id.productPriceCart);
            etjumlah = view.findViewById(R.id.productTotalCart);
            ivimage = view.findViewById(R.id.productImageCart);

        }
    }
}
