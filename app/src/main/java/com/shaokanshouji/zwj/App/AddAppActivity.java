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
    private List<PackageInfo> mPackageInfoList;
    private List<String> mAppNameList;
    //private int mPos;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add_app);
        init();
        mPackageInfoList = getInstalledPkgs();
        mAppNameList = getAppNamesFromPackageInfos(mPackageInfoList);
        //mPos = 0;
//        List<UsageStats> usageStatsList = getRunningProcess1();
//        List<String> runningProcessList1 = new ArrayList<String>();
//        if(usageStatsList!=null)
//        for(UsageStats usageStats:usageStatsList)
//            runningProcessList1.add(usageStats.getPackageName());
        //mSpinnerChooseApp.setOnItemClickListener();
        ArrayAdapter<String> arrayAdapterApps = new ArrayAdapter<String>(this, R.layout.spinner_app_item, mAppNameList);
        mSpinnerChooseApp.setAdapter(arrayAdapterApps);


        mButtonAddDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                AppRestricted appRestricted = getAppRestricted();
                i.putExtra("AddAppToRestrict", appRestricted);
                //startActivity(i);
                setResult(RESULT_OK, i);
                AddAppActivity.this.finish();
            }
        });
    }

    private void init() {
        mSpinnerChooseApp = findViewById(R.id.sp_choose_app);
        mSpinnerDurationStart = findViewById(R.id.sp_duration_start);
        mSpinnerDurationEnd = findViewById(R.id.sp_duration_end);
        mSpinnerTimeHour = findViewById(R.id.sp_time_hour);
        mSpinnerTimeMinute = findViewById(R.id.sp_time_minute);
        mButtonAddDone = findViewById(R.id.btn_add_done);
    }

    private AppRestricted getAppRestricted() {
        AppRestricted appRestricted = new AppRestricted();
        appRestricted.setName(mSpinnerChooseApp.getSelectedItem().toString());
        appRestricted.setPackageName(mPackageInfoList.get(mSpinnerChooseApp.getSelectedItemPosition()).packageName);
        appRestricted.setDurationStart(new Time(mSpinnerDurationStart.getSelectedItem().toString()));
        appRestricted.setDurationEnd(new Time(mSpinnerDurationEnd.getSelectedItem().toString()));
        appRestricted.setTimeAvailableSum(new Time(mSpinnerTimeHour.getSelectedItem().toString() +
                mSpinnerTimeMinute.getSelectedItem().toString()));
        appRestricted.setTimeLeft(appRestricted.getTimeAvailableSum());
        return appRestricted;
    }

    //get List<String>of processName from List<PackageInfo>
    private List<String> getAppNamesFromPackageInfos(List<PackageInfo> pkgInfoList) {
        List<String> appNameList = new ArrayList<String>();
        for (PackageInfo packageInfo : pkgInfoList)
            appNameList.add(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
        return appNameList;
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
}
