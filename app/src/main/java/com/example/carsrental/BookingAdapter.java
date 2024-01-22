package com.example.carsrental;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.carsrental.entities.Booking;
import com.example.carsrental.entities.Car;
import com.example.carsrental.firebase.Storage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    ArrayList<Booking> brandList = new ArrayList<>();

    public ArrayList<Booking> getBrandList() {
        return brandList;
    }

    private OnclickListener onClickListener;

    public void setOnClickListener(OnclickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnclickListener {
        void onClick(Booking brand);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView capacity;
        private TextView price;
        private ShapeableImageView imageView;
        private TextView reservation_date;
        private Button cancel;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.title);
            imageView = view.findViewById(R.id.car_photo);
            price = view.findViewById(R.id.reservation_price);
            reservation_date = view.findViewById(R.id.reservation_date);
            capacity = view.findViewById(R.id.seats);


        }

        @SuppressLint("ResourceAsColor")
        public void bind(Booking booking) {
            Car car = booking.getCar();
            name.setText(car.getNom());
            Storage.getImage(car.getImage(), imageView, imageView.getContext());
            capacity.setText(car.getCapacity());
            reservation_date.setText(booking.getReservationDate()+"  -  "+ booking.getReturnDate());
            price.setText(car.getRentPrice() + " dh/day");

        }

    }

    public void setBrands(ArrayList<Booking> brandList) {
        this.brandList = brandList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reservation_layout, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BookingAdapter.ViewHolder holder, final int position) {
        Booking brand = brandList.get(position);
        holder.bind(brand);
        holder.itemView.setOnClickListener(view -> {
            onClickListener.onClick(brandList.get(holder.getAdapterPosition()));
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return brandList.size();
    }
}
