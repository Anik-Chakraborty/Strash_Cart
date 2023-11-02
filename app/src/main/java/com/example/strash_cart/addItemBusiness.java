package com.example.strash_cart;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.ArrayList;


public class addItemBusiness extends AppCompatActivity {
    TextInputEditText ItemName, ItemDes, ItemPrice;
    RelativeLayout pickImage;
    ViewPager viewPager;
    Button btn_upload_item;
    Uri ImageUri;
    ProgressDialog progressDialog;
    ArrayList<Uri> ChooseImageList;
    ArrayList<String> UrlsList;
    FirebaseFirestore firestore;
    StorageReference storagereferce;
    FirebaseStorage mStorage;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    String UserPhone;
    ImageView btn_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item_business);

        ItemPrice = findViewById(R.id.item_price);
        ItemName = findViewById(R.id.item_name);
        ItemDes = findViewById(R.id.item_des);
        btn_upload_item = findViewById(R.id.btn_upload_item);
        pickImage = findViewById(R.id.pickImage);
        viewPager= findViewById(R.id.viewPager);
        btn_back = findViewById(R.id.btn_back);


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        String userID = firebaseUser.getUid();


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if(readUserDetails !=null){
                    UserPhone = readUserDetails.PhoneNumber;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(addItemBusiness.this, "User Phone number not fetched", Toast.LENGTH_SHORT).show();
            }
        });








        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading Data");
        progressDialog.setMessage("Please Wait While Uploading Your data...");

        ChooseImageList = new ArrayList<>();
        UrlsList = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance();
        storagereferce = mStorage.getReference();





        btn_upload_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UploadImages();
            }
        });

        pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CheckPermission();
                PickImageFromGallery();
            }
        });



    }



    private void CheckPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(addItemBusiness.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(addItemBusiness.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
            } else {
                PickImageFromGallery();
            }
        }
    }

    private void UploadImages() {

        // we need list that images urls
        for (int i = 0; i < ChooseImageList.size(); i++) {
            Uri IndividualImage = ChooseImageList.get(i);
            if (IndividualImage != null) {
                progressDialog.show();
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("ItemImages");
                final StorageReference ImageName = ImageFolder.child("Image" + i + ": " + IndividualImage.getLastPathSegment());
                ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UrlsList.add(String.valueOf(uri));
                                if (UrlsList.size() == ChooseImageList.size()) {
                                    StoreLinks(UrlsList);
                                }
                            }
                        });

                    }
                });
            } else {
                Toast.makeText(this, "Please fill All Field", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void StoreLinks(ArrayList<String> urlsList) {
        // now we need get text from EditText
        String Price = ItemPrice.getText().toString();
        String Name = ItemName.getText().toString();
        String Description = ItemDes.getText().toString();
        if (!TextUtils.isEmpty(Name) && !TextUtils.isEmpty(Description) && !TextUtils.isEmpty(Price) && ImageUri != null) {
            // now we need a model class
            ItemModel model = new ItemModel(Name, Description," " ,Price ,UrlsList, UserPhone);
            firestore.collection("Items").add(model).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    // now here we need Item id and set into model
                    model.setItemId(documentReference.getId());
                    firestore.collection("Items").document(model.getItemId())
                            .set(model, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            progressDialog.dismiss();
                      // if data uploaded successfully then show ntoast
                            Toast.makeText(addItemBusiness.this, "Your data Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    }
                });


                }
            });

        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please Fill All field", Toast.LENGTH_SHORT).show();
        }
        // if you want to clear viewpager after uploading Images
        ChooseImageList.clear();

    }

    private void PickImageFromGallery() {
       Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select images"), 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       Log.i("result", "inside activity result");
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) {
            Log.i("result", "result is ok");
            if (data.getClipData() != null) {
                Log.i("result", "fine");
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {
                    ImageUri = data.getClipData().getItemAt(i).getUri();
                    ChooseImageList.add(ImageUri);
                    SetAdapter();
                }
            } else if (data.getData() != null) {
                Log.i("result", "clip data is null");
                ImageUri = data.getData();
                ChooseImageList.add(ImageUri);
                SetAdapter();
            }
        }

    }
    private void SetAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(this, ChooseImageList);
        viewPager.setAdapter(adapter);
    }

}
