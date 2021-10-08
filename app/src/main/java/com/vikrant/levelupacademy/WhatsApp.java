package com.vikrant.levelupacademy;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class WhatsApp extends AppCompatActivity {

    RecyclerView recyclerView;
    WhatsAppAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<String> name, phone;
    ArrayList<StudentNode> studentNodeArrayList;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference studentRef = database.collection("Students");

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
        studentNodeArrayList = new ArrayList<>();
        getStudentList();
        //adapter= new WhatsAppAdapter(this, name, phone);
        //recyclerView.setAdapter(adapter);
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
                adapter= new WhatsAppAdapter(WhatsApp.this, studentNodeArrayList);
                recyclerView.setAdapter(adapter);
            }
        });

        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        for(int i=0; i<id; i++) {
            name.add(preferences.getString("Name"+ i, "Sample Name"));
            phone.add(preferences.getString("Phone"+ i, "Sample Name"));
            //attendance.add(preferences.getString("Attendance"+ i, "Sample Name"));
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}