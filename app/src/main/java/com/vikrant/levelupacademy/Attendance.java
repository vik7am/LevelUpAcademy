package com.vikrant.levelupacademy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, attendance;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    int dayNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(day[0]);
        initialize();
    }

    public void initialize() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<>(); attendance = new ArrayList<>();
        getStudentList();
        adapter= new AttendanceAdapter(this, name, attendance);
        recyclerView.setAdapter(adapter);

    }

    public void selectDay(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setItems(day, (dialog, which) -> updateDay(which));
        alertDialogBuilder.show();
    }

    public void updateDay(int which) {
        dayNo = which;
        adapter.dayNo = which;
        setTitle(day[which]);
        //Toast.makeText(this, ""+which, Toast.LENGTH_SHORT).show();
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            attendance.add(preferences.getString(""+dayNo+"Attendance"+ i, ""));
        }
        name.clear();
        attendance.clear();
        getStudentList();
        adapter.notifyDataSetChanged();
    }

    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        name.clear();
        attendance.clear();
        adapter.notifyDataSetChanged();
    }

    public void getStudentList() {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            attendance.add(preferences.getString(""+dayNo+"Attendance"+ i, ""));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}