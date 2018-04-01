package com.shaokanshouji.zwj.App;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by zwj on 2018/3/29.
 */

public class SetPasswordActivity extends AppCompatActivity {
    private EditText mEditTextPassword1, mEditTextPassword2;
    private Button mButtonDone;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        mButtonDone = findViewById(R.id.btn_set_password);
        mEditTextPassword1 = findViewById(R.id.et_password);
        mEditTextPassword2 = findViewById(R.id.et_password_again);
        mSharedPreferences = getSharedPreferences("ShaoKanShouJi", Context.MODE_PRIVATE);
        //sharedPreferencesIsEnabled.edit().putBoolean("IsEnabled",false).commit();

        mButtonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password1 = mEditTextPassword1.getText().toString();
                String password2 = mEditTextPassword2.getText().toString();
                if (password1.equals("")) {
                    Toast.makeText(SetPasswordActivity.this, getString(R.string.no_input), Toast.LENGTH_LONG).show();
                } else if (password1.equals(password2)) {
                    Toast.makeText(SetPasswordActivity.this, getString(R.string.set_password_successfully),
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(SetPasswordActivity.this,MainActivity.class);
                    mSharedPreferences.edit().putString("password",password1).putBoolean("IsEnabled",true).commit();
                    startActivity(i);
                } else {
                    Toast.makeText(SetPasswordActivity.this, getString(R.string.passwords_inconsistent)
                            + "\n" + password1 + "--" + password2, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
