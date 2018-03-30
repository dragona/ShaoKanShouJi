package com.shaokanshouji.zwj.App;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/3/30.
 */

public class AddAppActivity extends AppCompatActivity {
    private Spinner mSpinnerChooseApp, mSpinnerDurationStart, mSpinnerDurationEnd,
            mSpinnerTimeHour, mSpinnerTimeMinute;
    private Button mButtonAddDone;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_add_app);
        mSpinnerChooseApp = findViewById(R.id.sp_choose_app);

        List<PackageInfo> packageInfoList = getPackageManager().getInstalledPackages(0);
        List<String> appNameList = new ArrayList<String>();
        for (int i = 0; i < packageInfoList.size(); ++i) {
            PackageInfo packageInfo = packageInfoList.get(i);
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                appNameList.add(packageInfo.applicationInfo.loadLabel(getPackageManager()).toString());
            }
        }
        ArrayAdapter<String> arrayAdapterApps = new ArrayAdapter<String>(this, R.layout.spinner_app_item, appNameList);
        mSpinnerChooseApp.setAdapter(arrayAdapterApps);

        mButtonAddDone = findViewById(R.id.btn_add_done);
        mButtonAddDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
