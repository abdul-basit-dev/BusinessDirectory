package com.social.businessdirectory.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.social.businessdirectory.Adapters.MyAdapterListing;
import com.social.businessdirectory.Model.CompanyModel;
import com.social.businessdirectory.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CategoriesReslutsActivity extends AppCompatActivity implements MyAdapterListing.OnItemClickListener {
    private RecyclerView recyclerViewListing;
    private MyAdapterListing adapterListing;

    private List<CompanyModel> companyModelList;
    String companyName, firstName, lastName, mobile, landline, email, website, businessCard, adress1, adress2, pincode;
    String city, state, country, status, postID;
    String cMobile, cLandLine, cEmail, cWebsite, cWholeSale, cRetails, cTerms;
    String businessDescription, categories;


    //FireBase
    FirebaseFirestore mFireStore;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef, dbRef;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    private ValueEventListener mListener;

    TextView textViewCount;


    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_resluts);


        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView textView;
        ivBack = toolbar.findViewById(R.id.ivBack);
        textView = toolbar.findViewById(R.id.tvTitle);
        textView.setText(getIntent().getStringExtra("cat"));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //setting up toolbar Finished...


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mFireStore = FirebaseFirestore.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business_app:").child("categories");
        dbRef = FirebaseDatabase.getInstance().getReference("business_app:").child("business");
        mStorage = FirebaseStorage.getInstance().getReference().child("categories_images");
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        recyclerViewListing = (RecyclerView) findViewById(R.id.recyclerViewListing);
        textViewCount = findViewById(R.id.tvCount);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewListing.setHasFixedSize(true);
        recyclerViewListing.setItemAnimator(new DefaultItemAnimator());
        companyModelList = new ArrayList<CompanyModel>();

        adapterListing = new MyAdapterListing(this, companyModelList);
        recyclerViewListing.setAdapter(adapterListing);
        adapterListing.setOnItemClickListener(CategoriesReslutsActivity.this);

        readApprovedBusinessFromDB();

    }

    @Override
    public void onListingItemClick(int position) {

        Intent intent = new Intent(CategoriesReslutsActivity.this, ListingDetailsActivity.class);

        intent.putExtra("imageURL", companyModelList.get(position).getBusinessCard());
        intent.putExtra("companyName", companyModelList.get(position).getCompanyName());
        intent.putExtra("firstName", companyModelList.get(position).getFirstName());
        intent.putExtra("lastName", companyModelList.get(position).getLastName());
        intent.putExtra("mobile", companyModelList.get(position).getMobile());
        intent.putExtra("landline", companyModelList.get(position).getLandline());
        intent.putExtra("website", companyModelList.get(position).getWebsite());
        intent.putExtra("email", companyModelList.get(position).getEmail());
        intent.putExtra("addressLine1", companyModelList.get(position).getAdress1());
        intent.putExtra("addressLine2", companyModelList.get(position).getAdress2());
        intent.putExtra("pincode", companyModelList.get(position).getPincode());
        intent.putExtra("city", companyModelList.get(position).getCity());
        intent.putExtra("state", companyModelList.get(position).getState());
        intent.putExtra("country", companyModelList.get(position).getCountry());
        intent.putExtra("cWholeSale", companyModelList.get(position).getcWholeSale());
        intent.putExtra("cRetails", companyModelList.get(position).getcRetails());
        intent.putExtra("cMobile", companyModelList.get(position).getcMobile());
        intent.putExtra("cLandline", companyModelList.get(position).getcLandLine());
        intent.putExtra("cEmail", companyModelList.get(position).getcEmail());
        intent.putExtra("cWebsite", companyModelList.get(position).getcWebsite());
        intent.putExtra("status", companyModelList.get(position).getStatus());
        intent.putExtra("postID", companyModelList.get(position).getPostID());
        intent.putExtra("categories", companyModelList.get(position).getCategoryName());
        intent.putExtra("businessDetails", companyModelList.get(position).getBusinessDisc());
        startActivity(intent);

    }

    private void readApprovedBusinessFromDB() {
        // progressDialog.show();
        // Read from the database
        mListener = dbRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                companyModelList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("status").getValue(String.class).equals("1")
                            && ds.child("categories").getValue(String.class).contains(getIntent().getStringExtra("cat"))) {
                        companyName = ds.child("companyName").getValue(String.class);
                        firstName = ds.child("firstName").getValue(String.class);
                        lastName = ds.child("lastName").getValue(String.class);
                        mobile = ds.child("mMobile").getValue(String.class);
                        landline = ds.child("landline").getValue(String.class);
                        email = ds.child("email").getValue(String.class);
                        website = ds.child("website").getValue(String.class);
                        adress1 = ds.child("addressLine1").getValue(String.class);
                        adress2 = ds.child("addressLine2").getValue(String.class);
                        pincode = ds.child("pinCode").getValue(String.class);
                        city = ds.child("city").getValue(String.class);
                        state = ds.child("state").getValue(String.class);
                        country = ds.child("country").getValue(String.class);
                        businessCard = ds.child("imageUrl").getValue(String.class);
                        cWholeSale = ds.child("cWholeSale").getValue(String.class);
                        cRetails = ds.child("cRetails").getValue(String.class);
                        cMobile = ds.child("cMobile").getValue(String.class);
                        cWebsite = ds.child("cWebsite").getValue(String.class);
                        cEmail = ds.child("cEmail").getValue(String.class);
                        cLandLine = ds.child("cLandLine").getValue(String.class);
                        status = ds.child("status").getValue(String.class);
                        postID = ds.child("postID").getValue(String.class);
                        categories = ds.child("categories").getValue(String.class);
                        businessDescription = ds.child("businessDetails").getValue(String.class);
                        //  String parts = category.replace(",", "\n");
                        //progressDialog.hide();

                        companyModelList.add(new CompanyModel(categories, businessDescription, postID, companyName, firstName, lastName, mobile, landline, email, website, businessCard,
                                adress1, adress2, pincode, city, state, country, status, cMobile, cLandLine, cEmail, cWebsite,
                                cWholeSale, cRetails));
                        Collections.reverse(companyModelList);
                    }

                    //progressDialog.hide();
                }
                adapterListing.notifyDataSetChanged();
                textViewCount.setText(adapterListing.getItemCount() + " " + "RESULTS FOUND");
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //   progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(CategoriesReslutsActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}