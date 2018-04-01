package com.shaokanshouji.zwj.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by zwj on 2018/3/31.
 */

public class LaunchActivity extends Activity{
    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_launch);
        new Thread(new Runnable(){
            @Override
            public void run()
            {
                //
                SharedPreferences sharedPreferences = getSharedPreferences("ShaoKanShouJi", Context.MODE_PRIVATE);
                final boolean isEnabled = sharedPreferences.getBoolean("IsEnabled",false);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Integer time = 2000;
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run(){
                                Intent i;
                                if(!isEnabled) {
                                    i = new Intent(LaunchActivity.this, EnableActivity.class);
                                }
                                else
                                {
                                    i = new Intent(LaunchActivity.this,MainActivity.class);
                                }
                                startActivity(i);
                                LaunchActivity.this.finish();
                            }
                        },time);
                    }
                });
            }
        }).start();
    }
}
