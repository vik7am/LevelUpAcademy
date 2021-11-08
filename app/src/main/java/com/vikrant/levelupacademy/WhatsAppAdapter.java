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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

public class WhatsAppAdapter extends RecyclerView.Adapter<WhatsAppAdapter.MyViewHolder> {

    ArrayList<String> name, phone, attendance;
    String countryCode = "91";
    Context context;
    PackageManager packageManager;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    //CollectionReference studentRef = database.collection("Students");
    CollectionReference attendanceRef = database.collection("class/10/attendance");
    ArrayList<StudentNode> studentList;
    ArrayList<AttendanceNode> attendanceList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public WhatsAppAdapter(Context context, ArrayList<StudentNode> studentNodes) {
        this.context = context;
        this.name = name;
        this.phone = phone;
        attendance= new ArrayList<>();
        packageManager = context.getPackageManager();
        this.studentList = studentNodes;
        attendanceList = new ArrayList<>();
    }

    @Override
    public WhatsAppAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row1, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(WhatsAppAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(studentList.get(position).getName());
        holder.textView2.setText("");
        holder.itemView.setOnClickListener(v -> getAttendance(position));
    }

    public void getAttendance(int position) {
        //System.out.println("$$$$$$$$$$$$$$$ ");
        attendanceList.clear();
        attendanceRef.document(""+position).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for(int i = 0; i<6; i++) {
                    if(documentSnapshot.get(""+day[i]) == null)
                        attendanceList.add(new AttendanceNode(""+position, day[i], "Not Marked"));
                    else
                    {
                        System.out.println("^^^^^^^^^^^^^^^666");
                        attendanceList.add(new AttendanceNode(""+position, day[i], documentSnapshot.get(""+day[i]).toString()));
                    }

                }
                sendMessage(position);
            }
        });
        /*
        attendanceRef.whereEqualTo("studentId", studentList.get(position).getId())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            AttendanceNode node = documentSnapshot.toObject(AttendanceNode.class);
                            node.setId(documentSnapshot.getId());
                            attendanceList.add(node);
                        }
                        sendMessage(position);
                    }
                });
*/
        /*attendance.clear();
        SharedPreferences preferences = context.getSharedPreferences("student", Context.MODE_PRIVATE);
        //int id = preferences.getInt("id",0);
        for(int i=0; i<6; i++) {
            attendance.add(preferences.getString("" + i + "Attendance" + position, ""));
        }
        System.out.println("$$$$$$$$$$$$$"+ attendance.size());*/
    }

    /*public String getStringAttendance(int i) {
        for(AttendanceNode a:attendanceList)
            if(a.getDay().equals(day[i]))
                return a.attendance;
        return "";
    }*/


    public void sendMessage(int position) {
        String packageName;
        //System.out.println("$$$$$$$$$$$$$$$ ");
        //System.out.println("$$$$$$$$$$$$$$$ "+appInstalledOrNot("com.whatsapp"));
        String message="Attendance of "+ studentList.get(position).getName() +":\n";
        //Toast.makeText(context, ""+ studentNodes.get(position).getPhone(), Toast.LENGTH_SHORT).show();


        for(int i=0; i<6; i++)
            message += day[i] + " : " + attendanceList.get(i).attendance + "\n";
        if(appInstalledOrNot("com.whatsapp"))
            packageName = "com.whatsapp";
        else if (appInstalledOrNot("com.whatsapp.w4b"))
            packageName = "com.whatsapp.w4b";
        else if (appInstalledOrNot("com.gbwhatsapp"))
            packageName = "com.gbwhatsapp";
        else {
            Toast.makeText(context, "WhatsApp is not Installed", Toast.LENGTH_SHORT).show();
            System.out.println(message);
            return;
        }
        Intent intent = new Intent();
        String url;
        try {
            PackageInfo info=packageManager.getPackageInfo(packageName, PackageManager.GET_META_DATA);
            url = "https://api.whatsapp.com/send?phone="+ countryCode + studentList.get(position).getPhone() +"&text=" +
                    URLEncoder.encode(message, "UTF-8");
            intent.setPackage(packageName);
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
        return studentList.size();
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