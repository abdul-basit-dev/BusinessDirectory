package com.social.businessdirectory.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.social.businessdirectory.Adapters.MyAdapterCategory;
import com.social.businessdirectory.Adapters.MyAdapterListing;
import com.social.businessdirectory.Count;
import com.social.businessdirectory.UI.CategoriesReslutsActivity;
import com.social.businessdirectory.UI.CategoryActivity;
import com.social.businessdirectory.UI.ListingDetailsActivity;
import com.social.businessdirectory.Model.Categories;
import com.social.businessdirectory.Model.CompanyModel;
import com.social.businessdirectory.R;
import com.social.businessdirectory.UI.SearchActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment implements MyAdapterCategory.OnItemClickListener, MyAdapterListing.OnItemClickListener {

    //recyclerview object
    private RecyclerView recyclerView, recyclerViewListing, recyclerViewListingDetails;
    private LinearLayoutManager mLayoutManager;
    private MyAdapterCategory adapter;
    private List<Categories> categoriesList;

    private MyAdapterListing adapterListing;
    private List<CompanyModel> companyModelList;

    TextView tvAllCategories, tvListingAll, tvCloseCardView;
    CardView mCardViewListingDetails;

    //FireBase
    FirebaseFirestore mFireStore;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef, dbRef;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    private ValueEventListener mListener;
    ProgressDialog progressDialog;

    String catName, catImage;

    String companyName, firstName, lastName, mobile, landline, email, website, businessCard, adress1, adress2, pincode;
    String city, state, country, status, postID;
    String cMobile, cLandLine, cEmail, cWebsite, cWholeSale, cRetails, cTerms;
    String businessDescription, categories;

    TextView edSearch;
    ImageButton btnSearch;
    RelativeLayout layoutSearch;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        FirebaseApp.initializeApp(getActivity());

        mFireStore = FirebaseFirestore.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business_app:").child("categories");
        dbRef = FirebaseDatabase.getInstance().getReference("business_app:").child("business");
        mStorage = FirebaseStorage.getInstance().getReference().child("categories_images");
        preferences = getActivity().getSharedPreferences("MyPref", 0);
        editor = preferences.edit();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.setTitle("Please wait...");


        tvAllCategories = rootView.findViewById(R.id.tvAllCategories);
        edSearch = rootView.findViewById(R.id.edSearch);
        btnSearch = rootView.findViewById(R.id.btnSearch);
        layoutSearch = rootView.findViewById(R.id.layoutSearch);
        tvCloseCardView = rootView.findViewById(R.id.tvCloseCardView);
        tvListingAll = rootView.findViewById(R.id.tvListingAll);
        mCardViewListingDetails = rootView.findViewById(R.id.mCardViewListingDetails);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        categoriesList = new ArrayList<Categories>();


        adapter = new MyAdapterCategory(getActivity(), categoriesList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(HomeFragment.this);


        recyclerViewListing = (RecyclerView) rootView.findViewById(R.id.recyclerViewListing);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewListing.setHasFixedSize(true);
        recyclerViewListing.setItemAnimator(new DefaultItemAnimator());
        companyModelList = new ArrayList<CompanyModel>();

        adapterListing = new MyAdapterListing(getActivity(), companyModelList);
        recyclerViewListing.setAdapter(adapterListing);
        adapterListing.setOnItemClickListener(HomeFragment.this);


        recyclerViewListingDetails = (RecyclerView) rootView.findViewById(R.id.recyclerViewListingDetails);
        recyclerViewListingDetails.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewListingDetails.setHasFixedSize(true);
        recyclerViewListingDetails.setItemAnimator(new DefaultItemAnimator());
        recyclerViewListingDetails.setAdapter(adapterListing);


        tvAllCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });

        tvListingAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CategoryActivity.class);
                startActivity(intent);
            }
        });
        tvCloseCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCardViewListingDetails.getVisibility() == View.VISIBLE) {
                    mCardViewListingDetails.setVisibility(View.GONE);
                }
            }
        });
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/app_users").addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();
            }
        });
        readCategoriesFromDB();
        readApprovedBusinessFromDB();

//        edSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    hideKeyboard(v);
//                    return true;
//                }
//                return false;
//            }
//        });
//        edSearch.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                if (i2 > 0) {
//                    btnSearch.setImageResource(R.drawable.ic_clear);
//                } else {
//                    btnSearch.setImageResource(R.drawable.ic_search);
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (edSearch.getText().toString().trim().length() > 1) {
//                    edSearch.setText("");
//                }

                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);

            }
        });
        edSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });
        layoutSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                startActivity(intent);
            }
        });


        return rootView;
    }

    private void performSearch() {

    }

    @Override
    public void onItemClick(int position) {
        // Toast.makeText(getActivity(), "Position:"+position, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), CategoriesReslutsActivity.class);
        intent.putExtra("cat", categoriesList.get(position).getCategoryName());
        startActivity(intent);
    }

    @Override
    public void onListingItemClick(int position) {

        Intent intent = new Intent(getActivity(), ListingDetailsActivity.class);

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

    private void readCategoriesFromDB() {
        //   progressDialog.show();
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
                    // progressDialog.hide();
                    categoriesList.add(new Categories(catName, catImage));
                }
                //   progressDialog.hide();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                // progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void readApprovedBusinessFromDB() {
        progressDialog.show();
        // Read from the database
        mListener = dbRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                companyModelList.clear();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (ds.child("status").getValue(String.class).equals("1")) {
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
                        progressDialog.dismiss();
                        companyModelList.add(new CompanyModel(categories, businessDescription, postID, companyName, firstName, lastName, mobile, landline, email, website, businessCard,
                                adress1, adress2, pincode, city, state, country, status, cMobile, cLandLine, cEmail, cWebsite,
                                cWholeSale, cRetails));
                        Collections.reverse(companyModelList);
                    }

                    progressDialog.dismiss();
                }
                adapterListing.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //   progressDialog.dismiss();
                Log.w("TAG", "Failed to read value.", error.toException());
                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            inputMethodManager = (InputMethodManager) Objects.requireNonNull(getActivity()).getSystemService(Activity.INPUT_METHOD_SERVICE);
        }
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}