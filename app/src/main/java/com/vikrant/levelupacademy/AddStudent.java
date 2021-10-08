package com.vikrant.levelupacademy;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddStudent extends AppCompatActivity {

    EditText name, phone;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    CollectionReference studentRef = database.collection("Students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.editTextTextPersonName);
        phone = findViewById(R.id.editTextPhone);
    }

    public void saveStudent(View view) {
        String studentName = name.getText().toString();
        String studentPhone = phone.getText().toString();
        StudentNode node = new StudentNode(studentName, studentPhone);
        studentRef.add(node).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddStudent.this, "Student Added", Toast.LENGTH_SHORT).show();
            }
        });
        name.setText("");
        phone.setText("");
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}