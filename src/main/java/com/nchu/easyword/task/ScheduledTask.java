package com.nchu.easyword.task;

import com.nchu.easyword.service.impl.SpiderService;
import com.nchu.easyword.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author xujw
 * 2018-5-7 15:40:43
 * 定时任务相关类
 */
@Component
public class ScheduledTask {
    @Autowired
    SpiderService spiderService;
    /*新闻相关定时任务,每天凌晨5点自动抓取当日新闻*/

    /**
     * Cron表达式是一个字符串，是由空格隔开的6或7个域组成，
     * 每一个域对应一个含义（秒 分 时 每月第几天 月 星期 年）其中年是可选字段,spring的schedule值不能设置年
     */
    @Scheduled(cron = "${newsTaskTime}")
    public void newsTask() {
        System.out.println("定时任务执行");
        try {
            spiderService.startCrawl(DateUtil.getTodayString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
