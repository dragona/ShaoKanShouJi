package com.shaokanshouji.zwj.App;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences mSharedPreferences;

    public static final String EXTRA_WHO_CALL_ME = "com.ShaoKanShouJi.zwj.App";
    private String mWho;

    @Override
    public void onCreate(final Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_enter_password);
        mWho = getIntent().getStringExtra(EXTRA_WHO_CALL_ME);

        mTextViewEnterPassword = findViewById(R.id.tv_enter_password);
        mEditTextVerifyPassword = findViewById(R.id.et_verify_password);
        mButtonVerifyPassword = findViewById(R.id.btn_verify_password);
        mSharedPreferences = getSharedPreferences("ShaoKanShouJi", Context.MODE_PRIVATE);

        switch (mWho) {
            case "MainAct":
                mTextViewEnterPassword.setText(R.string.to_disable_restriction);
                break;
            case "RestrictionDetailActToModify":
                mTextViewEnterPassword.setText("please enter password to modify restriction");
                break;
            case "RestrictionDetailActToRemove":
                mTextViewEnterPassword.setText("please enter password to remove restriction");
                break;
            default:
                break;
        }

        mButtonVerifyPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mSharedPreferences.edit().clear().commit();
                String enterPassword = mEditTextVerifyPassword.getText().toString();
                String password = mSharedPreferences.getString("password", "");
                if (password.equals(enterPassword)) {
                    Toast.makeText(EnterPasswordActivity.this, "password correct", Toast.LENGTH_LONG).show();
                    Toast.makeText(EnterPasswordActivity.this, mWho, Toast.LENGTH_LONG).show();
                    switch (mWho) {
                        case "MainAct":
                            mSharedPreferences.edit().remove("password").commit();
                            mSharedPreferences.edit().remove("IsEnabled").commit();
                            Intent intent = new Intent();
                            setResult(RESULT_OK, intent);
                            finish();
                            break;
                        case "RestrictionDetailActToModify":
                            Intent i = new Intent();
                            setResult(RESULT_OK, i);
                            finish();
                            break;
                        case "RestrictionDetailActToRemove":
                            Intent in = new Intent();
                            setResult(RESULT_OK, in);
                            finish();
                            break;
                        default:
                            ;
                            break;
                    }
                } else {
                    Toast.makeText(EnterPasswordActivity.this, "password incorrect", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
