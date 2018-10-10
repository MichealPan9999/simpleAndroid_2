package com.example.panzq.simpleandroid_2;

import android.os.Parcel;
import android.os.Parcelable;

public class User2 implements Parcelable {
    public int UserId;
    public String userName;
    public boolean isMale;

    public User2(int userId, String userName, boolean isMale) {
        UserId = userId;
        this.userName = userName;
        this.isMale = isMale;
    }

    protected User2(Parcel in) {
        UserId = in.readInt();
        userName = in.readString();
        isMale = in.readByte() != 0;
    }

    public static final Creator<User2> CREATOR = new Creator<User2>() {
        @Override
        public User2 createFromParcel(Parcel in) {
            return new User2(in);
        }

        @Override
        public User2[] newArray(int size) {
            return new User2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(UserId);
        parcel.writeString(userName);
        parcel.writeByte((byte) (isMale ? 1 : 0));
    }


}
