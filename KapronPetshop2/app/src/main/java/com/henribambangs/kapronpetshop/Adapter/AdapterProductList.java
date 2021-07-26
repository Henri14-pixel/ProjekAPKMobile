package com.henribambangs.kapronpetshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.henribambangs.kapronpetshop.DetailsActivity;
import com.henribambangs.kapronpetshop.Model.ModelProductList;
import com.henribambangs.kapronpetshop.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.List;

public class AdapterProductList extends RecyclerView.Adapter<AdapterProductList.HolderData>{
    private List<ModelProductList> mItems;
    private Context context;

    public AdapterProductList(Context context, List<ModelProductList> items) {
        this.mItems = items;
        this.context = context;
    }

    @Override
    public HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_product, parent, false
        );
        HolderData holderData = new HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(HolderData holder, int position) {
        ModelProductList md = mItems.get(position);

        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = Integer.parseInt(md.getPrice());
        String formattedNumber = formatter.format(myNumber);

        holder.tvcategory.setText(md.getCategory());
        holder.tvname.setText(md.getName());
        holder.tvprice.setText("Rp " + formattedNumber);

        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/produk/"+md.getImage());
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
        TextView tvname, tvprice, tvcategory ;
        ImageView ivimage;
        ModelProductList md;

        public HolderData(View view) {
            super(view);

            tvcategory = (TextView) view.findViewById(R.id.productCategory);
            tvname = (TextView) view.findViewById(R.id.productName);
            tvprice = (TextView) view.findViewById(R.id.productPrice);
            ivimage = view.findViewById(R.id.productImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, DetailsActivity.class);
                    update.putExtra("id", md.getId());
                    update.putExtra("productCategory", md.getCategory());
                    update.putExtra("productName", md.getName());
                    update.putExtra("productPrice", md.getPrice());
                    update.putExtra("productImage", md.getImage());
                    update.putExtra("productStock", md.getStock());
                    update.putExtra("productWeight", md.getWeight());
                    update.putExtra("productDesc", md.getDescription());

                    context.startActivity(update);
                }
            });
        }
    }
}
