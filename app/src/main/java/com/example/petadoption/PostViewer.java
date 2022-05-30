package com.example.petadoption;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.petadoption.databinding.ActivityEditProfileBinding;
import com.example.petadoption.databinding.ActivityPostViewerBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PostViewer extends DrawerBaseActivity {
    private RecyclerView RV;
    private ArrayList<Animal> animalArrayList;
    private RVAdapter animalRVAdapter;
    private FirebaseFirestore db;

    // Dashboard stuff
    ActivityPostViewerBinding activityPostViewerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        activityPostViewerBinding = ActivityPostViewerBinding.inflate(getLayoutInflater());
        setContentView(activityPostViewerBinding.getRoot());
        allocateActivityTitle("Posts");

        // divider stuffs
        DividerItemDecoration itemDecoration= new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);

        RV = findViewById(R.id.recyclerview);
        RV.addItemDecoration(itemDecoration);

        db = FirebaseFirestore.getInstance();

        animalArrayList = new ArrayList<>();
        RV.setHasFixedSize(true);
        RV.setLayoutManager(new LinearLayoutManager(this));

        animalRVAdapter = new RVAdapter(animalArrayList, this);

        RV.setAdapter(animalRVAdapter);

        db.collection("Posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for(DocumentSnapshot d : list) {
                        Animal c = d.toObject(Animal.class);
                        animalArrayList.add(c);
                    }
                    animalRVAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(PostViewer.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostViewer.this, "Fail to get the data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
