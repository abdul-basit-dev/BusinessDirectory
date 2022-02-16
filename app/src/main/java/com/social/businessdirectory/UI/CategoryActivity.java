package com.social.businessdirectory.UI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.social.businessdirectory.Adapters.MyAdapterCategory;
import com.social.businessdirectory.Model.Categories;
import com.social.businessdirectory.R;

import java.util.ArrayList;
import java.util.List;


public class CategoryActivity extends AppCompatActivity implements MyAdapterCategory.OnItemClickListener {


    //recyclerview object
    private RecyclerView recyclerView;
    private MyAdapterCategory adapter;
    private List<Categories> categoriesList;

    private ValueEventListener mListener;
    ProgressDialog progressDialog;

    String catName, catImage;


    //FireBase
    FirebaseFirestore mFireStore;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;

    public CategoryActivity() {
        // Required empty public constructor
    }




    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView textView;
        ivBack = toolbar.findViewById(R.id.ivBack);
        textView = toolbar.findViewById(R.id.tvTitle);
        textView.setText("CATEGORIES");
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
        //dbRef = FirebaseDatabase.getInstance().getReference("business_app:").child("business");
        mStorage = FirebaseStorage.getInstance().getReference().child("categories_images");
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setTitle("Please wait...");

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //  recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        categoriesList = new ArrayList<Categories>();


        adapter = new MyAdapterCategory(this, categoriesList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(CategoryActivity.this);
        readCategoriesFromDB();


    }

    @Override
    public void onItemClick(int position) {

        Intent intent = new Intent(CategoryActivity.this, CategoriesReslutsActivity.class);
        intent.putExtra("cat",categoriesList.get(position).getCategoryName());
        startActivity(intent);
    }

    private void readCategoriesFromDB() {
        progressDialog.show();
        // Read from the database
        mListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                categoriesList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    catName = ds.child("catName").getValue(String.class);
                    catImage = ds.child("imageUrl").getValue(String.class);


                    //  String parts = category.replace(",", "\n");
                    progressDialog.hide();
                    categoriesList.add(new Categories(catName, catImage));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(CategoryActivity.this, "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}