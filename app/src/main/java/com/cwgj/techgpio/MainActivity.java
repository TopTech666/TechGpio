package com.cwgj.techgpio;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cwgj.gpio_lib.RkGpioManager;

public class MainActivity extends AppCompatActivity {
    boolean isContinue = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Thread testThred = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isContinue) {
                    System.out.println("testThred 正在执行");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        System.out.println("是否活着1" + testThred.isAlive());
        testThred.start();
        System.out.println("是否活着2" + testThred.isAlive());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isContinue = false;
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("是否活着3" + testThred.isAlive());

                isContinue = true;
                testThred.start();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("是否活着4" + testThred.isAlive());

            }
        }, 2000);


//        getWindow().getDecorView().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    RkGpioManager.getInstance().init(new RkGpioManager.onGpioReceiver() {
//                        @Override
//                        public void onGpioReceiver(int gpio) {
//                            Log.d("xxxxxxxxxx", " press succ: "+ gpio);
//                            RkGpioManager.getInstance().stopGpioScan();
//                        }
//                    }, 4);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }, 1000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RkGpioManager.getInstance().stopGpioScan();
    }
}
