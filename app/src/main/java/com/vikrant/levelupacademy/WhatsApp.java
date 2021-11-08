package com.vikrant.levelupacademy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;

public class WhatsApp extends AppCompatActivity {

    int noOfStudents;
    RecyclerView recyclerView;
    WhatsAppAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, phone;
    ArrayList<StudentNode> studentList;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference studentRef = database.document("class/10/student/list");
    DocumentReference dataRef = database.document("class/10");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initialize();
    }

    public void initialize() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        name = new ArrayList<>(); phone = new ArrayList<>();
        studentList = new ArrayList<>();
        getNoOfStudents();
        //adapter= new WhatsAppAdapter(this, name, phone);
        //recyclerView.setAdapter(adapter);
    }


    /*public void getStudentList() {

        studentRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                    StudentNode node = documentSnapshot.toObject(StudentNode.class);
                    node.setId(documentSnapshot.getId());
                    studentNodeArrayList.add(node);
                }
                a
            }
        });
    }*/

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
                adapter= new WhatsAppAdapter(WhatsApp.this, studentList);
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