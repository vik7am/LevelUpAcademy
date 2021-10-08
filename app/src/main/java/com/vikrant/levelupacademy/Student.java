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

    RecyclerView recyclerView;
    StudentAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<StudentNode> studentNodeArrayList;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference studentRef = database.collection("Students");

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
            studentNodeArrayList.clear();
            getStudentList();
            adapter.notifyDataSetChanged();
        }

    }

    public void initialize() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        studentNodeArrayList = new ArrayList<>();
        getStudentList();

    }

    public void addStudent(View view) {
        startActivity(new Intent(this, AddStudent.class));
    }

    public void clearStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        preferences.edit().clear().apply();
        studentNodeArrayList.clear();
        adapter.notifyDataSetChanged();
    }
    public void getStudentList() {

        studentRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    StudentNode node = documentSnapshot.toObject(StudentNode.class);
                    node.setId(documentSnapshot.getId());
                    studentNodeArrayList.add(node);
                }
                adapter= new StudentAdapter(studentNodeArrayList);
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