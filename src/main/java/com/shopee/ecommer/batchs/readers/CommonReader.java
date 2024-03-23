package com.shopee.ecommer.batchs.readers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.BatchUtils;
import com.shopee.ecommer.utils.FunctionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;
import java.io.File;

@Configuration
public class CommonReader {

    @Autowired
    private DataSource dataSource;

    @StepScope
    @Bean
    public JdbcCursorItemReader<?> commonItemReader(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        JdbcCursorItemReader<Object> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setSql(FunctionUtils.getQueryIncludeCreateUpdated(BatchUtils.getListFieldByCondition(request), request.getTable()));
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>() {
            {
                setMappedClass(BatchUtils.getClassByCondition(request));
            }
        });
        jdbcCursorItemReader.setDataSource(dataSource);
//        jdbcCursorItemReader.setCurrentItemCount(2);
//        jdbcCursorItemReader.setMaxItemCount(8);
        return jdbcCursorItemReader;
    }

    @StepScope
    @Bean
    public FlatFileItemReader flatFileItemReader(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        FlatFileItemReader flatFileItemReader = new FlatFileItemReader();
        flatFileItemReader.setResource(new FileSystemResource(new File(FunctionUtils.getCurrentFilePath(request))));
        flatFileItemReader.setLineMapper(new DefaultLineMapper() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setDelimiter(ConstantValue.DELIMITER);
                        setNames(BatchUtils.getListFieldByArray(request));
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper() {
                    {

                        setTargetType(BatchUtils.getClassByCondition(request));
                    }
                });
            }
        });

        flatFileItemReader.setLinesToSkip(1);

        return flatFileItemReader;
    }

}
