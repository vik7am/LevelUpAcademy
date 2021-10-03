package com.vikrant.levelupacademy;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WhatsAppAdapter extends RecyclerView.Adapter<WhatsAppAdapter.MyViewHolder> {

    ArrayList<String> name, phone, attendance;
    String countryCode = "91";
    Context context;
    PackageManager packageManager;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public WhatsAppAdapter(Context context, ArrayList<String> name, ArrayList<String> phone,
                           ArrayList<String> attendance) {
        this.context = context;
        this.name = name;
        this.phone = phone;
        this.attendance = attendance;
        packageManager = context.getPackageManager();
    }

    @Override
    public WhatsAppAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(WhatsAppAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(name.get(position));
        holder.textView2.setText(attendance.get(position));
        holder.itemView.setOnClickListener(v -> sendMessage(position));
    }

    public void sendMessage(int position) {
        String message = name.get(position) + " was " + attendance.get(position) + " today.";
        Intent intent = new Intent();
        String url;
        try {
            PackageInfo info=packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
            url = "https://api.whatsapp.com/send?phone="+ countryCode + phone.get(position) +"&text=" +
                    URLEncoder.encode(message, "UTF-8");
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (NameNotFoundException e) {
            Toast.makeText(context, "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}