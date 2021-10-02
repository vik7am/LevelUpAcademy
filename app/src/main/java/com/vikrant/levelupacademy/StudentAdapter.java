package com.vikrant.levelupacademy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.MyViewHolder>{

    ArrayList<String> name, phone;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView1, textView2;
        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public StudentAdapter(ArrayList<String> name, ArrayList<String> phone) {
        this.name = name;
        this.phone = phone;
    }

    @Override
    public StudentAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,parent,false);
        MyViewHolder myViewHolder =new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(StudentAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(name.get(position));
        holder.textView2.setText(phone.get(position));
    }

    @Override
    public int getItemCount() {
        return name.size();
    }

}
