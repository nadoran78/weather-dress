package com.fighting.weatherdress.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@EnableScheduling
@Configuration
public class SchedulerConfiguration implements SchedulingConfigurer {

  private final int POOL_SIZE = 3;

  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
    ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();

    threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
    threadPoolTaskScheduler.setThreadNamePrefix("scheduling-task");
    threadPoolTaskScheduler.initialize();

    taskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
  }
}
