package com.ialex.foodsavr.data.remote.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by alex on 25/03/2018.
 */

public class RecipeItem implements Parcelable {

    @SerializedName("ID")
    public Integer id;

    @SerializedName("Name")
    public String name;

    @SerializedName("Photo")
    public String photo;

    @SerializedName("Ingredients")
    public List<String> ingredients;

    protected RecipeItem(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readInt();
        }
        name = in.readString();
        photo = in.readString();
        ingredients = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(id);
        }
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeStringList(ingredients);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RecipeItem> CREATOR = new Creator<RecipeItem>() {
        @Override
        public RecipeItem createFromParcel(Parcel in) {
            return new RecipeItem(in);
        }

        @Override
        public RecipeItem[] newArray(int size) {
            return new RecipeItem[size];
        }
    };
}
