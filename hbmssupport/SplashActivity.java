package com.hbms.hbmssupport;

/**
 * Created by gmorak on 09.09.16.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;

public class SplashActivity extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 1000;//3000;
    private final int pBarMax = 2;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final ProgressBar pBar = (ProgressBar) findViewById(R.id.progress_bar);
        pBar.setMax(pBarMax);
        final Thread pBarThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (progress <= pBarMax) {
                        pBar.setProgress(progress);
                        sleep(300);
                        ++progress;
                    }
                } catch (InterruptedException e) {
                }
            }
        };


        pBarThread.start();

        new Handler().postDelayed(new Runnable() {

         /*
          * Showing splash screen with a timer. This will be useful when you
          * want to show case your app logo / company
          */

            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, HBMSActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);


    }


}