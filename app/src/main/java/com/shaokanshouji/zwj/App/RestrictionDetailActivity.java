package com.shaokanshouji.zwj.App;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by zwj on 2018/3/31.
 */

public class RestrictionDetailActivity extends AppCompatActivity{
    private Spinner mSpinnerDurationStart, mSpinnerDurationEnd,
            mSpinnerTimeHour, mSpinnerTimeMinute;
    private TextView mTextViewAppName,mTextViewTimeLeft;
    private Button mButtonModify,mButtonRemove;
    @Override
    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_restriction_detail);
        init();
        mSpinnerDurationStart.setSelection(3);
        mSpinnerDurationStart.setEnabled(false);
    }
    private void init()
    {
        mSpinnerDurationStart = findViewById(R.id.sp_duration_start_rd);
        mSpinnerDurationEnd = findViewById(R.id.sp_duration_end_rd);
        mSpinnerTimeHour = findViewById(R.id.sp_time_hour_rd);
        mSpinnerTimeMinute = findViewById(R.id.sp_time_minute_rd);
        mTextViewAppName = findViewById(R.id.tv_app_name_rd);
        mTextViewTimeLeft = findViewById(R.id.tv_time_left);
        mButtonModify = findViewById(R.id.btn_modify);
        mButtonRemove = findViewById(R.id.btn_remove);
    }
}
