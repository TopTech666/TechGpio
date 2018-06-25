package com.cwgj.techgpio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cwgj.gpio_lib.RkGpioManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    RkGpioManager.getInstance().startGpioScan( new RkGpioManager.onGpioReceiver() {
                        @Override
                        public void onGpioReceiver(int gpio) {
                            Log.d("xxxxxxxxxx", " press succ: "+ gpio);
                            RkGpioManager.getInstance().stopGpioScan();
                        }
                    }, 4);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RkGpioManager.getInstance().stopGpioScan();
    }
}
