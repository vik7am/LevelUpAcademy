package com.vikrant.levelupacademy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{

    ArrayList<String> name, attendance;
    int noOfAttendance;
    ArrayList<StudentNode> studentList;
    ArrayList<AttendanceNode> attendanceList;
    AttendanceNode attendanceNode;
    Context context;
    String id;
    Map<String, Object> map;
    int i;
    int dayNo;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference attendanceRef = database.collection("class/10/attendance");
    DocumentReference dataRef = database.document("class/10");

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public AttendanceAdapter(Context context, ArrayList<StudentNode> studentList,
                             ArrayList<AttendanceNode> attendanceList, int noOfAttendance) {
        this.context = context;
        this.noOfAttendance = noOfAttendance;
        this.studentList = studentList;
        this.attendanceList = attendanceList;
        map = new HashMap();
    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,parent,false);
        MyViewHolder myViewHolder =new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(studentList.get(position).getName());
        holder.textView2.setText(attendanceList.get(position).attendance);
        holder.itemView.setOnClickListener(v -> showDialog(position));
    }

    public String getAttendance(int position) {
        for(AttendanceNode a:attendanceList)
            if(a.getId().equals(dayNo+studentList.get(position).getId()))
                return a.attendance;
        return "";
    }

    public void showDialog(int position) {
        String[] items = {"Present", "Absent", "Late"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setItems(items, (dialog, which) -> updateAttendance(items[which], position));
        alertDialogBuilder.show();
    }

    public void updateAttendance(String item, int position) {
        map.clear();
        map.put(day[dayNo], item);
        if(noOfAttendance == 0) {
            attendanceRef.document("" + position).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    attendanceList.get(position).attendance = item;
                    updateNoOfAttendance();
                }
            });
        }
        else {
            attendanceRef.document(""+position).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    attendanceList.get(position).attendance = item;
                    notifyDataSetChanged();
                }
            });
        }
    }

    void updateNoOfAttendance() {
        map.clear();
        map.put("no-of-attendance", (noOfAttendance +1));
        dataRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

}
