package com.social.businessdirectory.Adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.social.businessdirectory.Model.Categories;
import com.social.businessdirectory.R;


import java.util.List;

public class MyAdapterCategory extends RecyclerView.Adapter<MyAdapterCategory.ViewHolder> {

    private Context context;
    private List<Categories> list;
    Categories categories;

    private OnItemClickListener mListener;

    public MyAdapterCategory(Context context, List<Categories> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        categories = list.get(position);

        holder.tvCategoryName.setText(categories.getCategoryName());

//        Picasso.with(context)
//                .load(categories.getCategoryImage()).fit().centerCrop()
//                .placeholder(R.drawable.ic_home)
//                .error(R.drawable.ic_home)
//                .into(holder.imageViewCategory);

        Glide.with(context).load(categories.getCategoryImage())
                .centerCrop()
                .placeholder(R.drawable.ic_home)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(holder.imageViewCategory);

    }

    @Override
    final public int getItemCount() {

        return list.size();

    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageViewCategory;
        public TextView tvCategoryName;


        public ViewHolder(View itemView) {
            super(itemView);

            imageViewCategory = itemView.findViewById(R.id.ivCategoryIcon);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                    mListener.onItemClick(position);

                }
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


}
