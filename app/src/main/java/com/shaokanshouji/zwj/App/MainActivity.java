package com.shaokanshouji.zwj.App;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zwj on 2018/3/29.
 */

public class MainActivity extends AppCompatActivity {
    private ListView mListViewAppRestricted;
    private List<AppRestricted> mAppRestrictedList = new ArrayList<AppRestricted>();
    AppRestrictedAdapter mAppRestrictedAdapter;
    private Button mButtonDisableRestriction;
    private ImageView mImageViewAdd;
    private final int ADD_APP_ACT = 0;
    private final int RESTRICTION_DETAIL_ACT = 1;
    private final int ENTER_PASSWORD_TO_DISABLE = 2;
    public static Handler mHandler;
    private String APP_RESTRICTED_DATA_FILE_NAME = "AppRestrictedList.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AppRestricted app = new AppRestricted("com.android.gallery3d", "Gallery", new Time(06, 00), new Time(24, 00),
                new Time(01, 00), new Time());
        for (int i = 0; i < 0; ++i) {
            mAppRestrictedList.add(app);
        }

        mListViewAppRestricted = findViewById(R.id.lv_app_restricted);
        try {
            readAppRestrictedListFromFile();
        }catch(Exception e){
            Log.e("MainActivity",e.getMessage());
        }
        mAppRestrictedAdapter = new AppRestrictedAdapter(MainActivity.this, R.layout.listview_item, mAppRestrictedList);
        mListViewAppRestricted.setAdapter(mAppRestrictedAdapter);
        mListViewAppRestricted.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AppRestricted app = mAppRestrictedList.get(i);
                Toast.makeText(MainActivity.this, app.getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, RestrictionDetailActivity.class);
                intent.putExtra("AppDetail", app);
                startActivityForResult(intent, RESTRICTION_DETAIL_ACT);
                //startActivity(intent);
            }
        });
        mButtonDisableRestriction = findViewById(R.id.btn_disable_restriction);
        mButtonDisableRestriction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, EnterPasswordActivity.class);
                i.putExtra(EnterPasswordActivity.EXTRA_WHO_CALL_ME, "MainAct");
                startActivityForResult(i, ENTER_PASSWORD_TO_DISABLE);
            }
        });
        mImageViewAdd = findViewById(R.id.iv_add);
        mImageViewAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddAppActivity.class);
                startActivityForResult(i, ADD_APP_ACT);
            }
        });
        //!!!test!!!
        //mAppRestrictedAdapter.remove(new AppRestricted("WeChat", "WeChat", new Time(06, 00), new Time(24, 00),
        //        new Time(01, 00), new Time()));
        //WeChat still here!!!
        //after override remove, it works

        Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
        intentToService.putParcelableArrayListExtra("AppRestrictedList", (ArrayList) mAppRestrictedList);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<AppRestricted> appRestrictedList = (List<AppRestricted>) msg.obj;
                Log.i("MainActivity", "get appRestrictedList("+appRestrictedList.size()+") from MonitorService");
                Log.i("MainActivity","adapter size:"+mAppRestrictedAdapter.getCount());
                //mAppRestrictedList = appRestrictedList;
            }
        };
        intentToService.putExtra("updateAppRestricted", "start");
        MonitorService.setServiceAlarm(this, true, intentToService);
    }
    @Override
    protected void onStop(){
        Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
        intentToService.putParcelableArrayListExtra("AppRestrictedList", (ArrayList) mAppRestrictedList);
        MonitorService.setServiceAlarm(this, true, intentToService);
        super.onStop();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_APP_ACT) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("AddAppToRestrict")) {
                    AppRestricted app = data.getParcelableExtra("AddAppToRestrict");
                    mAppRestrictedAdapter.add(app);

                    Log.i("MainActivity","adapte.size():"+mAppRestrictedAdapter.getCount()+
                            "list.size():"+mAppRestrictedList.size());
                    try{
                        writeAppRestrictedListToFile();
                    }catch(Exception e){
                        Log.e("MainActivity",e.getMessage());
                    }

                    Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
                    intentToService.putParcelableArrayListExtra("AppRestrictedList", (ArrayList) mAppRestrictedList);
                    intentToService.putExtra("updateAppRestricted", "start");
                    MonitorService.setServiceAlarm(this, true, intentToService);
                }
            }
        } else if (requestCode == RESTRICTION_DETAIL_ACT) {
            if (resultCode == RESULT_OK) {
                if (data.hasExtra("AppRestrictedModified")) {
                    AppRestricted app = data.getParcelableExtra("AppRestrictedModified");
                    mAppRestrictedAdapter.update(app);

                    try{
                        writeAppRestrictedListToFile();
                    }catch(Exception e){
                        Log.e("MainActivity",e.getMessage());
                    }

                    Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
                    intentToService.putParcelableArrayListExtra("AppRestrictedList", (ArrayList) mAppRestrictedList);
                    intentToService.putExtra("updateAppRestricted", "start");
                    MonitorService.setServiceAlarm(this, true, intentToService);
                } else if (data.hasExtra("AppRestrictedRemoved")) {
                    AppRestricted app = data.getParcelableExtra("AppRestrictedRemoved");
//                    for(int i = 0;i<mAppRestrictedList.size();++i){
//                        if(mAppRestrictedList.get(i).getPackageName().equals(app.getPackageName())){
//                            mAppRestrictedList.remove(i);
//                        }
//                    }
                    mAppRestrictedAdapter.remove(app);
                    //adapter和list数据同步延迟
                    //mAppRestrictedAdapter.notifyDataSetChanged();
                    Log.i("MainActivity","After remove:adapter.size():"+mAppRestrictedAdapter.getCount()+"list.size():"+mAppRestrictedList.size());
                    try{
                    writeAppRestrictedListToFile();
                    }catch(Exception e){
                        Log.e("MainActivity",e.getMessage());
                    }
                    Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
                    intentToService.putParcelableArrayListExtra("AppRestrictedList", (ArrayList) mAppRestrictedList);
                    intentToService.putExtra("updateAppRestricted", "start");
                    MonitorService.setServiceAlarm(this, true, intentToService);
                }
            }
        } else if (requestCode == ENTER_PASSWORD_TO_DISABLE) {
            if (resultCode == RESULT_OK) {
                Intent intentToService = new Intent(MainActivity.this, MonitorService.class);
                MonitorService.setServiceAlarm(this, false, intentToService);
                Intent i = new Intent(MainActivity.this, EnableActivity.class);
                startActivity(i);
                finish();
            }
        }
    }

    //write app restricted info(include timeLeft) to a json file
    private void writeAppRestrictedListToFile()throws JSONException,IOException{
        JSONArray array = new JSONArray();
        for(AppRestricted app:mAppRestrictedList){
            array.put(app.toJSON());
        }

        Writer writer = null;
        try{
            OutputStream outputStream = MainActivity.this.openFileOutput(APP_RESTRICTED_DATA_FILE_NAME,
                    Context.MODE_PRIVATE);
            writer=new OutputStreamWriter(outputStream);
            writer.write(array.toString());
        }finally{
            if(writer!=null)
                writer.close();
        }
    }
    // read app restricted info(include timeLeft) from a json file
    private void readAppRestrictedListFromFile()throws IOException,JSONException{
        BufferedReader reader=null;
        try{
            InputStream inputStream =MainActivity.this.openFileInput(APP_RESTRICTED_DATA_FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while((line=reader.readLine())!=null){
                jsonString.append(line);
            }
            JSONArray array =(JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            for(int i=0;i<array.length();++i){
                mAppRestrictedList.add(new AppRestricted(array.getJSONObject(i)));
            }
        }catch(FileNotFoundException e){
            //
        }finally {
            if(reader!=null)
                reader.close();
        }
    }
}
