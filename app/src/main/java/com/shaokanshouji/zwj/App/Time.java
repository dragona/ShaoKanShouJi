package com.shaokanshouji.zwj.App;

import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zwj on 2018/3/29.
 */

public class Time extends Binder implements Parcelable {
    private int mHour;
    private int mMinute;
    private int mSecond;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mHour);
        dest.writeInt(mMinute);
        dest.writeInt(mSecond);
    }

    public static final Parcelable.Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel source) {
            return new Time(source.readInt(), source.readInt());
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public Time(int h, int m) {
        mHour = h;
        mMinute = m;
        mSecond = 0;
    }

    public Time() {
        mHour = 0;
        mMinute = 0;
        mSecond = 0;
    }

    public int getHour() {
        return mHour;
    }

    public void setHour(int hour) {
        mHour = hour;
    }

    public int getMinute() {
        return mMinute;
    }

    public void setMinute(int minute) {
        mMinute = minute;
    }

    public int getSecond() {
        return mSecond;
    }

    public void setSecond(int second) {
        mSecond = second;
    }
}
