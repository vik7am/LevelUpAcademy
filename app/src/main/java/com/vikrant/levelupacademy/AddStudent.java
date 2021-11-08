package com.vikrant.levelupacademy;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.HashMap;
import java.util.Map;

public class AddStudent extends AppCompatActivity {

    EditText name, phone;
    int noOfStudents;
    Map<String, Object> map;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    DocumentReference studentRef = database.document("class/10/student/list");
    //CollectionReference attendanceRef = database.collection("class/10/attendance");
    DocumentReference dataRef = database.document("class/10");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.editTextTextPersonName);
        phone = findViewById(R.id.editTextPhone);
        map = new HashMap<>();
        getNoOfStudents();
    }

    public void saveStudent(View view) {
        String studentName = name.getText().toString();
        String studentPhone = phone.getText().toString();
        StudentNode node = new StudentNode(studentName, studentPhone);
        map.clear();
        map.put(noOfStudents +"n", studentName);
        map.put(noOfStudents +"p", studentPhone);
        if(noOfStudents == 0) {
            studentRef.set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateNoOfStudents();
                }
            });
        }
        else {
            studentRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    updateNoOfStudents();
                }
            });
        }
    }

    public void updateNoOfStudents() {
        map.clear();
        map.put("no-of-students", "" + (noOfStudents + 1));
        dataRef.update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddStudent.this, "Student Added", Toast.LENGTH_SHORT).show();
            }
        });
        name.setText("");
        phone.setText("");
    }

    /*public void createAttendanceDocument() {
        map.clear();
        map.put("id", "0");
        attendanceRef.document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AddStudent.this, "Attendance table Added", Toast.LENGTH_SHORT).show();
                id = "" + (Integer.parseInt(id)+1);
            }
        });
    }*/

    public void getNoOfStudents() {
        dataRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                noOfStudents = Integer.parseInt(documentSnapshot.get("no-of-students").toString());
                //getStudentList();
            }
        });
    }

    /*public void updateCounter() {
        id = "" + (Integer.parseInt(id)+1);
        studentRef.document("List").update("id", id);
    }*/

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}