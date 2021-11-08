package com.vikrant.levelupacademy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{

    ArrayList<StudentNode> studentList;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public StudentAdapter(ArrayList<StudentNode> studentNodeArrayList) {
        this.studentList =studentNodeArrayList;
    }

    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,parent,false);
        MyViewHolder myViewHolder =new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(StudentAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(studentList.get(position).getName());
        holder.textView2.setText(studentList.get(position).getPhone());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

}
