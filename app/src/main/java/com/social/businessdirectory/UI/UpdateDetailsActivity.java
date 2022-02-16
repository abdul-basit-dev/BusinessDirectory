package com.social.businessdirectory.UI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.social.businessdirectory.Adapters.MyAdapterListing;
import com.social.businessdirectory.Model.CompanyModel;
import com.social.businessdirectory.Model.StateVO;
import com.social.businessdirectory.R;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class UpdateDetailsActivity extends AppCompatActivity {
    Button btnSubmit, btnChooseImage;
    ImageView ivBusinessCard;
    TextView tvEditDetails;

    TextInputLayout mMobile, companyName, firstName, lastName, landline, email, website, addressLine1, addressLine2;
    TextInputLayout pinCode, city, state, country;

    CheckBox cbMobile, cbLandLine, cbEmail, cbWebsite, cbWholeSale, cbRetails, cbTerms;
    // String cMobile = "true", cLandLine = "true", cEmail = "true", cWebsite = "true", cWholeSale = "true", cRetails = "true", cTerms;
    private TextView btnClose;
    private static final int GALLERY = 2;
    static final int REQUEST_TAKE_PHOTO = 1;
    String currentPhotoPath;
    Uri photoURI;
    Uri myImageUri;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    String imageUrl;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    //FireBase
    FirebaseFirestore mFireStore;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private MyAdapterListing adapterListing;
    private List<CompanyModel> companyModelList;
    ProgressDialog progressDialog;
    TextView tvInfo;
    ScrollView scrollView;

    private ValueEventListener mListener, mListenerCats;

    String catName, catImage;
    ArrayAdapter<String> adapterCats;
    ArrayList<String> usersArray;
    ArrayList<StateVO> listVOs;
    ArrayList<String> selectedItems;
    private DatabaseReference mDatabaseRef;


    String scompanyName, sfirstName, slastName, smobile, slandline, semail, swebsite, sbusinessCard, sadress1, sadress2, spincode;
    String scity, sstate, scountry, sstatus, spostID, sBusinessDetails, sCategory;
    String scMobile, scLandLine, scEmail, scWebsite, scWholeSale, scRetails, scTerms;

    TextView tvSelectedCats;
    EditText businessDetails;
    ListView listView;
    String[] outputStrArr;
    LinearLayout layoutViewCats;
    CardView cardViewCats, cvBusiness;
    Button btnSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_update_details);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView tvTitle;
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        ivBack = toolbar.findViewById(R.id.ivBack);
        tvTitle.setText("Update Details");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //setting up toolbar Finished...

        FirebaseApp.initializeApp(this);

        mFireStore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("business_app:").child("business");
        mStorage = FirebaseStorage.getInstance().getReference().child("business_images");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business_app:").child("categories");

        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait...");


        //initialize views

        layoutViewCats = findViewById(R.id.layoutViewCats);
        cardViewCats = findViewById(R.id.cardViewCats);
        cvBusiness = findViewById(R.id.cvBusiness);
        btnSave = findViewById(R.id.btnSave);

        businessDetails = findViewById(R.id.businessDetails);
        tvSelectedCats = findViewById(R.id.tvSelectedCats);
        listView = findViewById(R.id.listview);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        ivBusinessCard = findViewById(R.id.ivBusinessCard);
        mMobile = findViewById(R.id.mMobile);
        companyName = findViewById(R.id.companyName);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        landline = findViewById(R.id.landline);
        email = findViewById(R.id.email);
        website = findViewById(R.id.website);
        addressLine1 = findViewById(R.id.address1);
        addressLine2 = findViewById(R.id.address2);
        pinCode = findViewById(R.id.pinCode);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        country = findViewById(R.id.country);
        tvEditDetails = findViewById(R.id.tvEditDetails);


        cbRetails = findViewById(R.id.cbRetails);
        cbWholeSale = findViewById(R.id.cbWholeSale);
        cbMobile = findViewById(R.id.cbMobile);
        cbLandLine = findViewById(R.id.cbLandLine);
        cbWebsite = findViewById(R.id.cbWebsite);
        cbEmail = findViewById(R.id.cbEmail);
        cbTerms = findViewById(R.id.cbTerms);

        tvInfo = findViewById(R.id.tvInfo);
        scrollView = findViewById(R.id.scrollView);

        //initialize views


        layoutViewCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewCats.getVisibility() == View.GONE) {
                    hideKeyboard(view);
                    cardViewCats.setVisibility(View.VISIBLE);
                    //cvBusiness.setBackgroundColor(Color.parseColor("#c1c1c1"));
                    cvBusiness.setCardBackgroundColor(Color.parseColor("#c1c1c1"));
                }
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewCats.getVisibility() == View.VISIBLE) {
                    cardViewCats.setVisibility(View.GONE);
                    cvBusiness.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });


        turnViewsOFF();
        tvEditDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSubmit.setEnabled(true);
                btnChooseImage.setEnabled(true);
                mMobile.setEnabled(true);
                companyName.setEnabled(true);
                firstName.setEnabled(true);
                lastName.setEnabled(true);
                landline.setEnabled(true);
                email.setEnabled(true);
                website.setEnabled(true);
                addressLine1.setEnabled(true);
                addressLine2.setEnabled(true);
                pinCode.setEnabled(true);
                city.setEnabled(true);
                state.setEnabled(true);
                country.setEnabled(true);

                cbWholeSale.setEnabled(true);
                cbRetails.setEnabled(true);
                cbMobile.setEnabled(true);
                cbLandLine.setEnabled(true);
                cbWebsite.setEnabled(true);
                cbEmail.setEnabled(true);
                cbTerms.setEnabled(true);

                businessDetails.setEnabled(true);
                layoutViewCats.setEnabled(true);
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
                showPictureDialog();
            }
        });

        cbWholeSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbWholeSale.isChecked()) {
                    scWholeSale = "true";
                    // Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scWholeSale = "false";
                    // Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbRetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbRetails.isChecked()) {
                    scRetails = "true";
                    // Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scRetails = "false";
                    //  Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbMobile.isChecked()) {
                    scMobile = "true";
                    //  Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scMobile = "false";
                    // Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbLandLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbLandLine.isChecked()) {
                    scLandLine = "true";
                    //  Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scLandLine = "false";
                    // Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbEmail.isChecked()) {
                    scEmail = "true";
                    //   Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scEmail = "false";
                    // Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cbWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbWebsite.isChecked()) {
                    scWebsite = "true";
                    // Toast.makeText(UpdateDetailsFragment.this, "true", Toast.LENGTH_SHORT).show();

                } else {
                    scWebsite = "false";
                    // Toast.makeText(UpdateDetailsFragment.this, "false", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                // startActivity(new Intent(UpdateDetailsFragment.this, VerifyPhoneActivity.class).putExtra("phone", mMobile.getEditText().getText().toString()));
//                Intent intent = new Intent(UpdateDetailsFragment.this, VerifyPhoneActivity.class);
//                intent.putExtra("phone", mMobile.getEditText().getText().toString());
//                startActivity(intent);

                registerBusiness();
            }
        });


        readApprovedBusinessFromDB();
        readCategoriesFromDB();
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            //  Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new AlertDialog.Builder(UpdateDetailsActivity.this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                                    choosePhotoFromGallery();
                                }
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void choosePhotoFromGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(this, "Error while creating file", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.social.businessdirectory.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
            if (bitmap.getWidth() > bitmap.getHeight()) {

                Matrix matrix = new Matrix();

                matrix.postRotate(90);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);

                Bitmap rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix, true);

                ivBusinessCard.setVisibility(View.VISIBLE);
                ivBusinessCard.setImageBitmap(rotatedBitmap);
                myImageUri = getImageUri(UpdateDetailsActivity.this, rotatedBitmap);
            } else {
                ivBusinessCard.setImageBitmap(bitmap);
                ivBusinessCard.setVisibility(View.VISIBLE);
                myImageUri = getImageUri(UpdateDetailsActivity.this, bitmap);
            }

        }

        if (requestCode == GALLERY && resultCode == RESULT_OK) {
            List<Bitmap> bitmapList = new ArrayList<>();
            ClipData clipData = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                clipData = data.getClipData();
            }
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    InputStream inputStream = null;
                    try {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            inputStream = getContentResolver().openInputStream(imageUri);
                        }
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        ivBusinessCard.setVisibility(View.VISIBLE);
                        ivBusinessCard.setImageBitmap(bitmap);
                        myImageUri = getImageUri(UpdateDetailsActivity.this, bitmap);

                        // bitmapList.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }

            } else {
                Uri imageUri = data.getData();
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                        if (imageUri != null) {
                            InputStream inputStream = getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            ivBusinessCard.setVisibility(View.VISIBLE);
                            ivBusinessCard.setImageBitmap(bitmap);

                            myImageUri = getImageUri(UpdateDetailsActivity.this, bitmap); // bitmapList.add(bitmap);

                        }
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);

        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, currentPhotoPath, null);
        return Uri.parse(path);
    }

    //TODO: Register Business
    public void registerBusiness() {
        String uniqueID = UUID.randomUUID().toString();
        if (companyName.getEditText().getText().toString().isEmpty()) {
            companyName.setError("Please Fill in Name");
            companyName.requestFocus();
        } else if (businessDetails.getText().toString().isEmpty()) {
            businessDetails.setError("Please Add Details");
            businessDetails.requestFocus();
        } else if (tvSelectedCats.getText().toString().isEmpty()) {
            tvSelectedCats.setError("Please Choose Category");
            Toast.makeText(UpdateDetailsActivity.this, "Please Choose Category", Toast.LENGTH_SHORT).show();

            tvSelectedCats.requestFocus();
        } else if (firstName.getEditText().getText().toString().isEmpty()) {
            firstName.setError("Please Fill in Name");
            firstName.requestFocus();
        } else if (lastName.getEditText().getText().toString().isEmpty()) {
            lastName.setError("Please Fill in Name");
            lastName.requestFocus();
        } else if (mMobile.getEditText().getText().toString().isEmpty()) {
            mMobile.setError("Please Fill in Name");
            mMobile.requestFocus();
        } else if (email.getEditText().getText().toString().isEmpty()) {
            email.setError("Please Fill in Name");
            email.requestFocus();
        } else if (!email.getEditText().getText().toString().trim().matches(emailPattern)) {
            email.setError("Please Fill in correct Email");
            email.requestFocus();
            Toast.makeText(UpdateDetailsActivity.this, "Please Fill in correct Email", Toast.LENGTH_SHORT).show();
        } else if (addressLine1.getEditText().getText().toString().isEmpty()) {
            addressLine1.setError("Please Fill in Name");
            addressLine1.requestFocus();
        } else if (pinCode.getEditText().getText().toString().isEmpty()) {
            pinCode.setError("Please Fill in Name");
            pinCode.requestFocus();
        } else if (city.getEditText().getText().toString().isEmpty()) {
            city.setError("Please Fill in Name");
            city.requestFocus();
        } else if (state.getEditText().getText().toString().isEmpty()) {
            state.setError("Please Fill in Name");
            state.requestFocus();
        } else if (country.getEditText().getText().toString().isEmpty()) {
            country.setError("Please Fill in Name");
            country.requestFocus();
        } else if (!cbTerms.isChecked()) {
            Toast.makeText(UpdateDetailsActivity.this, "Please Accept Terms", Toast.LENGTH_SHORT).show();
        } else if (!companyName.getEditText().getText().toString().isEmpty()
                && !firstName.getEditText().getText().toString().isEmpty()
                && !lastName.getEditText().getText().toString().isEmpty()
                && !businessDetails.getText().toString().isEmpty()
                && !tvSelectedCats.getText().toString().isEmpty()
                && !mMobile.getEditText().getText().toString().isEmpty()
                && !email.getEditText().getText().toString().isEmpty()
                && email.getEditText().getText().toString().trim().matches(emailPattern)
                && !addressLine1.getEditText().getText().toString().isEmpty()
                && !pinCode.getEditText().getText().toString().isEmpty()
                && !city.getEditText().getText().toString().isEmpty()
                && !state.getEditText().getText().toString().isEmpty()
                && !country.getEditText().getText().toString().isEmpty()
                && cbTerms.isChecked()
                && myImageUri != null) {

            btnSubmit.setEnabled(false);
            btnSubmit.setText("Adding...");

            final StorageReference image_ref = mStorage.child("business_images" + myImageUri.getLastPathSegment());
            image_ref.putFile(myImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    image_ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            imageUrl = uri.toString();
                            String uploadId = mDatabase.push().getKey();
                            Map<String, Object> dbMap = new HashMap<>();
                            dbMap.put("imageUrl", imageUrl);
                            mDatabase.child(spostID).updateChildren(dbMap);

                        }
                    });

                    mListener = mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Map<String, Object> dbMap = new HashMap<>();
                            /// dbMap.put("imageUrl", imageUrl);
                            // dbMap.put("ID", uniqueID);
                            dbMap.put("companyName", companyName.getEditText().getText().toString());
                            dbMap.put("firstName", firstName.getEditText().getText().toString());
                            dbMap.put("lastName", lastName.getEditText().getText().toString());
                            dbMap.put("mMobile", mMobile.getEditText().getText().toString());
                            dbMap.put("landline", landline.getEditText().getText().toString());
                            dbMap.put("email", email.getEditText().getText().toString());
                            dbMap.put("website", website.getEditText().getText().toString());
                            dbMap.put("addressLine1", addressLine1.getEditText().getText().toString());
                            dbMap.put("addressLine2", addressLine2.getEditText().getText().toString());
                            dbMap.put("pinCode", pinCode.getEditText().getText().toString());
                            dbMap.put("city", city.getEditText().getText().toString());
                            dbMap.put("state", state.getEditText().getText().toString());
                            dbMap.put("country", country.getEditText().getText().toString());
                            dbMap.put("cWholeSale", scWholeSale);
                            dbMap.put("cRetails", scRetails);
                            dbMap.put("cMobile", scMobile);
                            dbMap.put("cLandLine", scLandLine);
                            dbMap.put("cEmail", scEmail);
                            dbMap.put("cWebsite", scWebsite);
                            // dbMap.put("status", "0");
                            dbMap.put("categories", tvSelectedCats.getText().toString());
                            dbMap.put("businessDetails", businessDetails.getText().toString());

                            //dbMap.put("postID", uploadId);
                            mDatabase.child(spostID).updateChildren(dbMap);
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    btnSubmit.setText("Submit");
                                    btnSubmit.setEnabled(true);
                                    myImageUri = null;
                                    //ivBusinessCard.setImageResource(R.drawable.ic_search);

                                    final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(UpdateDetailsActivity.this);
                                    LayoutInflater layoutInflater1 = LayoutInflater.from(UpdateDetailsActivity.this);
                                    View view1 = layoutInflater1.inflate(R.layout.layout_dialog, null);
                                    btnClose = view1.findViewById(R.id.tvClose);
                                    mBuilder.setView(view1);
                                    mBuilder.setCancelable(false);

                                    final AlertDialog dialog = mBuilder.create();
                                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_error_layout);
                                    dialog.setView(view1, 0, 0, 0, 0);


                                    dialog.show();
                                    btnClose.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            mBuilder.setCancelable(true);
                                            dialog.cancel();
                                            dialog.dismiss();
                                            turnViewsOFF();
                                        }
                                    });


                                }
                            }, 1000);


                        }

                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            progressDialog.dismiss();
                            Log.w("TAG", "Failed to read value.", error.toException());
                            Toast.makeText(UpdateDetailsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                        }
                    });

                }


            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UpdateDetailsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    btnSubmit.setEnabled(true);
                }
            });

        } else if (!companyName.getEditText().getText().toString().isEmpty()
                && !firstName.getEditText().getText().toString().isEmpty()
                && !lastName.getEditText().getText().toString().isEmpty()
                && !businessDetails.getText().toString().isEmpty()
                && !tvSelectedCats.getText().toString().isEmpty()
                && !mMobile.getEditText().getText().toString().isEmpty()
                && !email.getEditText().getText().toString().isEmpty()
                && email.getEditText().getText().toString().trim().matches(emailPattern)
                && !addressLine1.getEditText().getText().toString().isEmpty()
                && !pinCode.getEditText().getText().toString().isEmpty()
                && !city.getEditText().getText().toString().isEmpty()
                && !state.getEditText().getText().toString().isEmpty()
                && !country.getEditText().getText().toString().isEmpty()
                && cbTerms.isChecked()
        ) {

            btnSubmit.setEnabled(false);
            btnSubmit.setText("Adding...");

            mListener = mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Map<String, Object> dbMap = new HashMap<>();
                    /// dbMap.put("imageUrl", imageUrl);
                    // dbMap.put("ID", uniqueID);
                    dbMap.put("companyName", companyName.getEditText().getText().toString());
                    dbMap.put("firstName", firstName.getEditText().getText().toString());
                    dbMap.put("lastName", lastName.getEditText().getText().toString());
                    dbMap.put("mMobile", mMobile.getEditText().getText().toString());
                    dbMap.put("landline", landline.getEditText().getText().toString());
                    dbMap.put("email", email.getEditText().getText().toString());
                    dbMap.put("website", website.getEditText().getText().toString());
                    dbMap.put("addressLine1", addressLine1.getEditText().getText().toString());
                    dbMap.put("addressLine2", addressLine2.getEditText().getText().toString());
                    dbMap.put("pinCode", pinCode.getEditText().getText().toString());
                    dbMap.put("city", city.getEditText().getText().toString());
                    dbMap.put("state", state.getEditText().getText().toString());
                    dbMap.put("country", country.getEditText().getText().toString());
                    dbMap.put("cWholeSale", scWholeSale);
                    dbMap.put("cRetails", scRetails);
                    dbMap.put("cMobile", scMobile);
                    dbMap.put("cLandLine", scLandLine);
                    dbMap.put("cEmail", scEmail);
                    dbMap.put("cWebsite", scWebsite);
                    dbMap.put("categories", tvSelectedCats.getText().toString());
                    dbMap.put("businessDetails", businessDetails.getText().toString());

                    //dbMap.put("status", "0");
                    //dbMap.put("postID", uploadId);
                    mDatabase.child(spostID).updateChildren(dbMap);
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            btnSubmit.setText("Submit");
                            btnSubmit.setEnabled(true);
                            myImageUri = null;
                            ivBusinessCard.setImageResource(R.drawable.ic_search);
                            turnViewsOFF();

//                            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(UpdateDetailsFragment.this);
//                            LayoutInflater layoutInflater1 = LayoutInflater.from(UpdateDetailsFragment.this);
//                            View view1 = layoutInflater1.inflate(R.layout.layout_dialog, null);
//                            btnClose = view1.findViewById(R.id.tvClose);
//                            mBuilder.setView(view1);
//                            mBuilder.setCancelable(false);
//
//                            final AlertDialog dialog = mBuilder.create();
//                            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_error_layout);
//                            dialog.setView(view1, 0, 0, 0, 0);
//
//
//                            dialog.show();
//                            btnClose.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mBuilder.setCancelable(true);
//                                    dialog.cancel();
//                                    dialog.dismiss();
//
//                                    turnViewsOFF();
//                                }
//                            });


                        }
                    }, 1000);


                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    progressDialog.dismiss();
                    Log.w("TAG", "Failed to read value.", error.toException());
                    Toast.makeText(UpdateDetailsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
                }
            });

        }

    }


    private void readApprovedBusinessFromDB() {
        progressDialog.show();
        // Read from the database
        mListener = mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                companyModelList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("status").getValue(String.class).equals("1") && ds.child("ID").getValue(String.class).equalsIgnoreCase(preferences.getString("uniqueId", ""))) {
                        scompanyName = ds.child("companyName").getValue(String.class);
                        sfirstName = ds.child("firstName").getValue(String.class);
                        slastName = ds.child("lastName").getValue(String.class);
                        smobile = ds.child("mMobile").getValue(String.class);
                        slandline = ds.child("landline").getValue(String.class);
                        semail = ds.child("email").getValue(String.class);
                        swebsite = ds.child("website").getValue(String.class);
                        sadress1 = ds.child("addressLine1").getValue(String.class);
                        sadress2 = ds.child("addressLine2").getValue(String.class);
                        spincode = ds.child("pinCode").getValue(String.class);
                        scity = ds.child("city").getValue(String.class);
                        sstate = ds.child("state").getValue(String.class);
                        scountry = ds.child("country").getValue(String.class);
                        sbusinessCard = ds.child("imageUrl").getValue(String.class);
                        scWholeSale = ds.child("cWholeSale").getValue(String.class);
                        scRetails = ds.child("cRetails").getValue(String.class);
                        scMobile = ds.child("cMobile").getValue(String.class);
                        scWebsite = ds.child("cWebsite").getValue(String.class);
                        scEmail = ds.child("cEmail").getValue(String.class);
                        scLandLine = ds.child("cLandLine").getValue(String.class);
                        //sstatus = ds.child("status").getValue(String.class);
                        spostID = ds.child("postID").getValue(String.class);
                        sCategory = ds.child("categories").getValue(String.class);
                        sBusinessDetails = ds.child("businessDetails").getValue(String.class);


                        ivBusinessCard.setVisibility(View.VISIBLE);
                        Glide.with(getApplicationContext()).load(sbusinessCard)
                                .centerCrop()
                                .placeholder(R.drawable.ic_search)
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        return false;
                                    }
                                }).into(ivBusinessCard);
                        companyName.getEditText().setText(scompanyName);
                        firstName.getEditText().setText(sfirstName);
                        lastName.getEditText().setText(slastName);
                        mMobile.getEditText().setText(smobile);
                        landline.getEditText().setText(slandline);
                        email.getEditText().setText(semail);
                        website.getEditText().setText(swebsite);
                        addressLine1.getEditText().setText(sadress1);
                        addressLine2.getEditText().setText(sadress2);
                        pinCode.getEditText().setText(spincode);
                        city.getEditText().setText(scity);
                        state.getEditText().setText(sstate);
                        country.getEditText().setText(scountry);
                        tvSelectedCats.setText(sCategory);
                        businessDetails.setText(sBusinessDetails);


                        cbWholeSale.setChecked(scWholeSale.equalsIgnoreCase("true"));
                        cbRetails.setChecked(scRetails.equalsIgnoreCase("true"));
                        cbMobile.setChecked(scMobile.equalsIgnoreCase("true"));
                        cbLandLine.setChecked(scLandLine.equalsIgnoreCase("true"));
                        cbWebsite.setChecked(scWebsite.equalsIgnoreCase("true"));
                        cbEmail.setChecked(scEmail.equalsIgnoreCase("true"));


                        //  String parts = category.replace(",", "\n");
                        progressDialog.hide();
                        scrollView.setVisibility(View.VISIBLE);
                        tvInfo.setVisibility(View.GONE);
