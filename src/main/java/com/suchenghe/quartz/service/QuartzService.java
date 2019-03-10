package com.suchenghe.quartz.service;

import java.util.Date;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author SuChenghe
 * @date 2018/6/26 14:29
 */
@Service
public class QuartzService {

    @Autowired
    @Qualifier(value = "scheduler")
    private Scheduler scheduler;

    /**
     *   "0 0 10 * * ?"  每天10点运行
     *   "0 15 10 * * ?"  每天10点15运行
     *   "0 15 10 * * ? 2017"  2017年的每天10点15运行
     *   "0 * 10 * * ?" 每天的10点的每分钟运行一次,开始于10:00,结束于10:59
     *   "0 0/5 10 * * ?" 每天的10点的的每5分钟运行一次,开始于10:00,结束于10:55
     *   "0 0/5 10,12 * * ?" 每天的10点和12点的每5分钟运行一次
     *   "0 0-5 10 * * ?" 每天10:00到10:05,每分钟运行一次
     *   "0 10,44 10 ? 3 WED" 三月份的每周三的10点10分和44分,分别运行一次
     *   "0 15 10 ? * MON-FRI" 每月的周一到周五的每天10点15分运行一次
     *   "0 15 10 15 * ?" 每月15号的10点15分执行一次
     *   "0 15 10 L * ?" 每月的最后一天的10:15执行一次
     *   "0 15 10 ? * 6L" 每月最后一个星期五的10:15执行一次
     *   "0 15 10 ? * 6L 2017-2018" 2017年至2018年每月最后一个星期五的10:15执行一次
     *   "0 15 10 ? * 6#3" 每个月第三个星期五的10:15执行一次
     */
    public boolean addCronScheduleJob(Class className, String jobName, String groupName, Date startTime, String cron) {

        JobDetail jobDetail = JobBuilder.newJob(className).withIdentity(jobName,groupName).build();

        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobName, groupName).startAt(startTime)
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();

        try {
            scheduler.scheduleJob(jobDetail, cronTrigger);
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * rateState:执行周期的单位
     * rate:执行周期的时间间隔
     * count:执行的次数(如果为-1,则forever)
     * @return
     */
    public boolean addSimpleScheduleJob(Class className, String jobName, String groupName, Date startTime,String rateState,int rate,int count) {
        JobDetail jobDetail = JobBuilder.newJob(className).withIdentity(jobName,groupName).build();
        SimpleScheduleBuilder simpleScheduleBuilder = null;

        if(rateState!=null&&rate>0&&(count>0||count==-1)){
            if(rateState.equals("seconds")){
                if(count==-1){
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(rate).repeatForever();
                }else {
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(rate).withRepeatCount(count-1);
                }
            }if(rateState.equals("minute")){
                if(count==-1){
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(rate).repeatForever();
                }else{
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInMinutes(rate).withRepeatCount(count-1);
                }
            }if(rateState.equals("hour")){
                if(count==-1){
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(rate).repeatForever();
                }else{
                    simpleScheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInHours(rate).withRepeatCount(count-1);
                }
            }
            if(simpleScheduleBuilder!=null){
                SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger().withIdentity(jobName,groupName).startAt(startTime)
                        .withSchedule(simpleScheduleBuilder).build();
                try {
                    scheduler.scheduleJob(jobDetail,simpleTrigger);
                    return true;
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
        return  false;

    }

    /**
     * 临时中止，可start();shutdown()以后，不可以再start();
     * @return
     */
    public boolean standBy() {
        try {
            scheduler.standby();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean shutdown() {
        try {
            scheduler.shutdown();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean clearAllJob() {
        try {
            scheduler.clear();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * start();
     * @return
     */
    public boolean start() {
        try {
            scheduler.start();
            return true;
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean resumeJob(String jobId, String groupId) throws SchedulerException {
        scheduler.resumeJob(new JobKey(jobId,groupId));
        return true;
    }

    public boolean pauseJob(String jobId, String groupId) throws SchedulerException {
        scheduler.pauseJob(new JobKey(jobId, groupId));
        return true;
    }

    public boolean pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
        return true;
    }

    public boolean doJobRightNow(String jobId, String groupId) throws SchedulerException {
        scheduler.triggerJob(new JobKey(jobId,groupId));
        return true;
    }

    public boolean removeJob(String jobId, String groupId) throws SchedulerException {
        scheduler.deleteJob(new JobKey(jobId, groupId));
        return true;
    }

    public boolean jobIsExists(String jobId, String groupId) throws SchedulerException {
        return scheduler.checkExists(new JobKey(jobId, groupId));
    }


    public boolean modifyJobTime(String jobId,String groupId,String cron) throws SchedulerException {
        CronTrigger cronTrigger = TriggerBuilder.newTrigger().withIdentity(jobId,groupId).startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule(cron)).build();
        scheduler.rescheduleJob(new TriggerKey(jobId,groupId),cronTrigger);
        return true;
    }

}
