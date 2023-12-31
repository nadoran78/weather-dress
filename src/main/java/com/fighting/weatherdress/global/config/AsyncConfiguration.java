package com.fighting.weatherdress.global.config;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfiguration {

  @Bean(name = "threadPoolTaskExecutor")
  public Executor threadPoolTaskExecutor() {

    ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

    final int CORE_POOL_SIZE = 3;
    final int MAX_POOL_SIZE = 10;
    final int QUEUE_CAPACITY = 100_000;

    taskExecutor.setCorePoolSize(CORE_POOL_SIZE);
    taskExecutor.setMaxPoolSize(MAX_POOL_SIZE);
    taskExecutor.setQueueCapacity(QUEUE_CAPACITY);
    taskExecutor.setThreadNamePrefix("Executor-");

    return taskExecutor;
  }

}
