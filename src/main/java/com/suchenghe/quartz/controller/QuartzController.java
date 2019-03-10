package com.suchenghe.quartz.controller;

import com.suchenghe.quartz.service.MyThreadExecutor;
import com.suchenghe.quartz.job.QuartzJob;
import com.suchenghe.quartz.service.QuartzService;
import java.util.Date;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author SuChenghe
 * @date 2018/12/17 23:03
 */
@RestController("quartz_controller")
@RequestMapping("/quartz")
public class QuartzController {
  @Autowired
  QuartzService quartzService;

  @RequestMapping(value = "/doJob", method = RequestMethod.GET)
  public String getList() throws SchedulerException, InterruptedException {
    MyThreadExecutor.myThreadExecutor.execute(() -> {
      try {
        doJob();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });
    return "success";
  }

  private void doJob() throws InterruptedException, SchedulerException {
    Class className = QuartzJob.class;
    String jobName = "JobNameTest";
    String groupName = "JobGroupNameTest";
    String cron = "0/1 * * * * ?";

    quartzService.addCronScheduleJob(className,jobName,groupName, new Date(),cron);

    System.out.println("开始执行......");
    quartzService.start();
    Thread.sleep(3*1000L);

    System.out.println("任务暂停......");
    quartzService.pauseJob(jobName, groupName);
    Thread.sleep(3*1000L);

    System.out.println("立即执行一次......");
    quartzService.doJobRightNow(jobName,groupName);
    Thread.sleep(3*1000L);

    System.out.println("任务是否存在"+quartzService.jobIsExists(jobName,groupName));
    Thread.sleep(3*1000L);

    System.out.println("改成2秒执行一次......");
    quartzService.modifyJobTime(jobName,groupName,"0/2 * * * * ?");
    System.out.println("开始执行......");
    quartzService.resumeJob(jobName,groupName);
    Thread.sleep(6*1000L);

    System.out.println("暂停任务调度器......");
    quartzService.standBy();
    Thread.sleep(3*1000L);

    System.out.println("启动任务调度器......");
    quartzService.start();
    Thread.sleep(3*1000L);

    System.out.println("删除指定任务.....");
    quartzService.removeJob(jobName,groupName);
    System.out.println("删除所有任务.....");
    quartzService.clearAllJob();
//    System.out.println("关闭调度器.....");
//    quartzService.shutdown();
    System.out.println("cron执行结束......");
  }

}
