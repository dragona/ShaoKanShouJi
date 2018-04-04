package com.shaokanshouji.zwj.App;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/4/2.
 */

public class MonitorService extends IntentService {
    private static final String TAG = "MonitorService";
    private static final int MONITOR_INTERVAL = 1000 * 15;//5s
    private List<AppRestricted> mAppRestrictedList;
    private List<UsageStats> mUsageStatsList;

    public MonitorService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.i(TAG, "Received an intent:" + intent);
        PackageManager packageManager = getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && !checkAccessUsage()) {
            startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
            return;
        }
        mAppRestrictedList = intent.getParcelableArrayListExtra("AppRestrictedList");
        Log.i(TAG,"get appRestricted("+mAppRestrictedList.size()+") from MainActivity");
        if (mAppRestrictedList == null) {
            Log.i(TAG, "mAppRestrictedList==null");
            return;
        }
        mUsageStatsList = (ArrayList) getAppDailyUsageStats();
        if (mUsageStatsList == null) {
            Log.i(TAG, "mUsageStatsList==null");
            return;
        }
//!!!bug!!!
//            for (int i = 0;i<mUsageStatsList.size();++i) {
//                for (int j = 0;j<mAppRestrictedList.size();++j) {
//                    if (!mUsageStatsList.get(i).getPackageName().equals(mAppRestrictedList.get(j).getPackageName()))
//                        mUsageStatsList.remove(i);
//                }
//           }
//!!!correct!!!
//        for (int i = 0; i < mUsageStatsList.size(); ++i) {
//            for (int j = 0; j < mAppRestrictedList.size(); ++j) {
//                if (!mUsageStatsList.get(i).getPackageName().equals(mAppRestrictedList.get(j).getPackageName())) {
//                    mUsageStatsList.remove(i);
//                    --i;
//                    break;
//                }
//            }
//        }
        updateAppRestrictedInfo();
        if (intent.hasExtra("updateAppRestricted")) {
            if (intent.getStringExtra("updateAppRestricted").equals("start")) {
                //intent.putExtra("updateAppRestricted","stop");
                Log.i(TAG, "send appRestrictedList("+mAppRestrictedList.size()+") to MainActivity");
                Message message = new Message();
                message.obj = mAppRestrictedList;
                MainActivity.mHandler.sendMessage(message);
            }
        }
        Log.i(TAG, "appUsageStatsList.size" + mUsageStatsList.size());
        Log.i(TAG, "appRestrictedList.size:" + mAppRestrictedList.size());
        for (AppRestricted app : mAppRestrictedList) {
            String packageName = app.getPackageName();
            if (isAppRunning(packageName)) {
                Log.i(TAG, app.getName() + " running");
                if (app.getTimeLeft().getTimeMillis() < 0) {
                    //killApp(packageName);
                    Log.i(TAG, "kill " + app.getName());
                }
            }
        }
    }

    //version5.0 it works, but version6.0 it fails
    private List<ActivityManager.RunningAppProcessInfo> getRunningProcess() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningProcessInfoList = activityManager.getRunningAppProcesses();
        return runningProcessInfoList;
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

    private boolean checkAccessUsage() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode == AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private List<UsageStats> getAppDailyUsageStats() {
        long timeMillis = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, timeMillis - 24 * 60 * 60 * 1000, timeMillis);
        if (usageStatsList.size() == 0)
            Toast.makeText(this, "no permission to access App usage stats", Toast.LENGTH_LONG).show();
        return usageStatsList;
    }

    //is the app running in last 5second
    private boolean isAppRunning(String packageName) {
        long timeMillis = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> usageStatsList = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, timeMillis - 5 * 1000, timeMillis);
        if (usageStatsList == null || usageStatsList.isEmpty())
            return false;
        for (UsageStats usageStats : usageStatsList) {
            if (usageStats.getPackageName().equals(packageName))
                return true;
        }
        return false;
    }

//    private boolean canItRun(String packageName) {
//        AppRestricted appRestricted;
//        for (AppRestricted app : mAppRestrictedList) {
//            if (app.getPackageName().equals(packageName)) {
//                return app.getTimeLeft().getTimeMillis() > 0;
//            }
//        }
//        return true;
//    }

    private void updateAppRestrictedInfo() {
        for (int i = 0; i < mAppRestrictedList.size(); ++i) {
            AppRestricted app = mAppRestrictedList.get(i);
            for (UsageStats us : mUsageStatsList) {
                if (us.getPackageName().equals(app.getPackageName()))
                    app.setTimeLeft(new Time(app.getTimeAvailableSum().getTimeMillis() - us.getTotalTimeInForeground()));
            }
        }
    }

    private void killApp(String pkgName) {
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(pkgName);
    }

    public static void setServiceAlarm(Context context, boolean isOn, Intent i) {
        PendingIntent pi = PendingIntent.getService(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (isOn) {
            alarmManager.setRepeating(AlarmManager.RTC, System.currentTimeMillis(), MONITOR_INTERVAL, pi);
        } else {
            alarmManager.cancel(pi);
            pi.cancel();
        }
    }
}
