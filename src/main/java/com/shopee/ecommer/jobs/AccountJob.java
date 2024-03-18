package com.shopee.ecommer.jobs;

import com.shopee.ecommer.models.Product;
import com.shopee.ecommer.writers.ProductWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Slf4j
@Configuration
public class AccountJob {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private ProductWriter productWriter;


    protected Log logger = LogFactory.getLog(this.getClass());


    //Import Job
    @Bean
    public Job importFromAccount(JobRepository jobRepository,Step firstStepAccount) {
        return new JobBuilder("accountJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(firstStepAccount)
                .build();
    }

    //Define Step
    @Bean
    public Step firstStepAccount(JobRepository jobRepository, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("firstStep", jobRepository)
                .<Product, Product>chunk(10 , transactionManager)
                .reader(accountItemReader(null))
                .writer(productWriter)
                .build();
    }

    //Define Reader
    @StepScope
    @Bean
    public JdbcCursorItemReader<Product> accountItemReader(@Value("#{jobParameters['valueTest']}") String contributionCardCsvFileName) {
        log.info("account");
        log.info(contributionCardCsvFileName);
        JdbcCursorItemReader<Product> jdbcCursorItemReader =
                new JdbcCursorItemReader<Product>();

        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setSql("select id from product");

        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>() {
            {
                setMappedClass(Product.class);
            }
        });

        jdbcCursorItemReader.setCurrentItemCount(2);
        jdbcCursorItemReader.setMaxItemCount(8);

        return jdbcCursorItemReader;
    }

}
