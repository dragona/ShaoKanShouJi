package com.shaokanshouji.zwj.App;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by zwj on 2018/3/29.
 */

public class AppRestricted implements Parcelable{
    private String mPackageName;
    private String mName;
    private Time mDurationStart;
    private Time mDurationEnd;
    private Time mTimeAvailableSum;
    private Time mTimeLeft;
    private final static String JSON_PKG_NAME="packageName";
    private final static String JSON_APP_NAME="name";
    private final static String JSON_DURATION_START = "durationStart";
    private final static String JSON_DURATION_END="durationEnd";
    private final static String JSON_TIME_AVAILABLE="timeAvailable";
    private final static String JSON_TIME_LEFT="timeLeft";
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
            return new AppRestricted(source);
        }

        @Override
        public AppRestricted[] newArray(int size) {
            return new AppRestricted[size];
        }
    };
    public AppRestricted(JSONObject jsonObject)throws JSONException{
        mPackageName = jsonObject.getString(JSON_PKG_NAME);
        mName = jsonObject.getString(JSON_APP_NAME);
        mDurationStart = new Time(jsonObject.getString(JSON_DURATION_START));
        mDurationEnd=new Time(jsonObject.getString(JSON_DURATION_END));
        mTimeAvailableSum=new Time(jsonObject.getString(JSON_TIME_AVAILABLE));
        mTimeLeft = new Time(jsonObject.getString(JSON_TIME_LEFT));
    }
    public AppRestricted(Parcel source)
    {
        mPackageName = source.readString();
        mName=source.readString();
        mDurationStart=(Time)source.readValue(Time.class.getClassLoader());
        mDurationEnd = (Time)source.readValue(Time.class.getClassLoader());
        mTimeAvailableSum = (Time)source.readValue(Time.class.getClassLoader());
        mTimeLeft = (Time)source.readValue(Time.class.getClassLoader());
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
    public JSONObject toJSON()throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_PKG_NAME,mPackageName);
        jsonObject.put(JSON_APP_NAME,mName);
        jsonObject.put(JSON_DURATION_START,mDurationStart.toStringFormat());
        jsonObject.put(JSON_DURATION_END,mDurationEnd.toStringFormat());
        jsonObject.put(JSON_TIME_AVAILABLE,mTimeAvailableSum.toStringFormat());
        jsonObject.put(JSON_TIME_LEFT,mTimeLeft.toStringFormat());
        return jsonObject;
    }
    public String getPackageName() {
        return mPackageName;
    }

    public void setPackageName(String packageName) {
        mPackageName = packageName;
    }

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
