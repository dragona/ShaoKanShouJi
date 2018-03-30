package com.shaokanshouji.zwj.App;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by zwj on 2018/3/30.
 */

public class EnterPasswordActivity extends AppCompatActivity {
    private TextView mTextViewEnterPassword;
    private EditText mEditTextVerifyPassword;
    private Button mButtonVerifyPassword;

    public static final String EXTRA_WHO_CALL_ME ="com.ShaoKanShouJi.zwj.App";
    private String who;

    @Override
    public void onCreate(final Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_enter_password);
        who = getIntent().getStringExtra(EXTRA_WHO_CALL_ME);

        mTextViewEnterPassword = findViewById(R.id.tv_enter_password);
        mEditTextVerifyPassword = findViewById(R.id.et_verify_password);
        mButtonVerifyPassword = findViewById(R.id.btn_verify_password);

        switch(who)
        {
            case "MainActivity":
                mTextViewEnterPassword.setText(R.string.to_disable_restriction);
                break;
            default:
                break;
        }

        mButtonVerifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EnterPasswordActivity.this,who,Toast.LENGTH_LONG).show();
                switch(who)
                {
                    case "MainActivity":
                        ;
                        break;
                    default:
                        ;
                        break;
                }
            }
        });
    }
}
