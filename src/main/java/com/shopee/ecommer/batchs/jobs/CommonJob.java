package com.shopee.ecommer.batchs.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;

@Slf4j
@Configuration
public class CommonJob {

    private static final String FILE_PATH = "src/main/resources/static/response/";
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

    //Define Step
    @Bean
    public Step stepSqlToJsonCommon(JobRepository jobRepository,
                                    DataSourceTransactionManager transactionManager
    ) throws JsonProcessingException {
        return new StepBuilder("stepSqlToJsonCommon", jobRepository)
                .chunk(10, transactionManager)
                .reader(commonItemReader(null))
                .writer((ItemWriter<? super Object>) jsonWriter(null))
                .build();
    }

    @Bean
    public Step stepSqlToCsvCommon(JobRepository jobRepository,
                                   DataSourceTransactionManager transactionManager
    ) throws JsonProcessingException {
        return new StepBuilder("stepSqlToCsvCommon", jobRepository)
                .chunk(10, transactionManager)
                .reader(commonItemReader(null))
                .writer((ItemWriter<? super Object>) CsvWriter(null))
                .build();
    }

    //Define Reader
    private String getQuery(BatchRequest request) {
        String sql = null;
        if (request.getTable().equalsIgnoreCase(Product.class.getSimpleName())) {
            sql = FunctionUtils.getQueryIncludeCreateUpdated(ConstantValue.LIST_FIELD_PRODUCT, request.getTable());
        } else if (request.getTable().equalsIgnoreCase(Category.class.getSimpleName())) {
            sql = FunctionUtils.getQueryIncludeCreateUpdated(ConstantValue.LIST_FIELD_CATEGORY, request.getTable());
        }
        return sql;
    }

    private Class getCurrentClass(BatchRequest request) {
        if (request.getTable().equalsIgnoreCase(Product.class.getSimpleName())) {
            return Product.class;
        }
        if (request.getTable().equalsIgnoreCase(Category.class.getSimpleName())) {
            return Category.class;
        }
        return null;
    }

    @StepScope
    @Bean
    public JdbcCursorItemReader<?> commonItemReader(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        JdbcCursorItemReader<Object> jdbcCursorItemReader = new JdbcCursorItemReader<>();
        jdbcCursorItemReader.setSql(getQuery(request));
        jdbcCursorItemReader.setRowMapper(new BeanPropertyRowMapper<>() {
            {
                setMappedClass(getCurrentClass(request));
            }
        });
        jdbcCursorItemReader.setDataSource(dataSource);
        jdbcCursorItemReader.setCurrentItemCount(2);
        jdbcCursorItemReader.setMaxItemCount(8);
        return jdbcCursorItemReader;
    }


    @StepScope
    @Bean
    public JsonFileItemWriter<?> jsonWriter(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        FileSystemResource fileSystemResource = new FileSystemResource(getCurrentFilePath(request));
        return new JsonFileItemWriter<>(fileSystemResource, new JacksonJsonObjectMarshaller<>());
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<?> CsvWriter(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);

        FileSystemResource fileSystemResource = new FileSystemResource(getCurrentFilePath(request));
        FlatFileItemWriter<Product> flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(fileSystemResource);
        flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                if (Product.class.getSimpleName().equalsIgnoreCase(request.getTable())) {
                    log.info(String.join(",", FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_PRODUCT)));
                    writer.write(String.join(",", FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_PRODUCT)));
                }
            }
        });

        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setNames(FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_PRODUCT).toArray(new String[0]));
                    }
                });
            }
        });

        return flatFileItemWriter;
    }

    private String getCurrentFilePath(BatchRequest request) {
        StringBuilder path = new StringBuilder();
        path.append(FILE_PATH).append(request.getTypeFile()).append("/")
                .append(request.getTable()).append(".").append(request.getTypeFile());
        log.info(path.toString());
        return path.toString();
    }


}
