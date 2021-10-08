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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Attendance extends AppCompatActivity {

    RecyclerView recyclerView;
    AttendanceAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, attendance;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    int dayNo = 0;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference studentRef = database.collection("Students");
    CollectionReference attendanceRef = database.collection("Attendances");
    ArrayList<StudentNode> studentNodes;
    ArrayList<AttendanceNode> attendanceNodes;

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
        studentNodes = new ArrayList<>();
        attendanceNodes = new ArrayList<>();
        getStudentList();
        //adapter= new AttendanceAdapter(this, name, attendance);
        //recyclerView.setAdapter(adapter);

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
        attendanceNodes.clear();
        attendanceRef.whereEqualTo("day",day[dayNo])
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    AttendanceNode node = documentSnapshot.toObject(AttendanceNode.class);
                    node.setId(documentSnapshot.getId());
                    attendanceNodes.add(node);
                }
                adapter.attendanceNodes = attendanceNodes;
                adapter.notifyDataSetChanged();
            }
        });
        /*
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            attendance.add(preferences.getString(""+dayNo+"Attendance"+ i, ""));
        }
        name.clear();
        attendance.clear();
        getStudentList();*/

    }

    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        name.clear();
        attendance.clear();
        adapter.notifyDataSetChanged();
    }

    public void getStudentList() {

        studentRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    StudentNode node = documentSnapshot.toObject(StudentNode.class);
                    node.setId(documentSnapshot.getId());
                    studentNodes.add(node);
                }
                getAttendanceList();
            }
        });
        /*
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            attendance.add(preferences.getString(""+dayNo+"Attendance"+ i, ""));
        }*/
    }

    public void getAttendanceList() {
        attendanceRef.whereEqualTo("day",day[dayNo])
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    AttendanceNode node = documentSnapshot.toObject(AttendanceNode.class);
                    node.setId(documentSnapshot.getId());
                    attendanceNodes.add(node);
                }
                adapter= new AttendanceAdapter(Attendance.this, studentNodes, attendanceNodes);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}