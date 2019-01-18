package com.example.pwallet.peditywallet;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by pwallet on 6/23/2018.
 */

public class FrontPage extends AppCompatActivity {
    private static int TIME_TO_DISPLAY = 500;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frontpage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(MySingleTon.getInstance().existingKey("SecretKeyAlias_PEDITY6")) {
                    intent = new Intent(FrontPage.this, MainActivity.class);
                }
                else {
                    intent = new Intent(FrontPage.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();
            }
        }, TIME_TO_DISPLAY);
    }
}
