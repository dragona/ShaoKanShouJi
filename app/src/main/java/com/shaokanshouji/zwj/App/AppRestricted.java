package com.shaokanshouji.zwj.App;

import java.util.Date;

/**
 * Created by zwj on 2018/3/29.
 */

public class AppRestricted {
    public AppRestricted(int id,String name,Time durationStart,Time durationEnd,Time timeAvailableSum,Time timeLeft)
    {
        mId=id;
        mName=name;
        mDurationStart=durationStart;
        mDurationEnd = durationEnd;
        mTimeAvailableSum = timeAvailableSum;
        mTimeLeft = timeLeft;
    }
    public AppRestricted()
    {
        mId=0;
        mName="";
        mDurationStart=new Time();
        mDurationEnd = new Time();
        mTimeAvailableSum = new Time();
        mTimeLeft = new Time();
    }
    private int mId;
    private String mName;
    private Time mDurationStart;
    private Time mDurationEnd;
    private Time mTimeAvailableSum;
    private Time mTimeLeft;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
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
