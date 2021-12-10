package com.pucmm.isc581_ecommerce.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.Utils.Validator;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.ProductsDB;
import com.pucmm.isc581_ecommerce.models.Category;
import com.pucmm.isc581_ecommerce.models.Product;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;

public class ManageProductActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText uuidET;
    private EditText nameET;
    private EditText priceET;
    private ExtendedFloatingActionButton fab;
    private ExtendedFloatingActionButton cancelFab;
    private ImageView imageView;
    private Uri imageUri;
    private String imageUrl = "";
    private Spinner product_spn;
    private Category spn_opt;
    private DatabaseReference myRef = MainActivity.database.getReference("categories");
    private ArrayList<Category> categories;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_product);

        uuidET = findViewById(R.id.reg_product_uuid);
        nameET = findViewById(R.id.reg_product_name);
        priceET = findViewById(R.id.reg_product_price);
        fab = findViewById(R.id.reg_product_button);
        cancelFab = findViewById(R.id.reg_product_cancel);
        product_spn = findViewById(R.id.reg_product_categories);

        imageView = findViewById(R.id.reg_product_image);
        progressBar = findViewById(R.id.reg_product_progressbar);

        //Deactivate touch
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        categories = new ArrayList<>();
        Bundle extras = getIntent().getExtras();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    //SAVES EACH CATEGORY TO THE ARRAY
                    Category value = categorySnapshot.getValue(Category.class);
                    categories.add(0,value);
                }

                String[] cat = new String[categories.size()];
                int i = 0;
                for (Category category : categories) {
                    cat[i] = category.getName();
                    i++;
                }

                ArrayAdapter adapter = new ArrayAdapter(ManageProductActivity.this,android.R.layout.simple_spinner_item,cat);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                product_spn.setAdapter(adapter);
                product_spn.setOnItemSelectedListener(ManageProductActivity.this);

                //Activate Touch
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                progressBar.setVisibility(View.GONE);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DB VALUES CATEGORIES", "Failed to read value.", error.toException());
            }
        });


        if(extras != null) {
            nameET.setText(extras.getString("ProductName"));
            priceET.setText(String.valueOf(extras.getFloat("ProductPrice")));
            uuidET.setText(extras.getString("ProductID"));
            uuidET.setEnabled(false);
            imageUrl = extras.getString("ProductURL");
            Glide.with(this).load(imageUrl).into(imageView);
            fab.setText("Update");
        }
        cancelFab.setVisibility(View.VISIBLE);

        imageView.setOnClickListener( v-> {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        });

        fab.setOnClickListener(v-> {
//            if(Validator.isEmpty(uuidET, nameET,priceET)) return;
            if(Validator.isEmpty(nameET,priceET)) return;

            StorageReference productImageRef = FirebaseStorage.getInstance().
                    getReference("ProductsPics/" + System.currentTimeMillis() + ".jpg");

            String name = nameET.getText().toString();
            String uuid = "2";
            Log.wtf("uuid", uuidET.getText().toString());
            String price = priceET.getText().toString();

            if(imageUri != null) {

                productImageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        //GET IMAGE URL TO CREATE ARTICLE
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.wtf("Product created", "CREATED IMAGE ON STORAGE, URL : " + uri.toString());
                                imageUrl = uri.toString();
                                Product product;
                                ArrayList<Product> products = spn_opt.getProducts();
                                if(products == null) {
                                    products = new ArrayList<>();
                                }

                                if(extras != null) {
                                    product = new Product(extras.getString("ProductID"),name,imageUrl,Float.valueOf(price), spn_opt.getId());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        products.forEach(product1 -> {
                                            if(product1.getUuid().equals(extras.getString("ProductID"))) {
                                                product1.setCategoryID(spn_opt.getId());
                                                product1.setImageUrl(imageUrl);
                                                product1.setName(name);
                                                product1.setPrice(Float.valueOf(price));
                                            }
                                        });
                                    }
                                }else {
                                    product = new Product(uuid,name,imageUrl,Float.valueOf(price), spn_opt.getId());
                                    products.add(product);

                                }

                                ProductsDB.manageProducts(product.getUuid(), product);
                                CategoriesDB.updateProducts(spn_opt.getId(), products);
                                Toast.makeText(ManageProductActivity.this, "Product Created", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ManageProductActivity.this, e.getMessage(),
                                        Toast.LENGTH_SHORT).show();

                            }
                        });



            }
            else if (imageUrl != ""){
                Product modProduct = new Product(extras.getString("ProductID"),name,imageUrl,Float.valueOf(price), spn_opt.getId());
                ArrayList<Product> products = spn_opt.getProducts();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                   if(products != null) {
                        products.forEach(product1 -> {
                            if (product1.getUuid().equals(extras.getString("ProductID"))) {
                                product1.setCategoryID(spn_opt.getId());
                                product1.setImageUrl(imageUrl);
                                product1.setName(name);
                                product1.setPrice(Float.valueOf(price));
                            }
                        });
                    }
                }

                CategoriesDB.updateProducts(spn_opt.getId(), products);
                ProductsDB.manageProducts(modProduct.getUuid(), modProduct);

                finish();
            }
            else{
                Toast.makeText(ManageProductActivity.this, "Image is needed", Toast.LENGTH_SHORT).show();
                return;
            }
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

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        spn_opt = categories.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}