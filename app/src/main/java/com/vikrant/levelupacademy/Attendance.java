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
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Attendance extends AppCompatActivity {

    RecyclerView recyclerView;
    boolean refreshAttendance;
    AttendanceAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, attendance;
    int noOfAttendance;
    String id;
    int noOfStudents;
    Map<String, Object> map;
    int i;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    int dayNo = 0;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    //CollectionReference studentRef = database.collection("Students");
    DocumentReference studentRef = database.document("class/10/student/list");
    CollectionReference attendanceRef = database.collection("class/10/attendance");
    DocumentReference dataRef = database.document("class/10");
    ArrayList<StudentNode> studentList;
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
        map = new HashMap();
        studentList = new ArrayList<>();
        attendanceNodes = new ArrayList<>();
        getNoOfStudents();
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
        refreshAttendance =true;
        getAttendance();
    }


    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        name.clear();
        attendance.clear();
        adapter.notifyDataSetChanged();
    }

    public void getStudentList() {
        if(noOfStudents == 0)
            return;
        studentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for(int i = 0; i < noOfStudents; i++){
                    studentList.add(new StudentNode(documentSnapshot.get(i+"n").toString(),
                            documentSnapshot.get(i+"p").toString()));
                }
                getAttendanceList();
            }
        });
    }

    public void getNoOfStudents() {
        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noOfStudents = Integer.parseInt(documentSnapshot.get("no-of-students").toString());
                getStudentList();
            }
        });
    }

    public void getAttendanceList() {
        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noOfAttendance = Integer.parseInt(documentSnapshot.get("no-of-attendance").toString());
                getAttendance();
            }
        });
    }

    public void getAttendance() {
        //if(noOfAttendance == 0) {
            for(i=0; i<studentList.size(); i++) {
                attendanceNodes.add(new AttendanceNode(""+i, day[dayNo], "Not Marked"));
            }
            if (refreshAttendance)
                adapter.notifyDataSetChanged();
            else {
                adapter= new AttendanceAdapter(Attendance.this, studentList, attendanceNodes, noOfAttendance);
                recyclerView.setAdapter(adapter);
            }
        }
        /*else {
            for(i=0; i<studentList.size();i++) {
                getAtt(i);
            }
        }
    }*/

    /*void getAtt(final int ii) {
            //System.out.println("$$$$$$$$$$$$$$$$"+ii);
            attendanceRef.document(""+ii).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    //System.out.println("%%%%%%%%%%%%%55 "+ documentSnapshot.get(day[dayNo]));
                    if(documentSnapshot.get(day[dayNo]) == null)
                        attendanceNodes.add(new AttendanceNode(""+ii, day[dayNo], "Not Marked"));
                    else
                        attendanceNodes.add(new AttendanceNode(""+ii, day[dayNo], documentSnapshot.get(day[dayNo]).toString()));
                    if(ii==studentList.size()-1) {
                        if(refreshAttendance)
                            adapter.notifyDataSetChanged();
                        else {
                            adapter= new AttendanceAdapter(Attendance.this, studentList, attendanceNodes, noOfAttendance);
                            recyclerView.setAdapter(adapter);
                        }
                        //System.out.println("$$$$$$$$$$$$$$$$ Launch");
                    }
                }
            });


    }*/

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}