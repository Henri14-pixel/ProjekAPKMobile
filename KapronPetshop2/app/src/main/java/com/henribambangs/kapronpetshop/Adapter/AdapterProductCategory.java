package com.henribambangs.kapronpetshop.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.henribambangs.kapronpetshop.DashboardActivity;
import com.henribambangs.kapronpetshop.Model.ModelProductCategory;
import com.henribambangs.kapronpetshop.R;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class AdapterProductCategory extends RecyclerView.Adapter<AdapterProductCategory.HolderData> {
    private List<ModelProductCategory> mItems;
    private Context context;

    public AdapterProductCategory(Context context, List<ModelProductCategory> items) {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterProductCategory.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_kategori, parent, false
        );
        AdapterProductCategory.HolderData holderData = new AdapterProductCategory.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterProductCategory.HolderData holder, int position) {
        ModelProductCategory md = mItems.get(position);
        holder.tvname.setText(md.getName());

        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/kategori/" + md.getImage());
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
        ModelProductCategory md;

        public HolderData(View view) {
            super(view);

            tvname = view.findViewById(R.id.productNameCategory);
            ivimage = view.findViewById(R.id.productImageCategory);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, DashboardActivity.class);
                    update.putExtra("idCategory", md.getId());
                    update.putExtra("productNameCategory", md.getName());

                    context.startActivity(update);
                }
            });
        }
    }
}