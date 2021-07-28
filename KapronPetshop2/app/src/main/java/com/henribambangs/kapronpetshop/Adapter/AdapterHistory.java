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
import com.henribambangs.kapronpetshop.DetailTransaksiActivity;
import com.henribambangs.kapronpetshop.DetailsActivity;
import com.henribambangs.kapronpetshop.Model.ModelHistory;
import com.henribambangs.kapronpetshop.R;
import com.henribambangs.kapronpetshop.Util.ServerAPI;
import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdapterHistory extends RecyclerView.Adapter<AdapterHistory.HolderData> {
    private List<ModelHistory> mItems;
    private Context context;

    public AdapterHistory(Context context, List<ModelHistory> items) {
        this.mItems = items;
        this.context = context;
    }

    @NonNull
    @Override
    public AdapterHistory.HolderData onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.layout_history, parent, false
        );
        AdapterHistory.HolderData holderData = new AdapterHistory.HolderData(layout);
        return holderData;
    }

    @Override
    public void onBindViewHolder(AdapterHistory.HolderData holder, int position) {
        ModelHistory md = mItems.get(position);

        // number format
        NumberFormat formatter = new DecimalFormat("#,###");
        int myNumber = Integer.parseInt(md.getPayment_amount());
        String formattedNumber = formatter.format(myNumber);

        holder.tvid.setText("Order ID: " + md.getOrder_id());
        holder.tvtanggal.setText("Tanggal: " + myDateFormat(md.getOrder_date()));
        holder.tvtotal.setText("Rp " + formattedNumber);
        holder.tvstatus.setText("Status: " + md.getOrder_status());
        holder.tvnama.setText(md.getProduct_name());
        if (Integer.parseInt(md.getJml()) == 0) {
            holder.tvjml.setText("");
        } else {
            holder.tvjml.setText("dan " + md.getJml() + " barang lainnya....");
        }

        // Image
        URL url = null;
        try {
            url = new URL("http://192.168.43.29/KapronPetshop/assets/img/produk/" + md.getProduct_image());
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
        TextView tvid, tvtanggal, tvtotal, tvstatus, tvnama, tvjml;
        ImageView ivimage;
        ModelHistory md;

        public HolderData(View view) {
            super(view);

            tvid = view.findViewById(R.id.historyOrderID);
            tvtanggal = view.findViewById(R.id.historyTanggal);
            tvtotal = view.findViewById(R.id.historyTotal);
            tvstatus = view.findViewById(R.id.historyStatus);
            tvnama = view.findViewById(R.id.historyProductName);
            tvjml = view.findViewById(R.id.historyJumlahBarang);
            ivimage = view.findViewById(R.id.historyProductImage);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent update = new Intent(context, DetailTransaksiActivity.class);
                    update.putExtra("order_id", md.getOrder_id());
                    context.startActivity(update);
                }
            });
        }
    }

    public String myDateFormat(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
