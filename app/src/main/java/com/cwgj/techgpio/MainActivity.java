package com.cwgj.techgpio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.cwgj.gpio_lib.RkGpioManager;

public class MainActivity extends AppCompatActivity {

    TextView tv_gpio5, tv_gpio4, tv_gpio3, tv_gpio2, tv_gpio1,tv_description;

    int nomalStateColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_gpio5 =  findViewById(R.id.tv_gpio5);
        tv_gpio4 =  findViewById(R.id.tv_gpio4);
        tv_gpio3 =  findViewById(R.id.tv_gpio3);
        tv_gpio2 =  findViewById(R.id.tv_gpio2);
        tv_gpio1 =  findViewById(R.id.tv_gpio1);
        tv_description=  findViewById(R.id.tv_description);
        tv_description.setText("注释:\n1.GPIO正常状态为高电平，按键按下触发为低电平\n2.通话按钮需要连接到GPIO4");

        nomalStateColor = getResources().getColor(R.color.color_blue);

        try {
            RkGpioManager.getInstance().initData(30, 1000,10, new RkGpioManager.onGpioReceiver() {
                @Override
                public void onGpioReceiver(final int gpio) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(gpio == 5){
                                tv_gpio5.setText("测试成功");
                                tv_gpio5.setTextColor(nomalStateColor);
                            }else if(gpio == 4){
                                tv_gpio4.setText("测试成功");
                                tv_gpio4.setTextColor(nomalStateColor);
                            }else if(gpio == 3){
                                tv_gpio3.setText("测试成功");
                                tv_gpio3.setTextColor(nomalStateColor);
                            }else if(gpio == 2){
                                tv_gpio2.setText("测试成功");
                                tv_gpio2.setTextColor(nomalStateColor);
                            }else if(gpio == 1){
                                tv_gpio1.setText("测试成功");
                                tv_gpio1.setTextColor(nomalStateColor);
                            }
                        }
                    });
                }
            }, 1, 2, 3, 4, 5);
            RkGpioManager.getInstance().startGPIOScan();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        final Thread testThred = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (isContinue) {
//                    System.out.println("testThred 正在执行");
//                    try {
//                        Thread.sleep(1000);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        System.out.println("是否活着1" + testThred.isAlive());
//        testThred.start();
//        System.out.println("是否活着2" + testThred.isAlive());
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                isContinue = false;
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("是否活着3" + testThred.isAlive());
//
//                isContinue = true;
//                testThred.start();
//                try {
//                    Thread.sleep(2000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("是否活着4" + testThred.isAlive());
//
//            }
//        }, 2000);


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
        RkGpioManager.getInstance().stopGpioScan();
    }


}
