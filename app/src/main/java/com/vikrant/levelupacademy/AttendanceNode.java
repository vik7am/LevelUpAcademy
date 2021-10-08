package com.vikrant.levelupacademy;

import com.google.firebase.firestore.Exclude;

public class AttendanceNode {

    String id;
    String studentId;
    String day;
    String attendance;

    public AttendanceNode() {
    }

    public AttendanceNode(String studentId, String day, String attendance) {
        this.studentId = studentId;
        this.day = day;
        this.attendance = attendance;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getDay() {
        return day;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setId(String id) {
        this.id = id;
    }
}
