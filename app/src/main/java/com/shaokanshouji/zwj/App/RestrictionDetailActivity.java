package com.shaokanshouji.zwj.App;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

/**
 * Created by zwj on 2018/3/31.
 */

public class RestrictionDetailActivity extends AppCompatActivity {
    private Spinner mSpinnerDurationStart, mSpinnerDurationEnd,
            mSpinnerTimeHour, mSpinnerTimeMinute;
    private TextView mTextViewAppName, mTextViewTimeLeft,mTextViewTimeLeftValue;
    private Button mButtonModify, mButtonRemove;
    private AppRestricted mAppRestricted;
    private final int ENTER_PASSWORD_TO_MODIFY = 0;
    private final int ENTER_PASSWORD_TO_REMOVE = 1;
    private Pair<Integer,Integer> timeStringConvert(String time)
    {
        Pair<Integer,Integer> result = new Pair<Integer, Integer>(Integer.parseInt(time.substring(0,2)),
                Integer.parseInt(time.substring(3,5)));
        return result;
    }
    //get AppRestricted var after modified
    private AppRestricted getAppRestricted()
    {
        AppRestricted appRestricted = new AppRestricted();
        //!!!
        appRestricted.setPackageName(mAppRestricted.getPackageName());
        appRestricted.setName(mAppRestricted.getName());
        Pair<Integer,Integer> durationStart = timeStringConvert(mSpinnerDurationStart.getSelectedItem().toString());
        appRestricted.setDurationStart(new Time(durationStart.first,durationStart.second));
        Pair<Integer,Integer> durationEnd = timeStringConvert(mSpinnerDurationEnd.getSelectedItem().toString());
        appRestricted.setDurationEnd(new Time(durationEnd.first,durationEnd.second));
        appRestricted.setTimeAvailableSum(new Time(Integer.parseInt(mSpinnerTimeHour.getSelectedItem().toString()),
                Integer.parseInt(mSpinnerTimeMinute.getSelectedItem().toString())));
        appRestricted.setTimeLeft(appRestricted.getTimeAvailableSum());
        return appRestricted;
    }
    @Override
    public void onCreate(Bundle saveInstanceState) {
        Toast.makeText(RestrictionDetailActivity.this, "RestrictionDetailActivity onCreate", Toast.LENGTH_LONG).show();
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_restriction_detail);
        init();
        mAppRestricted = getIntent().getParcelableExtra("AppDetail");
        initAndDisableSpinners();
        mButtonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RestrictionDetailActivity.this, EnterPasswordActivity.class);
                i.putExtra(EnterPasswordActivity.EXTRA_WHO_CALL_ME, "RestrictionDetailActToModify");
                startActivityForResult(i, ENTER_PASSWORD_TO_MODIFY);
                //Toast.makeText(RestrictionDetailActivity.this,"RestrictionDetailActivity onModifyClick",Toast.LENGTH_LONG).show();
                //if(getIntent().getBooleanExtra("Password",false)){
                //enableSpinners();
                //}
            }
        });
        mButtonRemove.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(RestrictionDetailActivity.this, EnterPasswordActivity.class);
                i.putExtra(EnterPasswordActivity.EXTRA_WHO_CALL_ME, "RestrictionDetailActToRemove");
                startActivityForResult(i, ENTER_PASSWORD_TO_REMOVE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ENTER_PASSWORD_TO_MODIFY) {
            if(resultCode==RESULT_OK){
                enableSpinners();
                changeUI();
            }
        } else if (requestCode == ENTER_PASSWORD_TO_REMOVE) {
            if (resultCode == RESULT_OK) {
                Intent i = new Intent();
                i.putExtra("AppRestrictedRemoved",getAppRestricted());
                setResult(RESULT_OK,i);
                finish();
            }
        }
    }

    private void init() {
        mSpinnerDurationStart = findViewById(R.id.sp_duration_start_rd);
        mSpinnerDurationEnd = findViewById(R.id.sp_duration_end_rd);
        mSpinnerTimeHour = findViewById(R.id.sp_time_hour_rd);
        mSpinnerTimeMinute = findViewById(R.id.sp_time_minute_rd);
        mTextViewAppName = findViewById(R.id.tv_app_name_rd);
        mTextViewTimeLeft = findViewById(R.id.tv_time_left);
        mTextViewTimeLeftValue = findViewById(R.id.tv_time_left_value);
        mButtonModify = findViewById(R.id.btn_modify);
        mButtonRemove = findViewById(R.id.btn_remove);
    }

    private void initAndDisableSpinners() {
        mTextViewAppName.setText(mAppRestricted.getName());
        mSpinnerDurationStart.setSelection(mAppRestricted.getDurationStart().getHour() - 1);
        mSpinnerDurationStart.setEnabled(false);
        mSpinnerDurationEnd.setSelection(mAppRestricted.getDurationEnd().getHour() - 1);
        mSpinnerDurationEnd.setEnabled(false);
        mSpinnerTimeHour.setSelection(mAppRestricted.getTimeAvailableSum().getHour());
        mSpinnerTimeHour.setEnabled(false);
        mSpinnerTimeMinute.setSelection(mAppRestricted.getTimeAvailableSum().getMinute()/10);
        mSpinnerTimeMinute.setEnabled(false);
        mTextViewTimeLeftValue.setText(mAppRestricted.getTimeLeft().getHour() + "h" + mAppRestricted.getTimeLeft().getMinute() + "m");
    }

    private void enableSpinners() {
        mSpinnerDurationStart.setEnabled(true);
        mSpinnerDurationEnd.setEnabled(true);
        mSpinnerTimeHour.setEnabled(true);
        mSpinnerTimeMinute.setEnabled(true);
    }

    private void changeUI(){
        mButtonRemove.setVisibility(View.GONE);
        mTextViewTimeLeft.setVisibility(View.GONE);
        mTextViewTimeLeftValue.setVisibility(View.GONE);
        mButtonModify.setText("done");
        mButtonModify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent();
                i.putExtra("AppRestrictedModified",getAppRestricted());
                setResult(RESULT_OK,i);
                RestrictionDetailActivity.this.finish();
            }
        });
    }
}
