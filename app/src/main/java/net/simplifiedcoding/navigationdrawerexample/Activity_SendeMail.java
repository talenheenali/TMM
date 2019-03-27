package net.simplifiedcoding.navigationdrawerexample;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

/**
 * Created by Heenali on 26/3/2019.
 */

public class Activity_SendeMail extends AppCompatActivity {

    ImageView back_btn;
    Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendemail);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(Activity_SendeMail.this, R.color.appcolor2));
        }
        back_btn=(ImageView)findViewById(R.id.back_btn);
        btn_submit=(Button) findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent;
                intent = new Intent(getApplicationContext(), Activity_passcreate.class);
                startActivity(intent);
                finish();
            }
        });
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

