package com.social.businessdirectory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.social.businessdirectory.Model.StateVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Count extends AppCompatActivity {

    Button btnSave;
    EditText editTextName;
    private DatabaseReference mDatabase;
    private ValueEventListener mListener;
    String valueFromDB = "";
    int businessCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        btnSave = findViewById(R.id.btnSave);
        editTextName = findViewById(R.id.edName);


        mDatabase = FirebaseDatabase.getInstance().getReference().child("test");
        readIDFromDB();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDate();
            }
        });

    }

    public void saveDate() {
        businessCount++;

        String uniqueID = UUID.randomUUID().toString();

            String uploadId = mDatabase.push().getKey();
            Map<String, Object> dbMap = new HashMap<>();
            dbMap.put("businessID", uniqueID);
            dbMap.put("businessCount", businessCount);
            dbMap.put("postID", uploadId);

            mDatabase.child(uploadId).setValue(dbMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                  //  String uploadId = mDatabase.push().getKey();
                    Map<String, Object> dbMap = new HashMap<>();
                    dbMap.put("businessID", uniqueID);
                    dbMap.put("businessCount", businessCount);
                    dbMap.put("update", "update");
                    dbMap.put("postID", uploadId);
                    mDatabase.child(uploadId).child("business").updateChildren(dbMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Count.this, "onSuccess", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Count.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            });


    }

    private void readIDFromDB() {

        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                categoriesList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    valueFromDB = ds.child("businessID").getValue(String.class);
                    businessCount = Integer.parseInt(String.valueOf(ds.child("businessCount").getValue(int.class)));

                }
                Toast.makeText(Count.this, "Value:" + valueFromDB

                        , Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //   progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}