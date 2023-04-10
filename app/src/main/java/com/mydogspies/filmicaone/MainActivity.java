package com.mydogspies.filmicaone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String KEY_PROJECT = "project";
    private static final String KEY_TEXT = "text";

    private EditText projectText;
    private EditText someText;
    private TextView someData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference dbRef = db.document("Projects/First Project");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        projectText = findViewById(R.id.projectText);
        someText = findViewById(R.id.someText);
        someData = findViewById(R.id.dbData);
    }

    public void saveBtn(View v) {
        String projectTitle = projectText.getText().toString();
        String projectText = someText.getText().toString();

        Map<String, Object> entry = new HashMap<>();
        entry.put(KEY_PROJECT, projectTitle);
        entry.put(KEY_TEXT, projectText);

        dbRef.set(entry)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(MainActivity.this, "New entry added to db", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }

    public void loadBtn(View v) {
        dbRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            String title = documentSnapshot.getString(KEY_PROJECT);
                            String text = documentSnapshot.getString(KEY_TEXT);
                            someData.setText("Project: " + title + "\n" + "Text: " + text);
                        } else {
                            Toast.makeText(MainActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                    }
                });
    }
}