package com.henribambangs.kapronpetshop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.henribambangs.kapronpetshop.CartActivity;
import com.henribambangs.kapronpetshop.DashboardActivity;
import com.henribambangs.kapronpetshop.Model.ModelPaymentMethod;
import com.henribambangs.kapronpetshop.R;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterPaymentMethod extends RecyclerView.Adapter<AdapterPaymentMethod.HolderData> {
    private List<ModelPaymentMethod> mItems;
    private Context context;

    public AdapterPaymentMethod(Context context, List<ModelPaymentMethod> items) {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterPaymentMethod.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_payment_method, parent, false
        );
        AdapterPaymentMethod.HolderData holderData = new AdapterPaymentMethod.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterPaymentMethod.HolderData holder, int position) {
        ModelPaymentMethod md = mItems.get(position);
        holder.tvname.setText(md.getPayment_method_name());

        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/pembayaran/" + md.getPayment_method_image());
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
        TextView tvname;
        ImageView ivimage;
        RadioGroup rg;
        RadioButton rb;
        ModelPaymentMethod md;

        public HolderData(View view) {
            super(view);

            tvname = view.findViewById(R.id.cartPaymentName);
            ivimage = view.findViewById(R.id.cartPaymentImage);
            rg = view.findViewById(R.id.cartPaymentRG);
            rb = view.findViewById(R.id.cartPaymentRB);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (rb.isChecked()) {
                        rb.setChecked(false);
                    } else {
                        rb.setChecked(true);
                    }
                    String id = md.getPayment_method_id();
                    CartActivity.setPaymentID(id);
                }
            });
        }
    }
}
