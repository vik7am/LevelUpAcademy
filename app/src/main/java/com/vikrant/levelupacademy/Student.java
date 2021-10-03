package com.vikrant.levelupacademy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Student extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    @Override
    protected void onResume() {
        super.onResume();
        name.clear();
        phone.clear();
        getStudentList();
        adapter.notifyDataSetChanged();
    }

    public void initialize() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<>();
        phone = new ArrayList<>();
        getStudentList();
        adapter= new StudentAdapter(name, phone);
        recyclerView.setAdapter(adapter);
    }

    public void addStudent(View view) {
        startActivity(new Intent(this, AddStudent.class));
    }

    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        name.clear();
        phone.clear();
        adapter.notifyDataSetChanged();
    }
    public void getStudentList() {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            phone.add(preferences.getString("Phone"+ i, "Sample Number"));
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}