package com.social.businessdirectory.Adapters;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.social.businessdirectory.Model.Categories;
import com.social.businessdirectory.Model.CompanyModel;
import com.social.businessdirectory.R;


import java.util.List;

public class MyAdapterListing extends RecyclerView.Adapter<MyAdapterListing.ViewHolder> {

    private Context context;
    private AppCompatActivity activity;
    private List<CompanyModel> list;
    CompanyModel companyModel;

    private OnItemClickListener mListener;

    public MyAdapterListing(Context context, List<CompanyModel> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_listing, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        companyModel = list.get(position);

        holder.tvCompanyName.setText(companyModel.getCompanyName());
        holder.tvCategoryName.setText(companyModel.getCategoryName());
        holder.tvDescription.setText(companyModel.getBusinessDisc());
//        Glide.with(context).load(companyModel.getBusinessCard())
//                .centerCrop()
//                .placeholder(R.drawable.ic_search)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        return false;
//                    }
//                }).into(holder.ivBusinessCard);
        if (list.get(position).getcMobile().equalsIgnoreCase("false")) {
            holder.layoutMobile.setEnabled(false);
            holder.layoutMobile.setAlpha(0.5F);
        }
        if (list.get(position).getcEmail().equalsIgnoreCase("false")) {
            holder.layoutEmail.setEnabled(false);
            holder.layoutEmail.setAlpha(0.5F);
        }
        if (list.get(position).getcWebsite().equalsIgnoreCase("false")) {
            holder.layoutWebsite.setEnabled(false);
            holder.layoutWebsite.setAlpha(0.5F);
        }
        if (list.get(position).getcLandLine().equalsIgnoreCase("false")) {
            holder.layoutLandLine.setEnabled(false);
            holder.layoutLandLine.setAlpha(0.5F);
        }


        holder.layoutMobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Call " + list.get(position).getMobile());
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.ic_call);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage(list.get(position).getMobile());
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + list.get(position).getMobile()));
                        context.startActivity(intent);
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
        holder.layoutLandLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestMultiplePermissions();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                // Setting Alert Dialog Title
                alertDialogBuilder.setTitle("Call " + list.get(position).getLandline());
                // Icon Of Alert Dialog
                alertDialogBuilder.setIcon(R.drawable.ic_call);
                // Setting Alert Dialog Message
                alertDialogBuilder.setMessage(list.get(position).getLandline());
                alertDialogBuilder.setCancelable(false);
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + list.get(position).getLandline()));
                        context.startActivity(intent);
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
        holder.layoutEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String recipientList = list.get(position).getEmail().toString();
                String[] recipients = recipientList.split(",");

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                //intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                //intent.putExtra(Intent.EXTRA_TEXT, message);
                intent.setType("message/rfc822");
                context.startActivity(Intent.createChooser(intent, "Choose an email client"));


            }
        });
        holder.layoutWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + list.get(position).getWebsite()));
                context.startActivity(browserIntent);

            }
        });
    }

    @Override
    final public int getItemCount() {
       // return list.size();
        return Math.min(list.size(), 10);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout layoutMobile, layoutEmail, layoutWebsite, layoutLandLine;
        public TextView tvCompanyName, tvCategoryName, tvDescription;


        public ViewHolder(View itemView) {
            super(itemView);


            layoutMobile = itemView.findViewById(R.id.layoutMobile);
            layoutLandLine = itemView.findViewById(R.id.layoutLandLine);
            layoutEmail = itemView.findViewById(R.id.layoutEmail);
            layoutWebsite = itemView.findViewById(R.id.layoutWebsite);
            tvCompanyName = itemView.findViewById(R.id.tvCompanyName);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            tvDescription = itemView.findViewById(R.id.tvDescription);


            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    mListener.onListingItemClick(position);

                }
            }
        }

    }

    public interface OnItemClickListener {
        void onListingItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private void requestMultiplePermissions() {
        Dexter.withActivity((Activity) context)
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
                        Toast.makeText(context, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}
