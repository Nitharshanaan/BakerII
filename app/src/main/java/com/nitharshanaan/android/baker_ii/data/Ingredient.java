package com.nitharshanaan.android.baker_ii.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {

    static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };
    private String ingredient;
    private String measure;
    private String quantity;

    private Ingredient(Parcel in) {
        ingredient = in.readString();
        measure = in.readString();
        quantity = in.readString();
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public String getQuantity() {
        return quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ingredient);
        parcel.writeString(measure);
        parcel.writeString(quantity);
    }
}
