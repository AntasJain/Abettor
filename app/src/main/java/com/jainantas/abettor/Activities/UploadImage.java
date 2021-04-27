package com.jainantas.abettor.Activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jainantas.abettor.Models.Upload;
import com.jainantas.abettor.Preferences.PrefsData;
import com.jainantas.abettor.Preferences.SharedPreferencesHelper;
import com.jainantas.abettor.R;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class UploadImage extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    FloatingActionButton imgSelect;
    EditText filename;
    Button button;
    Uri imageUri;
    ProgressBar progressBar;
    ImageView image;
    StorageReference storageReference;
    FirebaseFirestore db;
    HashMap<String,Object> imgMap;
    String file_name;
    String doctype;
    ArrayList<String> list;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferencesHelper.init(this);
        setContentView(R.layout.upload_img);
        db=FirebaseFirestore.getInstance();
        list=new ArrayList<>();
        imgMap=new HashMap<>();
        imgSelect = findViewById(R.id.selectImage);
        filename = findViewById(R.id.filename);
        button = findViewById(R.id.buttonUpload);
        image = findViewById(R.id.imageUpload);
        progressBar = findViewById(R.id.progress);
        Intent intent=getIntent();
        doctype=SharedPreferencesHelper.getUserInfo("From",null);
        storageReference = FirebaseStorage.getInstance().getReference(doctype);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this).load(imageUri).into(image);
            image.setImageURI(imageUri);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    private void uploadFile() {
        progressBar.setVisibility(View.VISIBLE);
        file_name=new String();

        if (imageUri != null) {
            file_name=System.currentTimeMillis()+"."+getFileExtension(imageUri);
            StorageReference reference = storageReference
                    .child(file_name);
            reference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Upload Successful",Toast.LENGTH_LONG).show();
                       // addImageToDatabase(filename.getText().toString().trim());
                            addImageToDatabase();
                        //Upload upload=new Upload(filename.getText().toString().trim(),taskSnapshot.getUploadSessionUri().toString());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double stat = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                    progressBar.setProgress((int) stat);

                }
            });
        } else {
            Toast.makeText(this, "No file Selected", Toast.LENGTH_LONG).show();
        }
    }
    public void addImageToDatabase()
    {
        storageReference.child("/"+file_name).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                printit(uri);
            }
        });


    }
    public void printit(Uri uri)
    {
        String link=uri.toString();
        String name=filename.getText().toString().trim();
        imgMap.put("Title",name);
        imgMap.put("Link",link);
        imgMap.put("Time",new SimpleDateFormat("dMMyyyyHHmmss").format(Calendar.getInstance().getTime()));
        imgMap.put("FileName",file_name);

        db.collection(SharedPreferencesHelper.getUserInfo(PrefsData.emailId,null))
                .document(SharedPreferencesHelper.getUserInfo("To",null)).collection(doctype).document(name).set(imgMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getApplicationContext(),"Added",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Failed to Add",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
