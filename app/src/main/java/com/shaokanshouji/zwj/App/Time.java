package com.shaokanshouji.zwj.App;

/**
 * Created by zwj on 2018/3/29.
 */

public class Time {
    private int mHour;
    private int mMinute;
    private int mSecond;
    public Time(int h,int m,int s)
    {
        mHour=h;
        mMinute =m;
        mSecond=s;
    }
    public Time()
    {
        mHour=0;
        mMinute=0;
        mSecond=0;
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
