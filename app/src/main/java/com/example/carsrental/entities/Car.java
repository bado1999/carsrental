package com.example.carsrental.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Car implements Parcelable, Serializable {
    private String nom;
    private String reference;
    private String image;
    private String marque;
    private String capacity;
    private String type;
    private String maxSpeed;
    int rentPrice;

    private boolean status;


    protected Car(Parcel in) {
        nom = in.readString();
        reference = in.readString();
        image = in.readString();
        marque = in.readString();
        capacity = in.readString();
        type = in.readString();
        maxSpeed = in.readString();
        rentPrice = in.readInt();
        status = in.readByte() != 0;
    }


    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public int getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(int rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Car() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String name) {
        this.nom = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }
    public  boolean getStatus(){
        return  status;
    }

    public void setStatus(boolean status) {
        this.status =status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(nom);
        parcel.writeString(reference);
        parcel.writeString(image);
        parcel.writeString(marque);
        parcel.writeString(capacity);
        parcel.writeString(type);
        parcel.writeString(maxSpeed);
        parcel.writeDouble(rentPrice);
        parcel.writeByte((byte) (status ? 1 : 0));
    }
}
