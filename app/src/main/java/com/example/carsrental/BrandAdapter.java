package com.example.carsrental;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsrental.entities.Brand;
import com.example.carsrental.firebase.Storage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder>{
    ArrayList<Brand> brandList=new ArrayList<>();

    public ArrayList<Brand> getBrandList() {
        return brandList;
    }

    int index=0;
    Context context;
    private OnclickListener onClickListener;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface OnclickListener{
        void onClick(Brand brand);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView brandLogo;
        TextView brandName;
        public ViewHolder(View view) {
            super(view);
            brandLogo=view.findViewById(R.id.brand_logo);
            brandName=view.findViewById(R.id.brand_title);

        }

        @SuppressLint("ResourceAsColor")
        public void bind(Brand brand) {
            brandName.setText(brand.getNom());
            Storage.getImage(brand.getLogo(),brandLogo, brandLogo.getContext());

        }

    }
    public void setBrands(ArrayList<Brand> brandList) {
        this.brandList = brandList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.brand_layout,parent,false);
        return new ViewHolder(view);
    }


    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(BrandAdapter.ViewHolder holder,final int position) {
        Brand brand = brandList.get(position);
        holder.bind(brand);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                index=holder.getAdapterPosition();
                onClickListener.onClick(brandList.get(index));
                notifyDataSetChanged();
            }
        });
        if (index==position){
            holder.brandName.setTextColor(context.getColor(R.color.blue));
            holder.brandLogo.setStrokeColor(ColorStateList.valueOf(context.getColor(R.color.blue)));
            holder.brandLogo.setStrokeWidth(3.0F);
        }else {
            holder.brandName.setTextColor(context.getColor(R.color.dark_blue));
            holder.brandLogo.setStrokeColor(ColorStateList.valueOf(R.color.gray));
            holder.brandLogo.setStrokeWidth(0.5F);
        }

    }


    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
