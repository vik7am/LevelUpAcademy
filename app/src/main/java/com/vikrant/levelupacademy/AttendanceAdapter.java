package com.vikrant.levelupacademy;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.MyViewHolder>{

    ArrayList<String> name, attendance;
    ArrayList<StudentNode> studentNodes;
    ArrayList<AttendanceNode> attendanceNodes;
    AttendanceNode attendanceNode;
    Context context;
    int dayNo;
    String day[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    //CollectionReference studentRef = database.collection("Students");
    CollectionReference attendanceRef = database.collection("Attendances");

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(View view) {
            super(view);
            textView1 = view.findViewById(R.id.textView1);
            textView2 = view.findViewById(R.id.textView2);
        }
    }

    public AttendanceAdapter(Context context, ArrayList<StudentNode> studentNodes, ArrayList<AttendanceNode> attendanceNodes) {
        this.context = context;
        this.studentNodes = studentNodes;
        this.attendanceNodes = attendanceNodes;
    }

    @Override
    public AttendanceAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contactView= LayoutInflater.from(parent.getContext()).inflate(R.layout.row1,parent,false);
        MyViewHolder myViewHolder =new MyViewHolder(contactView);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(AttendanceAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(studentNodes.get(position).getName());
        holder.textView2.setText(getAttendance(position));
        holder.itemView.setOnClickListener(v -> showDialog(position));
    }

    public String getAttendance(int position) {
        for(AttendanceNode a:attendanceNodes)
            if(a.getId().equals(dayNo+studentNodes.get(position).getId()))
                return a.attendance;
        return "";
    }

    public void showDialog(int position) {
        String[] items = {"Present", "Absent", "Late"};
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setItems(items, (dialog, which) -> updateAttendance(items[which], position));
        alertDialogBuilder.show();
    }

    public void updateAttendance(String item, int position) {
        attendanceNode = new AttendanceNode(studentNodes.get(position).getId(), day[dayNo], item);
        //attendanceNode.setId(attendanceNodes.get(position).getId());
        attendanceRef.document(dayNo+attendanceNode.getStudentId()).set(attendanceNode)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        refreshAttendance();
                    }
                });
        /*SharedPreferences preferences = context.getSharedPreferences("student", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(""+dayNo+"Attendance"+ position, item);
        editor.apply();
        attendance.set(position, item);*/

    }
    public void refreshAttendance(){
        attendanceNodes.clear();
        attendanceRef.whereEqualTo("day",day[dayNo])
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots) {
                            AttendanceNode node = documentSnapshot.toObject(AttendanceNode.class);
                            node.setId(documentSnapshot.getId());
                            attendanceNodes.add(node);
                        }
                        notifyDataSetChanged();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return studentNodes.size();
    }

}
