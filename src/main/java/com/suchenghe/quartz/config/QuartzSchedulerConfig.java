package com.suchenghe.quartz.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author SuChenghe
 * @date 2018/12/17 22:58
 */
@Configuration
public class QuartzSchedulerConfig {

  @Bean(name = "scheduler")
  public Scheduler getScheduler() {
    StdSchedulerFactory sf = new StdSchedulerFactory();
    try {
      return sf.getScheduler();
    } catch (SchedulerException e) {
      return null;
    }
  }
}
