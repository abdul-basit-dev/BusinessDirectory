package com.social.businessdirectory.UI;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.social.businessdirectory.R;


public class ContactUsActivity extends AppCompatActivity {
    TextInputLayout Name, Mobile, Email;
    EditText message;
    Button btnSend;
    TextView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_contact_us);

        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_id);
        setSupportActionBar(toolbar);
        ImageView ivBack;
        TextView textView;
        ivBack = toolbar.findViewById(R.id.ivBack);
        textView = toolbar.findViewById(R.id.tvTitle);
        textView.setText("Contact Us");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //setting up toolbar Finished...


        Name = findViewById(R.id.name);
        Mobile = findViewById(R.id.mobile);
        Email = findViewById(R.id.email);
        message = findViewById(R.id.message);
        btnSend = findViewById(R.id.btnSend);


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendNShowPOPUP();
            }
        });
    }

    public void SendNShowPOPUP() {

        if (Name.getEditText().getText().toString().isEmpty()) {
            Name.setError("Please Fill in Name");
            Name.requestFocus();
        } else if (Mobile.getEditText().getText().toString().isEmpty()) {
            Mobile.setError("Please Fill in Mobile");
            Mobile.requestFocus();
        } else if (Email.getEditText().getText().toString().isEmpty()) {
            Email.setError("Please Fill in Email");
            Email.requestFocus();
        } else if (message.getText().toString().isEmpty()) {
            message.setError("Please Fill in Message");
            message.requestFocus();
        } else if (!Name.getEditText().getText().toString().isEmpty()
                && !Mobile.getEditText().getText().toString().isEmpty()
                && !Email.getEditText().getText().toString().isEmpty()
                && !message.getText().toString().isEmpty()) {


            String recipientList = "www.example@gmail.com";
            String[] recipients = recipientList.split(",");

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            intent.putExtra(Intent.EXTRA_TEXT,Name.getEditText().getText().toString()+"\n"+Mobile.getEditText().getText().toString()+"\n"+ message.getText().toString());
            intent.setType("message/rfc822");
            startActivity(Intent.createChooser(intent, "Choose an email client"));

//            final android.app.AlertDialog.Builder mBuilder = new android.app.AlertDialog.Builder(ContactUsActivity.this);
//            LayoutInflater layoutInflater1 = LayoutInflater.from(ContactUsActivity.this);
//            View view1 = layoutInflater1.inflate(R.layout.layout_dialog_contact_us, null);
//            btnClose = view1.findViewById(R.id.tvClose);
//            mBuilder.setView(view1);
//            mBuilder.setCancelable(false);
//
//            final AlertDialog dialog = mBuilder.create();
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_error_layout);
//            dialog.setView(view1, 0, 0, 0, 0);
//
//
//            dialog.show();
//            btnClose.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mBuilder.setCancelable(true);
//                    dialog.cancel();
//                    dialog.dismiss();
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
//                }
//            });
//
        }


    }
}