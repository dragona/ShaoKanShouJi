package com.shaokanshouji.zwj.App;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by zwj on 2018/3/29.
 */

public class AppRestricted implements Parcelable{
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPackageName);
        dest.writeString(mName);
        dest.writeValue(mDurationStart);
        dest.writeValue(mDurationEnd);
        dest.writeValue(mTimeAvailableSum);
        dest.writeValue(mTimeLeft);
    }

    public static final Parcelable.Creator<AppRestricted> CREATOR = new Creator<AppRestricted>() {
        @Override
        public  AppRestricted createFromParcel(Parcel source) {
            return new AppRestricted(source.readString(), source.readString(),source.readValue(Time.class.getClassLoader()),
                    source.readValue(Time.class.getClassLoader()),source.readValue(Time.class.getClassLoader()),
                            source.readValue(Time.class.getClassLoader()));
        }

        @Override
        public AppRestricted[] newArray(int size) {
            return new AppRestricted[size];
        }
    };
    public AppRestricted(String packageName,String name,Object durationStart,Object durationEnd,Object timeAvailableSum,Object timeLeft)
    {
        mPackageName = packageName;
        mName=name;
        mDurationStart=(Time)durationStart;
        mDurationEnd = (Time)durationEnd;
        mTimeAvailableSum = (Time)timeAvailableSum;
        mTimeLeft = (Time)timeLeft;
    }
    public AppRestricted(String packageName,String name,Time durationStart,Time durationEnd,Time timeAvailableSum,Time timeLeft)
    {
        mPackageName = packageName;
        mName=name;
        mDurationStart=durationStart;
        mDurationEnd = durationEnd;
        mTimeAvailableSum = timeAvailableSum;
        mTimeLeft = timeLeft;
    }
    public AppRestricted()
    {
        mPackageName="";
        mName="";
        mDurationStart=new Time();
        mDurationEnd = new Time();
        mTimeAvailableSum = new Time();
        mTimeLeft = new Time();
    }
    private String mPackageName;
    private String mName;
    private Time mDurationStart;
    private Time mDurationEnd;
    private Time mTimeAvailableSum;
    private Time mTimeLeft;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Time getDurationStart() {
        return mDurationStart;
    }

    public void setDurationStart(Time durationStart) {
        mDurationStart = durationStart;
    }

    public Time getDurationEnd() {
        return mDurationEnd;
    }

    public void setDurationEnd(Time durationEnd) {
        mDurationEnd = durationEnd;
    }

    public Time getTimeAvailableSum() {
        return mTimeAvailableSum;
    }

    public void setTimeAvailableSum(Time timeAvailableSum) {
        mTimeAvailableSum = timeAvailableSum;
    }

    public Time getTimeLeft() {
        return mTimeLeft;
    }

    public void setTimeLeft(Time timeLeft) {
        mTimeLeft = timeLeft;
    }
    public String getDuration()
    {
        String startHour = (mDurationStart.getHour()>9)?""+mDurationStart.getHour():"0"+mDurationStart.getHour();
        String startMinute = (mDurationStart.getMinute()>9)?""+mDurationStart.getMinute():"0"+mDurationStart.getMinute();
        String endHour =  (mDurationEnd.getHour()>9)?""+mDurationEnd.getHour():"0"+mDurationEnd.getHour();
        String endMinute = (mDurationEnd.getMinute()>9)?""+mDurationEnd.getMinute():"0"+mDurationEnd.getMinute();
        return startHour+":"+startMinute+"-"+endHour+":"+endMinute;
    }
    public String getLeftAndSum()
    {
        String leftHour = (mTimeLeft.getHour()>9)?""+mTimeLeft.getHour():"0"+mTimeLeft.getHour();
        String leftMinute = (mTimeLeft.getMinute()>9)?""+mTimeLeft.getMinute():"0"+mTimeLeft.getMinute();
        String sumHour =  (mTimeAvailableSum.getHour()>9)?""+mTimeAvailableSum.getHour():"0"+mTimeAvailableSum.getHour();
        String sumMinute = (mTimeAvailableSum.getMinute()>9)?""+mTimeAvailableSum.getMinute():"0"+mTimeAvailableSum.getMinute();
        return leftHour+"h"+leftMinute+"m/"+sumHour+"h"+sumMinute+"m";
    }
}
