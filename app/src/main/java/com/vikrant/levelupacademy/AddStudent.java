package com.vikrant.levelupacademy;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class AddStudent extends AppCompatActivity {

    EditText name, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name = findViewById(R.id.editTextTextPersonName);
        phone = findViewById(R.id.editTextPhone);
    }

    public void saveStudent(View view) {
        SharedPreferences preferences = getSharedPreferences("student", Context.MODE_PRIVATE);
        int id = preferences.getInt("id",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Name"+ id, name.getText().toString());
        editor.putString("Phone"+ id, phone.getText().toString());
        editor.putInt("id", id+1);
        editor.apply();
        name.setText("");
        phone.setText("");
        Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}