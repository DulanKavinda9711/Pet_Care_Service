package com.example.happytailhub;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateActivity extends AppCompatActivity {

    ImageView updateImage;
    Button updateButton;
    EditText updateOwner, updatePet, updateType, updateAge, updateSex, updateColor, updateNotes;
    String imageUrl;
    String owner, pet, type, age, sex, color, spec;
    String key, oldImageUrl;
    Uri uri;
    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        updateButton = findViewById(R.id.updateButton);
        updateOwner = findViewById(R.id.updateOwner);
        updatePet = findViewById(R.id.updatePet);
        updateType = findViewById(R.id.updateType);
        updateAge = findViewById(R.id.updateAge);
        updateSex = findViewById(R.id.updateSex);
        updateColor = findViewById(R.id.updateColor);
        updateNotes = findViewById(R.id.updateNotes);
        updateImage = findViewById(R.id.updateImage);

        ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            uri = data.getData();
                            updateImage.setImageURI(uri);
                        } else {
                            Toast.makeText(UpdateActivity.this, "No Image Selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imageUrl = bundle.getString("Image");
            if (imageUrl != null) {
                Glide.with(UpdateActivity.this).load(bundle.getString("Image")).into(updateImage);
                updateOwner.setText(bundle.getString("Name"));
                updatePet.setText(bundle.getString("PetName"));
                updateType.setText(bundle.getString("Type"));
                updateAge.setText(bundle.getString("Age"));
                updateSex.setText(bundle.getString("Sex"));
                updateColor.setText(bundle.getString("Color"));
                updateNotes.setText(bundle.getString("Special Notes"));
                key = bundle.getString("key");
                oldImageUrl = bundle.getString("Image");
            } else {
                // Handle the case where "Image" is null
                Toast.makeText(UpdateActivity.this, "Image URL is null", Toast.LENGTH_SHORT).show();
            }
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Pet Details").child(key);

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent photoPicker = new Intent(Intent.ACTION_PICK);
                photoPicker.setType("image/*");
                activityResultLauncher.launch(photoPicker);
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    public void saveData() {
        if (uri == null) {
            Toast.makeText(UpdateActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            return;
        }
        storageReference = FirebaseStorage.getInstance().getReference().child("Pet Details").child(uri.getLastPathSegment());

        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri urlImage) {
                        imageUrl = urlImage.toString();
                        updateData();
                        dialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("UpdateActivity", "Image upload failed: " + e.getMessage(), e);
                Toast.makeText(UpdateActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    public void updateData() {
        owner = updateOwner.getText().toString().trim();
        pet = updatePet.getText().toString().trim();
        type = updateType.getText().toString().trim();
        age = updateAge.getText().toString().trim();
        sex = updateSex.getText().toString().trim();
        color = updateColor.getText().toString().trim();
        spec = updateNotes.getText().toString().trim();

        HelperClass helperClass = new HelperClass(owner, pet, type, age, sex, color, spec,imageUrl);

        databaseReference.setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(oldImageUrl);
                    reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("UpdateActivity", "Old image deletion successful");
                            Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("UpdateActivity", "Old image deletion failed: " + e.getMessage(), e);
                            Toast.makeText(UpdateActivity.this, "Failed to delete old image", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("UpdateActivity", "Database update failed: " + e.getMessage(), e);
                Toast.makeText(UpdateActivity.this, "Database update failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}