package com.shopee.ecommer.batchs.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.batchs.listeners.CommonListener;
import com.shopee.ecommer.batchs.readers.CommonReader;
import com.shopee.ecommer.batchs.writers.CommonWriter;
import com.shopee.ecommer.batchs.writers.LogItemWriter;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class CommonStep {

    @Autowired
    CommonReader commonReader;

    @Autowired
    CommonWriter commonWriter;

    @Autowired
    LogItemWriter logItemWriter;

    @Autowired
    CommonListener commonListener;


    @Bean
    public Step stepSqlToJsonCommon(JobRepository jobRepository,
                                    DataSourceTransactionManager transactionManager
    ) throws JsonProcessingException {
        return new StepBuilder("stepSqlToJsonCommon", jobRepository)
                .chunk(10, transactionManager)
                .reader(commonReader.commonItemReader(null))
                .writer((ItemWriter<? super Object>) commonWriter.jsonWriter(null))
                .build();
    }


    @Bean
    public Step stepSqlToCsvCommon(JobRepository jobRepository,
                                   DataSourceTransactionManager transactionManager
    ) throws JsonProcessingException {
        return new StepBuilder("stepSqlToCsvCommon", jobRepository)
                .chunk(10, transactionManager)
                .reader(commonReader.commonItemReader(null))
                .writer((ItemWriter<? super Object>) commonWriter.CsvWriter(null))
                .build();
    }

    @Bean
    public Step stepFileToSqlCommon(JobRepository jobRepository,
                                    DataSourceTransactionManager transactionManager
    ) throws JsonProcessingException {
        return new StepBuilder("stepFileToSqlCommon", jobRepository)
                .chunk(2, transactionManager)
                .reader(commonReader.flatFileItemReader(null))
                .writer(commonWriter.jdbcWriter())
                .faultTolerant()
                .skip(Throwable.class)
                .skipPolicy(new AlwaysSkipItemSkipPolicy())
                .listener(commonListener)
                .build();
    }


}