//                        companyModelList.add(new CompanyModel(spostID, scompanyName, sfirstName, slastName, smobile, slandline, semail, swebsite, sbusinessCard,
//                                sadress1, sadress2, spincode, scity, sstate, scountry, sstatus, scMobile, scLandLine, scEmail, scWebsite,
//                                scWholeSale, scRetails));
                    } else {
                        //Toast.makeText(getApplicationContext(), "You don't have any registered Business", Toast.LENGTH_SHORT).show();
                        scrollView.setVisibility(View.GONE);
                        tvInfo.setVisibility(View.VISIBLE);
                    }

                    progressDialog.hide();
                }
                // adapterListing.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(UpdateDetailsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            inputMethodManager = (InputMethodManager) Objects.requireNonNull(getApplicationContext()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void readCategoriesFromDB() {
        //  progressDialog.show();
        // Read from the database
        usersArray = new ArrayList<>();
        mListenerCats = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
//                categoriesList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    catName = ds.child("catName").getValue(String.class);
                    catImage = ds.child("imageUrl").getValue(String.class);


                    //  String parts = category.replace(",", "\n");
                    //  progressDialog.hide();
                    // categoriesList.add(new Categories(catName, catImage));
                    usersArray.add(catName);
                }
                // adapterCats.notifyDataSetChanged();
                listVOs = new ArrayList<>();
                for (int i = 0; i < usersArray.size(); i++) {
                    StateVO stateVO = new StateVO();
                    stateVO.setTitle(usersArray.get(i));
                    stateVO.setSelected(false);
                    listVOs.add(stateVO);
                }
                adapterCats = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_multiple_choice, usersArray);

                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                listView.setAdapter(adapterCats);
                listView.setItemsCanFocus(false);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        SparseBooleanArray checked = listView.getCheckedItemPositions();


                        selectedItems = new ArrayList<String>();

                        for (int ii = 0; ii < checked.size(); ii++) {
                            // Item position in adapter
                            int position = checked.keyAt(ii);
                            // Add sport if it is checked i.e.) == TRUE!
                            if (checked.valueAt(ii)) {
                                selectedItems.add(adapterCats.getItem(position));

                                //  Toast.makeText(getActivity(), "" + selectedItemsID.toString(), Toast.LENGTH_SHORT).show();
                            }

                        }
                        outputStrArr = new String[selectedItems.size()];

                        for (int iii = 0; iii < selectedItems.size(); iii++) {
                            outputStrArr[iii] = selectedItems.get(iii);

                        }
                        String parts = selectedItems.toString().replace("[", "");
                        String partsA = parts.toString().replace("]", "");
                        tvSelectedCats.setText(partsA.toString());
                        editor.putString("set", selectedItems.toString());
                        editor.commit();
                        editor.apply();
                    }
                });


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

    public void turnViewsOFF() {
        btnSubmit.setEnabled(false);
        btnChooseImage.setEnabled(false);
        mMobile.setEnabled(false);
        companyName.setEnabled(false);
        firstName.setEnabled(false);
        lastName.setEnabled(false);
        landline.setEnabled(false);
        email.setEnabled(false);
        website.setEnabled(false);
        addressLine1.setEnabled(false);
        addressLine2.setEnabled(false);
        pinCode.setEnabled(false);
        city.setEnabled(false);
        state.setEnabled(false);
        country.setEnabled(false);

        cbWholeSale.setEnabled(false);
        cbRetails.setEnabled(false);
        cbMobile.setEnabled(false);
        cbLandLine.setEnabled(false);
        cbWebsite.setEnabled(false);
        cbEmail.setEnabled(false);
        cbTerms.setEnabled(false);

        businessDetails.setEnabled(false);
        layoutViewCats.setEnabled(false);

    }
}