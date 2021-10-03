package com.vikrant.levelupacademy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public WhatsAppAdapter(Context context, ArrayList<String> name, ArrayList<String> phone) {
        this.context = context;
        this.name = name;
        this.phone = phone;
        attendance= new ArrayList<>();
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
        holder.textView2.setText("");
        holder.itemView.setOnClickListener(v -> sendMessage(position));
    }

    public void getAttendance(int position) {
        attendance.clear();
        SharedPreferences preferences = context.getSharedPreferences("student", Context.MODE_PRIVATE);
        //int id = preferences.getInt("id",0);
        for(int i=0; i<6; i++) {
            attendance.add(preferences.getString("" + i + "Attendance" + position, ""));
        }
        System.out.println("$$$$$$$$$$$$$"+ attendance.size());
    }

    public void sendMessage(int position) {
        String packageName;
        getAttendance(position);
        String message="Attendance of "+ name.get(position) +":\n";
        for(int i=0; i<6; i++)
            message += day[i] + " : " + attendance.get(i) + "\n";
        if(appInstalledOrNot("com.whatsapp"))
            packageName = "com.whatsapp";
        else if (appInstalledOrNot("com.whatsapp.w4b"))
            packageName = "com.whatsapp.w4b";
        else {
            Toast.makeText(context, "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent();
        String url;
        try {
            PackageInfo info=packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            url = "https://api.whatsapp.com/send?phone="+ countryCode + phone.get(position) +"&text=" +
                    URLEncoder.encode(message, "UTF-8");
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (NameNotFoundException e) {
            //Toast.makeText(context, "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return name.size();
    }

    public boolean appInstalledOrNot(String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}