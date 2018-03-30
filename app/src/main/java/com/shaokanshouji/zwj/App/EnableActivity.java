package com.shaokanshouji.zwj.App;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EnableActivity extends AppCompatActivity {

    private Button mButtonEnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enable);

        mButtonEnable = findViewById(R.id.btn_enable_restriction);
        mButtonEnable.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(EnableActivity.this,SetPasswordActivity.class);
                startActivity(i);
            }
        });
    }
}
