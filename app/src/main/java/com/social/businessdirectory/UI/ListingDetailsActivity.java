package com.social.businessdirectory.UI;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.FirebaseDatabase;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.social.businessdirectory.R;

import java.util.List;

public class ListingDetailsActivity extends AppCompatActivity {
    ImageView ivBusinessCard;
    TextView tvCompanyName, tvFirstNLastname, tvMobile, tvLandline, tvEmail, tvWebsite;
    TextView tvAddressLine1, tvaddressLine2, tvPincode, tvCity, tvState, tvCountry, tvBusinessDetails;
    CheckBox cbMobile, cbLandLine, cbEmail, cbWebsite, cbWholeSale, cbRetails, cbTerms;
    LinearLayout layoutMobile, layoutEmail, layoutWebsite, layoutLandline;
    LinearLayout mlayoutMobile, mlayoutEmail, mlayoutWebsite, mlayoutLandline;

    Button btnReject, btnApprove;
    FirebaseDatabase database;
    private AdView mAdView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listing_details);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView tvTitle;
        ivBack = toolbar.findViewById(R.id.ivBack);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        tvTitle.setText("Business Details");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //setting up toolbar Finished...


        ivBusinessCard = findViewById(R.id.ivBusinessCard);
        tvBusinessDetails = findViewById(R.id.tvBusinessDetails);
        tvCompanyName = findViewById(R.id.tvCompanyName);
        tvFirstNLastname = findViewById(R.id.userName);
        tvAddressLine1 = findViewById(R.id.tvAddressLine1);
//        tvaddressLine2 = findViewById(R.id.tvAddressLine2);
//        tvPincode = findViewById(R.id.pincode);
//        tvCity = findViewById(R.id.tvcity);
//        tvState = findViewById(R.id.tvState);
//        tvCountry = findViewById(R.id.country);

        tvMobile = findViewById(R.id.tvMobile);
        tvLandline = findViewById(R.id.tvLandline);
        tvWebsite = findViewById(R.id.tvWebsite);
        tvEmail = findViewById(R.id.tvEmail);

        layoutMobile = findViewById(R.id.layoutMobile);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutLandline = findViewById(R.id.layoutLandLine);
        layoutWebsite = findViewById(R.id.layoutWebsite);


        mlayoutMobile = findViewById(R.id.mLayoutMobile);
        mlayoutEmail = findViewById(R.id.mLayoutEmail);
        mlayoutLandline = findViewById(R.id.mLayoutLandLine);
        mlayoutWebsite = findViewById(R.id.mLayoutWebsite);


        cbRetails = findViewById(R.id.cbRetails);
        cbWholeSale = findViewById(R.id.cbWholeSale);
        cbMobile = findViewById(R.id.cbMobile);
        cbLandLine = findViewById(R.id.cbLandLine);
        cbWebsite = findViewById(R.id.cbWebsite);
        cbEmail = findViewById(R.id.cbEmail);


        Glide.with(this).load(getIntent().getStringExtra("imageURL"))
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

        tvCompanyName.setText(getIntent().getStringExtra("companyName").toString());
        tvFirstNLastname.setText(getIntent().getStringExtra("firstName").toString() + " " + getIntent().getStringExtra("lastName").toString());
        tvMobile.setText(getIntent().getStringExtra("mobile").toString());
        tvLandline.setText(getIntent().getStringExtra("landline").toString());
        tvWebsite.setText(getIntent().getStringExtra("website").toString());
        tvEmail.setText(getIntent().getStringExtra("email").toString());
        tvAddressLine1.setText(getIntent().getStringExtra("pincode").toString() + "--" + getIntent().getStringExtra("addressLine1").toString()
                + ", " + getIntent().getStringExtra("addressLine2").toString()
                + ", " + getIntent().getStringExtra("city").toString()
                + ", " + getIntent().getStringExtra("state").toString()
                + ", " + getIntent().getStringExtra("country").toString());


//        tvaddressLine2.setText(getIntent().getStringExtra("addressLine2").toString());
//        tvPincode.setText(getIntent().getStringExtra("pincode").toString());
//        tvCity.setText(getIntent().getStringExtra("city").toString());
//        tvState.setText(getIntent().getStringExtra("state").toString());
//        tvCountry.setText(getIntent().getStringExtra("country").toString());

        tvBusinessDetails.setText(getIntent().getStringExtra("businessDetails").toString());


        // Toast.makeText(this, "value:" + getIntent().getStringExtra("status"), Toast.LENGTH_SHORT).show();
        cbWholeSale.setChecked(getIntent().getStringExtra("cWholeSale").equalsIgnoreCase("true"));
        cbRetails.setChecked(getIntent().getStringExtra("cRetails").equalsIgnoreCase("true"));
        cbMobile.setChecked(getIntent().getStringExtra("cMobile").equalsIgnoreCase("true"));
        cbLandLine.setChecked(getIntent().getStringExtra("cLandline").equalsIgnoreCase("true"));
        cbWebsite.setChecked(getIntent().getStringExtra("cWebsite").equalsIgnoreCase("true"));
        cbEmail.setChecked(getIntent().getStringExtra("cEmail").equalsIgnoreCase("true"));


        if (!cbMobile.isChecked()) {
            layoutMobile.setEnabled(false);
            layoutMobile.setAlpha(0.5F);
            mlayoutMobile.setAlpha(0.5F);
            tvMobile.setText("N/A");


        }
        if (!cbWebsite.isChecked()) {
            layoutWebsite.setEnabled(false);
            layoutWebsite.setAlpha(0.5F);
            mlayoutWebsite.setAlpha(0.5F);
            tvWebsite.setText("N/A");
        }
        if (!cbEmail.isChecked()) {
            layoutEmail.setEnabled(false);
            layoutEmail.setAlpha(0.5F);
            mlayoutEmail.setAlpha(0.5F);
            tvEmail.setText("N/A");
        }
        if (!cbLandLine.isChecked()) {
            layoutLandline.setEnabled(false);
            mlayoutLandline.setAlpha(0.5F);
            layoutLandline.setAlpha(0.5F);
            tvLandline.setText("N/A");
        }

        layoutWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + getIntent().getStringExtra("website").toString()));
                startActivity(browserIntent);
            }
        });
        layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recipientList = getIntent().getStringExtra("email").toString();
                String[] recipients = recipientList.split(",");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                //intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("message/rfc822");
                startActivity(Intent.createChooser(intent, "Choose an email client"));

            }
        });
        layoutMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListingDetailsActivity.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Call " + getIntent().getStringExtra("mobile").toString());
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.ic_call);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage(getIntent().getStringExtra("mobile").toString());
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("mobile").toString()));
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
        layoutLandline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ListingDetailsActivity.this);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Call " + getIntent().getStringExtra("landline").toString());
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.ic_call);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage(getIntent().getStringExtra("landline").toString());
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + getIntent().getStringExtra("landline").toString()));
                        startActivity(intent);
                    }
                });
                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.CALL_PHONE
                       )
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
}