# TechGpio
rk主板gpio触发管理

//开启gpio 按键检测
   RkGpioManager.getInstance().startGpioScan(50, 0, new RkGpioManager.onGpioReceiver() {
                        @Override
                        public void onGpioReceiver(int gpio) {
                            Log.d("xxxxxxxxxx", " press succ: "+ gpio);
                        }
                    }, 4);


//关闭按键检测
RkGpioManager.getInstance().stopGpioScan();
