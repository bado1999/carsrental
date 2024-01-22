package com.example.carsrental;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsrental.entities.Car;
import com.example.carsrental.firebase.Storage;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private List<Car> carList=new ArrayList<>();
    private OnClickListener onClickListener;
    private Context context;
    public void setCars(List<Car> cars) {
        carList = cars;
        notifyDataSetChanged();
    }

    public List<Car> getCarList() {
        return carList;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.car_layout, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(holder.getAdapterPosition());
        holder.bind(car);
        View view=holder.itemView;
        TextView status=view.findViewById(R.id.status);
        Resources resources=view.getContext().getResources();
        int red=resources.getColor(R.color.red),green=resources.getColor(R.color.green);
        status.setTextColor(car.getStatus()?green:red);
        holder.itemView.setOnClickListener(view1 -> {
            if (onClickListener != null) {
                int index=holder.getAdapterPosition();
                onClickListener.onClick(index,carList.get(index));
            }
        });
    }
    @Override
    public int getItemCount() {
        return carList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
    public interface OnClickListener {
        void onClick(int position, Car car);
    }
    static class CarViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView capacity;
        private TextView status;
        private  TextView price;
        private ShapeableImageView imageView;
        public CarViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.car_title);
            status=itemView.findViewById(R.id.status);
            capacity=itemView.findViewById(R.id.seats);
            imageView=itemView.findViewById(R.id.car_photo);
            price=itemView.findViewById(R.id.price);
        }

        public void bind(Car car) {
            name.setText(car.getNom());
            Storage.getImage(car.getImage(),imageView, imageView.getContext());
            capacity.setText(car.getCapacity());
            status.setText(car.getStatus()?"Available":"Unavailable");
            price.setText(car.getRentPrice()+"dh/day");
        }

    }

}

