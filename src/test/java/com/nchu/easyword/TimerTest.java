package com.nchu.easyword;

import java.util.Timer;
import java.util.TimerTask;

public class TimerTest extends TimerTask {
    private String name;
    //时间间隔(一天)
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;

    public TimerTest(String inputName) {
        name = inputName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        System.out.println("我的Timer名字是：" + name);
    }

    public static void main(String[] args) {
        //创建Timer实例
        Timer timer = new Timer();
        //创建TimerTask实例
        TimerTest myTimerTask = new TimerTest("No.1");
        //t通过timer定时定频率调用myTimerTask的业务逻辑
        //当前时间的2秒钟之后每隔一秒钟执行一次
        timer.schedule(myTimerTask, 2000L, 1000L);
    }
}
