package com.example.happytailhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

public class DetailActivity extends AppCompatActivity {

    TextView detailOwner, detailPet, detailType, detailAge, detailSex, detailColor, detailSpec;
    ImageView detailImage;
    FloatingActionButton deleteButton, editButton;
    String key = "";
    String imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_avtivity);
        detailOwner = findViewById(R.id.detailOwner);
        detailPet = findViewById(R.id.detailPet);
        detailSex = findViewById(R.id.detailSex);
        detailAge = findViewById(R.id.detailAge);
        detailType = findViewById(R.id.detailType);
        detailColor = findViewById(R.id.detailColor);
        detailSpec = findViewById(R.id.detailSpec);
        detailImage = findViewById(R.id.detailImage);
        deleteButton = findViewById(R.id.deleteButton);
        editButton = findViewById(R.id.editButton);

        Bundle bundle = getIntent().getExtras();
        if(bundle !=null){
            detailOwner.setText(bundle.getString("Name"));
            detailPet.setText(bundle.getString("PetName"));
            detailSex.setText(bundle.getString("Sex"));
            detailAge.setText(bundle.getString("Age"));
            detailType.setText(bundle.getString("Type"));
            detailColor.setText(bundle.getString("Color"));
            detailSpec.setText(bundle.getString("Special Notes"));
            key = bundle.getString("key");
            imageUrl = bundle.getString("Image");
            Glide.with(this).load(bundle.getString("Image")).into(detailImage);

        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Pet Details");
                FirebaseStorage storage = FirebaseStorage.getInstance();

                StorageReference storageReference = storage.getReferenceFromUrl(imageUrl);
                storageReference.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                reference.child(key.toLowerCase()).removeValue();
                                Toast.makeText(DetailActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), CustomerActivity.class));
                                finish();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "File does not exist or there was an error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, UpdateActivity.class)
                        .putExtra("Name", detailOwner.getText().toString())
                        .putExtra("PetName",detailPet.getText().toString())
                        .putExtra("Sex", detailSex.getText().toString())
                        .putExtra("Age", detailAge.getText().toString())
                        .putExtra("Color", detailColor.getText().toString())
                        .putExtra("Special Notes", detailSpec.getText().toString())
                        .putExtra("Image", imageUrl)
                        .putExtra("key", key);
                startActivity(intent);
            }
        });
    }
}