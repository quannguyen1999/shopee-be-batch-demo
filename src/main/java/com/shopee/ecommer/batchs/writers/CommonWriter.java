package com.shopee.ecommer.batchs.writers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.BatchUtils;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
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

import javax.sql.DataSource;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.shopee.ecommer.constants.ConstantValue.*;

@Configuration
@Slf4j
public class CommonWriter {

    @Autowired
    DataSource dataSource;

    @StepScope
    @Bean
    public JsonFileItemWriter<?> jsonWriter(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        FileSystemResource fileSystemResource = new FileSystemResource(FunctionUtils.getCurrentFilePath(request));
        return new JsonFileItemWriter<>(fileSystemResource, new JacksonJsonObjectMarshaller<>());
    }

    @StepScope
    @Bean
    public FlatFileItemWriter<?> CsvWriter(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);

        FileSystemResource fileSystemResource = new FileSystemResource(FunctionUtils.getCurrentFilePath(request));
        FlatFileItemWriter flatFileItemWriter = new FlatFileItemWriter<>();
        flatFileItemWriter.setResource(fileSystemResource);
        flatFileItemWriter.setHeaderCallback(new FlatFileHeaderCallback() {
            @Override
            public void writeHeader(Writer writer) throws IOException {
                writer.write(BatchUtils.getListFieldName(request));
            }
        });

        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        setDelimiter(ConstantValue.DELIMITER);
                        setNames(BatchUtils.getListFieldByArray(request));
                    }
                });
            }
        });

        return flatFileItemWriter;
    }

    @StepScope
    @Bean
    public JdbcBatchItemWriter jdbcWriter(@Value("#{jobParameters['batchRequest']}") String json) throws JsonProcessingException {
        BatchRequest request = FunctionUtils.mapper.readValue(json, BatchRequest.class);
        List<String> listFields = new ArrayList<>(FunctionUtils.includeCreateUpdate(BatchUtils.getListFieldByCondition(request), request.getTable()));
        String listColumns = FunctionUtils.handlerListToCamelCase(listFields);
        String listValues = listFields.stream().map(t -> "?").collect(Collectors.joining(", "));
        String query = String.join(SPACE, Arrays.asList(INSERT, INTO, FunctionUtils.camelCaseToSnakeCase(request.getTable()),
                OPEN, listColumns, CLOSE, VALUES,
                OPEN, listValues, CLOSE));
        log.info(query);

        JdbcBatchItemWriter jdbcBatchItemWriter = new JdbcBatchItemWriter();
        jdbcBatchItemWriter.setDataSource(dataSource);
        jdbcBatchItemWriter.setSql(query);

        jdbcBatchItemWriter.setItemPreparedStatementSetter(BatchUtils.getStatementByCondition(request));

        return jdbcBatchItemWriter;
    }

}
