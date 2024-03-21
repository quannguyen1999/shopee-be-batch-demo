package com.shopee.ecommer.batchs.jobs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class CommonJob {
    
    @Autowired
    private DataSource dataSource;

    //Import Job
    @Bean
    public Job sqlToJsonJob(JobRepository jobRepository, Step stepSqlToJsonCommon) {
        return new JobBuilder("sqlToJsonJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepSqlToJsonCommon)
                .build();
    }

    @Bean
    public Job sqlToCsvJob(JobRepository jobRepository, Step stepSqlToCsvCommon) {
        return new JobBuilder("sqlToCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepSqlToCsvCommon)
                .build();
    }

    @Bean
    public Job fileToJsonJob(JobRepository jobRepository, Step stepFileToSqlCommon) {
        return new JobBuilder("fileToJsonJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(stepFileToSqlCommon)
                .build();
    }

}
