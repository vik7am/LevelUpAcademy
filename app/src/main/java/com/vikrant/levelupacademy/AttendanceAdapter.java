package com.vikrant.levelupacademy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{

    ArrayList<String> name, attendance;
    Context context;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public AttendanceAdapter(Context context, ArrayList<String> name, ArrayList<String> attendance) {
        this.context = context;
        this.name = name;
        this.attendance = attendance;
    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,parent,false);
        MyViewHolder myViewHolder =new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(name.get(position));
        holder.textView2.setText(attendance.get(position));
        holder.itemView.setOnClickListener(v -> showDialog(position));
    }

    public void showDialog(int position) {
        String[] items = {"Present", "Absent", "Late"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setItems(items, (dialog, which) -> updateAttendance(items[which], position));
        alertDialogBuilder.show();
    }

    public void updateAttendance(String item, int position) {
        SharedPreferences preferences = context.getSharedPreferences("student", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Attendance"+ position, item);
        editor.apply();
        attendance.set(position, item);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

}
