package com.social.businessdirectory.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.social.businessdirectory.Adapters.MyAdapterListing;
import com.social.businessdirectory.Model.CompanyModel;
import com.social.businessdirectory.Model.StateVO;
import com.social.businessdirectory.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements MyAdapterListing.OnItemClickListener {

    EditText editTextSearch;
    String catName, catImage;
    ArrayAdapter<String> adapterCats;
    ArrayList<String> usersArray;
    ArrayList<StateVO> listVOs;
    ArrayList<String> selectedItems;
    ListView listView;
    String[] outputStrArr;
    private ValueEventListener mListener;
    private DatabaseReference mDatabaseRef, dbRef;
    SharedPreferences.Editor editor;
    SharedPreferences preferences;
    TextView tvSelectedCats;
    LinearLayout layoutViewCats;
    CardView cardViewCats, cvBusiness;
    Button btnSave, btnSearch;
    private MyAdapterListing adapterListing;

    private List<CompanyModel> companyModelList;
    String companyName, firstName, lastName, mobile, landline, email, website, businessCard, adress1, adress2, pincode;
    String city, state, country, status, postID;
    String cMobile, cLandLine, cEmail, cWebsite, cWholeSale, cRetails, cTerms;
    String businessDescription, categories;
    TextView textViewCount;
    private RecyclerView recyclerViewListing;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView textView;
        ivBack = toolbar.findViewById(R.id.ivBack);
        textView = toolbar.findViewById(R.id.tvTitle);
        textView.setText("SEARCH");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //setting up toolbar Finished...
        dbRef = FirebaseDatabase.getInstance().getReference("business_app:").child("business");

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("business_app:").child("categories");
        preferences = getSharedPreferences("MyPref", 0);
        editor = preferences.edit();
        usersArray = new ArrayList<>();
        tvSelectedCats = findViewById(R.id.tvSelectedCats);
        listView = findViewById(R.id.listview);
        layoutViewCats = findViewById(R.id.layoutViewCats);
        cardViewCats = findViewById(R.id.cardViewCats);
        cvBusiness = findViewById(R.id.cvBusiness);
        editTextSearch = findViewById(R.id.edSearch);
        btnSave = findViewById(R.id.btnSave);
        btnSearch = findViewById(R.id.btnSearch);
        editTextSearch.requestFocus();
        textViewCount = findViewById(R.id.tvCount);

        recyclerViewListing = (RecyclerView) findViewById(R.id.recyclerViewListing);
        textViewCount = findViewById(R.id.tvCount);
        recyclerViewListing.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerViewListing.setHasFixedSize(true);
        recyclerViewListing.setItemAnimator(new DefaultItemAnimator());
        companyModelList = new ArrayList<CompanyModel>();

        adapterListing = new MyAdapterListing(this, companyModelList);
        recyclerViewListing.setAdapter(adapterListing);
        adapterListing.setOnItemClickListener(SearchActivity.this);


        editTextSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (i2 > 0) {
                    editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                } else {
                    editTextSearch.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_search, 0);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        layoutViewCats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewCats.getVisibility() == View.GONE) {
                    hideKeyboard(view);
                    cardViewCats.setVisibility(View.VISIBLE);
                    //cvBusiness.setBackgroundColor(Color.parseColor("#c1c1c1"));
                    // cvBusiness.setCardBackgroundColor(Color.parseColor("#c1c1c1"));
                }
            }
        });

        readCategoriesFromDB();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cardViewCats.getVisibility() == View.VISIBLE) {
                    cardViewCats.setVisibility(View.GONE);
                    // cvBusiness.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchFromDB();
                hideKeyboard(view);
            }
        });
    }

    private void searchFromDB() {
        if (editTextSearch.getText().toString().isEmpty()) {
            editTextSearch.setError("Please enter here");
            editTextSearch.requestFocus();
        } else if (tvSelectedCats.getText().toString().isEmpty()) {
            tvSelectedCats.setError("Please Choose Category");
            tvSelectedCats.requestFocus();
        } else if (!tvSelectedCats.getText().toString().isEmpty()
                && !editTextSearch.getText().toString().isEmpty()) {
            // Toast.makeText(SearchActivity.this, "All Set", Toast.LENGTH_SHORT).show();
            readApprovedBusinessFromDB();
        }

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
                            && (ds.child("categories").getValue(String.class).contains(tvSelectedCats.getText().toString()))
                            && (ds.child("businessDetails").getValue(String.class).toLowerCase().contains(editTextSearch.getText().toString().toLowerCase()))) {
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
                Toast.makeText(SearchActivity.this, "" + error, Toast.LENGTH_SHORT).show();
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
        mListener = mDatabaseRef.addValueEventListener(new ValueEventListener() {
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

                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
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
                        tvSelectedCats.setAllCaps(true);
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

    @Override
    public void onListingItemClick(int position) {
        Intent intent = new Intent(SearchActivity.this, ListingDetailsActivity.class);

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
}