package com.vikrant.levelupacademy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class Student extends AppCompatActivity {

    //String ids = "";
    int noOfStudents;
    RecyclerView recyclerView;
    StudentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StudentNode> studentList;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference studentRef = database.document("class/10/student/list");
    DocumentReference dataRef = database.document("class/10");

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
        if(adapter != null) {
            studentList.clear();
            getNoOfStudents();
            adapter.notifyDataSetChanged();
        }
    }

    public void initialize() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentList = new ArrayList<>();
        getNoOfStudents();

    }

    public void addStudent(View view) {
        startActivity(new Intent(this, AddStudent.class));
    }

    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        studentList.clear();
        adapter.notifyDataSetChanged();
    }

    public void getStudentList() {
        if(noOfStudents ==0)
            return;
        studentRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                for(int i = 0; i < noOfStudents; i++){
                    studentList.add(new StudentNode(documentSnapshot.get(i+"n").toString(),
                            documentSnapshot.get(i+"p").toString()));
                }
                adapter= new StudentAdapter(studentList);
                recyclerView.setAdapter(adapter);
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

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}