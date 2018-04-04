package com.shaokanshouji.zwj.App;

import android.os.Binder;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zwj on 2018/3/29.
 * <p>
 * int mSecond is never used
 */

public class Time implements Parcelable {
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
        //！！！a big bug！！！
        //dest.writeInt(mSecond);
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

    //string 03:30 to Time var
    //string 0330 to Time var
    public Time(String s) {
        if (s.length() == 5) {
            mHour = Integer.parseInt(s.substring(0, 2));
            mMinute = Integer.parseInt(s.substring(3, 5));
        } else if (s.length() == 4) {
            mHour = Integer.parseInt(s.substring(0, 2));
            mMinute = Integer.parseInt(s.substring(2, 4));
        } else {
            mHour = 0;
            mMinute = 0;
        }
    }
    //to string such as 03:30
    public String toStringFormat(){
        String hour=mHour>9?""+mHour:"0"+mHour;
        String minute=mMinute>9?""+mMinute:"0"+mMinute;
        return hour+":"+minute;
    }

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

    public Time(long millis) {
        mHour = (int) (millis / 60 * 60 * 1000);
        mMinute = (int) ((millis - mHour * 60 * 60 * 1000) / 60 * 1000);
    }

    public int getTimeMillis() {
        return mHour * 60 * 60 * 1000 + mMinute * 60 * 1000;
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
