package com.shaokanshouji.zwj.App;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/3/29.
 */

public class MainActivity extends AppCompatActivity {
    private ListView mListViewAppRestricted;
    private List<AppRestricted> mAppRestrictedList = new ArrayList<AppRestricted>();
    private Button mButtonDisableRestriction;
    private ImageView mImageViewAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AppRestricted app = new AppRestricted(001, "QQ", new Time(06, 00, 00), new Time(24, 00, 00),
                new Time(01, 00, 00), new Time(00, 30, 00));
        mAppRestrictedList.add(app);
        mAppRestrictedList.add(new AppRestricted(001, "WeChat", new Time(06, 00, 00), new Time(24, 00, 00),
                new Time(01, 00, 00), new Time(00, 30, 00)));
        mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        //mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);mAppRestrictedList.add(app);
        mListViewAppRestricted = findViewById(R.id.lv_app_restricted);
        AppRestrictedAdapter appRestrictedAdapter= new AppRestrictedAdapter(MainActivity.this,R.layout.listview_item,mAppRestrictedList);
        mListViewAppRestricted.setAdapter(appRestrictedAdapter);
        mListViewAppRestricted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppRestricted app= mAppRestrictedList.get(i);
                Toast.makeText(MainActivity.this,app.getName(),Toast.LENGTH_LONG).show();
            }
        });
        mButtonDisableRestriction = findViewById(R.id.btn_disable_restriction);
        mButtonDisableRestriction.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MainActivity.this,EnterPasswordActivity.class);
                i.putExtra(EnterPasswordActivity.EXTRA_WHO_CALL_ME,"MainActivity");
                startActivity(i);
            }
        });
        mImageViewAdd = findViewById(R.id.iv_add);
        mImageViewAdd.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //mAppRestrictedList.add(app);
                Intent i = new Intent(MainActivity.this,AddAppActivity.class);
                startActivity(i);
            }
        });

    }
}
