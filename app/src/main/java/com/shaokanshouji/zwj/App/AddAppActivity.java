package com.shaokanshouji.zwj.App;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/3/30.
 */

public class AddAppActivity extends AppCompatActivity {
    private Spinner mSpinnerChooseApp, mSpinnerDurationStart, mSpinnerDurationEnd,
            mSpinnerTimeHour, mSpinnerTimeMinute;
    private Button mButtonAddDone;

    private void init()
    {
        mSpinnerChooseApp = findViewById(R.id.sp_choose_app);
        mSpinnerDurationStart = findViewById(R.id.sp_duration_start);
        mSpinnerDurationEnd = findViewById(R.id.sp_duration_end);
        mSpinnerTimeHour = findViewById(R.id.sp_time_hour);
        mSpinnerTimeMinute = findViewById(R.id.sp_time_minute);
        mButtonAddDone = findViewById(R.id.btn_add_done);
    }
    private AppRestricted getAppRestricted()
    {
        AppRestricted appRestricted = new AppRestricted();
        appRestricted.setName(mSpinnerChooseApp.getSelectedItem().toString());
        Pair<Integer,Integer> durationStart = timeStringConvert(mSpinnerDurationStart.getSelectedItem().toString());
        appRestricted.setDurationStart(new Time(durationStart.first,durationStart.second));
        Pair<Integer,Integer> durationEnd = timeStringConvert(mSpinnerDurationEnd.getSelectedItem().toString());
        appRestricted.setDurationEnd(new Time(durationEnd.first,durationEnd.second));
        appRestricted.setTimeAvailableSum(new Time(Integer.parseInt(mSpinnerTimeHour.getSelectedItem().toString()),
                Integer.parseInt(mSpinnerTimeMinute.getSelectedItem().toString())));
        return appRestricted;
    }
    //string 07:00 to<07,00>
    private Pair<Integer,Integer> timeStringConvert(String time)
    {
        Pair<Integer,Integer> result = new Pair<Integer, Integer>(Integer.parseInt(time.substring(0,2)),
                Integer.parseInt(time.substring(3,5)));
        return result;
    }

    private boolean checkAccessUsage()
    {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager)getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,applicationInfo.uid,applicationInfo.packageName);
            return (mode==AppOpsManager.MODE_ALLOWED);
        }catch(PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }

    //version5.0 it works, but version6.0 it fails
    private List<ActivityManager.RunningAppProcessInfo> getRunningProcess()
    {
        ActivityManager activityManager = (ActivityManager) AddAppActivity.this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcessInfoList = activityManager.getRunningAppProcesses();
        return runningProcessInfoList;
    }
    private List<UsageStats>getRunningProcess1()
    {
        PackageManager packageManager = getPackageManager();
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP  && !checkAccessUsage()){
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
        }
        long timeMillis = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager)AddAppActivity.this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,timeMillis-24*60*60*1000,timeMillis);
        if(usageStatsList.size()==0)
            Toast.makeText(AddAppActivity.this,"no set",Toast.LENGTH_LONG).show();
        return usageStatsList;
    }

    //get installed non-system packages
    private List<PackageInfo> getInstalledPkgs() {
        List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); ++i) {
            PackageInfo packageInfo = packageInfoList.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                packageInfoList.remove(i);
            }
        }
        return packageInfoList;
    }

    //get List<String>of processName from List<PackageInfo>
    private List<String> getAppNamesFromPackageInfos(List<PackageInfo> pkgInfoList) {
        List<String> appNameList = new ArrayList<String>();
        for(PackageInfo packageInfo:pkgInfoList)
            appNameList.add(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
        return appNameList;
    }

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add_app);
        init();

//        List<UsageStats> usageStatsList = getRunningProcess1();
//        List<String> runningProcessList1 = new ArrayList<String>();
//        if(usageStatsList!=null)
//        for(UsageStats usageStats:usageStatsList)
//            runningProcessList1.add(usageStats.getPackageName());
        ArrayAdapter<String> arrayAdapterApps = new ArrayAdapter<String>(this, R.layout.spinner_app_item, getAppNamesFromPackageInfos(getInstalledPkgs()));
        mSpinnerChooseApp.setAdapter(arrayAdapterApps);



        mButtonAddDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityManager activityManager =(ActivityManager)getSystemService(ACTIVITY_SERVICE);
                String selectedPkgName=mSpinnerChooseApp.getSelectedItem().toString();
                //activityManager.killBackgroundProcesses(selectedPkgName);
                //Intent i = new Intent(AddAppActivity.this,MainActivity.class);
                Intent i = new Intent();
                i.putExtra("AppRestricted",getAppRestricted());
                //startActivity(i);
                setResult(RESULT_OK,i);
                AddAppActivity.this.finish();
            }
        });
    }

    private boolean isAppProcessRunning(Context context, String processName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();
        if (runningAppProcessInfoList.size() > 0) {
            for (ActivityManager.RunningAppProcessInfo taskInfo : runningAppProcessInfoList) {
                if (taskInfo.processName.equals(processName)) {
                    isRunning = true;
                }
            }
        }
        return isRunning;
    }
}
