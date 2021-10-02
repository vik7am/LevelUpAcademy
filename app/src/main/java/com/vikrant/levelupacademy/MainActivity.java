package com.vikrant.levelupacademy;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void student(View view) {
        startActivity(new Intent(this, Student.class));
    }

    public void attendance(View view) {
        startActivity(new Intent(this, Attendance.class));
    }

    public void whatsapp(View view) { startActivity(new Intent(this, WhatsApp.class)); }

}
