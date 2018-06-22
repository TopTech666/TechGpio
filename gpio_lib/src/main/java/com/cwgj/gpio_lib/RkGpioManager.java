package com.cwgj.gpio_lib;

import android.os.Gpio;

/**
 * +----------------------------------------------------------------------
 * |  说明     ：
 * +----------------------------------------------------------------------
 * | 创建者   :
 * +----------------------------------------------------------------------
 * | 时　　间 ：2018/6/22 14:02
 * +----------------------------------------------------------------------
 * | 版权所有:
 * +----------------------------------------------------------------------
 **/
public class RkGpioManager {
  /*  1)  当有按钮触发时回调(可传入callback),回调里返回gpio来源（考虑多个gpio监听）
      2)  可设置遍历循环时间  0 -- 150ms
      3）可设置监听的gpio接口(gpio1或gpio2等)
      4）可启动，可停止
      5）设置多长间隔内无法再次触发（暂定10秒）*/

  public static final int NORMAL_GPIO_VALUE = -1;

    //子线程遍历是否在进行
    private boolean isWorking = false;

    //按键检测线程
    private Thread mThread;

    private static RkGpioManager sRkGpioManager;

    private RkGpioManager(){}

    public static RkGpioManager getInstance(){
        if(sRkGpioManager == null){
            synchronized (RkGpioManager.class){
                if(sRkGpioManager == null){
                    sRkGpioManager = new RkGpioManager();
                }
            }
        }
        return sRkGpioManager;
    }

    //默认按键检测间隔30ms ， 按键触发10s内无法触发
    public void startGpioScan(onGpioReceiver onGpioReceiver,  int... gpios ) throws Exception {
          startGpioScan(30, 10*1000 , onGpioReceiver, gpios);
    }

    /**
     *  开启按键检测程序
     * @param scanSpaceMs 按键检测间隔时间  (单位 ms)  10ms - 60ms  注意延时太长按键不灵敏
     * @param invalidTriggerTime  按键触发后多长时间无法触发 （单位 ms）
     * @param onGpioReceiver gpio 按键回调监听
     * @param gpios 需要遍历的gpio  （1， 2， 3， 4， 5 ）
     */
    public void startGpioScan(final int scanSpaceMs, final int invalidTriggerTime, final onGpioReceiver onGpioReceiver, final int... gpios) throws Exception {
        if(scanSpaceMs< 10 || scanSpaceMs>60){
            throw new Exception("invalidTriggerTime is error");
        }
        //设置为工作状态
        isWorking = true;
        for (int i = 0; i < gpios.length; i++) {
            //设置为gpio为读取模式
            Gpio.SetGpioInput(getGpioPinName(gpios[i]));
        }
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (isWorking){
                    try {
                        int params1 = getGpioValue(gpios);
                        if( params1 == NORMAL_GPIO_VALUE){
                            //没有按键按下,延时20ms再检测
                            Thread.sleep(scanSpaceMs);
                        }else {
                            //检测到有按键按下,延时20ms消抖
                            Thread.sleep(scanSpaceMs);
                            int params2 = getGpioValue(gpios);
                            if(params2 == params1){
                                //20ms后仍然是按下,认为是有效按键
                                if(onGpioReceiver!=null)
                                   onGpioReceiver.onGpioReceiver(params2);
                                //间隔一定时间 (10s) 才可以继续触发按键
                                Thread.sleep(invalidTriggerTime);
                                while (getGpioValue(gpios)!= NORMAL_GPIO_VALUE){
                                    //等待按键被释放
                                    Thread.sleep(scanSpaceMs);
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        mThread.start();
    }


    //停止gpio按键检测
    public void stopGpioScan(){
        isWorking = false;
    }

    /**
     * 遍历gpio 引脚电平变化 gpio1 - gpio5
     * @param gpios
     * @return
     */
   private int getGpioValue(int... gpios){
       for (int i = 0; i < gpios.length; i++) {
           //gpio 是从 1 开始, 这里存在问题是，如果同时按下2个按键，只会响应第1个
           int gpioValue = Gpio.GetGpioValue(getGpioPinName(gpios[i]));
           if(gpioValue == 0){
               //有按键按下,返回gpio引脚号
               return gpios[i];
           }
        }
        return -1;
   }



    //gpio名称
    private String getGpioPinName(int gpio){
        return "gpio".concat(String.valueOf(gpio));
    }


    //gpio有效按键被回调
    public interface onGpioReceiver {
        void onGpioReceiver( int gpio);
    }


}
