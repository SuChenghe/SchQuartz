package com.suchenghe.quartz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SchprojectApplicationJar extends SpringBootServletInitializer {

  public static void main(String[] args) {
    SpringApplication.run(SchprojectApplicationJar.class,args);
    System.out.println("启动成功");
  }
}
