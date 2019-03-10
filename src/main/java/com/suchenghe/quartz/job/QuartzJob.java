package com.suchenghe.quartz.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author SuChenghe
 * @DisallowConcurrentExecution:禁止并发执行
 * 默认情况下,当Job执行时间超过间隔时间时,调度框架为了让人物按照我们预定的时间间隔执行,会马上启用新的线程来执行任务
 */
@DisallowConcurrentExecution
@Component
public class QuartzJob implements Job{

    Logger LOGGER = LoggerFactory.getLogger(QuartzJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        String jobName = jobDetail.getKey().getName();
        String jobGroup = jobDetail.getKey().getGroup();
        Class jobClass = jobDetail.getKey().getClass();

        Trigger trigger = jobExecutionContext.getTrigger();

        LOGGER.error("QuartzJob任务执行....");
    }

}
