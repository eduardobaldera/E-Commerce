package com.pucmm.isc581_ecommerce.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.Utils.Validator;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.models.Category;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ManageCategoryActivity extends AppCompatActivity {

    private ImageView imageView;
    private EditText nameET;
    private ExtendedFloatingActionButton fab;
    private ExtendedFloatingActionButton cancelFab;
    private Uri imageUri;
    private String imageUrl = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_category);


        nameET = findViewById(R.id.category_name_create);
        imageView = findViewById(R.id.category_image_create);
        fab = findViewById(R.id.create_category_button);
        cancelFab = findViewById(R.id.cancel_category_update_button);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            nameET.setText(extras.getString("CategoryName"));
            imageUrl = extras.getString("CategoryURL");
            Glide.with(this).load(imageUrl).into(imageView);
            cancelFab.setVisibility(View.VISIBLE);
            fab.setText("Update");
        }

        imageView.setOnClickListener( v-> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        });

        fab.setOnClickListener(v -> {
            if(Validator.isEmpty(nameET)) {
                return;
            }

            StorageReference newsImageRef = FirebaseStorage.getInstance().
                        getReference("CategoriesPics/" + System.currentTimeMillis() + ".jpg");
            String name = nameET.getText().toString();

            if(imageUri != null) {

                newsImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //GET IMAGE URL TO CREATE ARTICLE
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.wtf("Category created", "CREATED IMAGE ON STORAGE, URL : " + uri.toString());
                                imageUrl = uri.toString();
                                Category category;
                                if(extras != null) {
                                    category = new Category(extras.getString("CategoryID"), name,imageUrl);
                                }else {
                                    String id = CategoriesDB.generateID();
                                    category = new Category(id, name,imageUrl);
                                }
                                CategoriesDB.manageCategory(category.getId(), category);
                                Toast.makeText(ManageCategoryActivity.this, "Category Created", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ManageCategoryActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });



            }else if (imageUrl != ""){
                Category category = new Category(extras.getString("CategoryID"), name,imageUrl);
                CategoriesDB.manageCategory(category.getId(), category);
                finish();
            }

            else{
                Toast.makeText(ManageCategoryActivity.this, "Image is needed", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        cancelFab.setOnClickListener(v-> {
            finish();
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE &&
                resultCode == RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            imageView.setImageURI(imageUri);
            imageUrl = "";

        }
    }
}