package com.shopee.ecommer.batchs.writers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.batchs.readers.CommonReader;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.FunctionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileHeaderCallback;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.json.JacksonJsonObjectMarshaller;
import org.springframework.batch.item.json.JsonFileItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.io.IOException;
import java.io.Writer;

@Configuration
@Slf4j
public class CommonWriter extends CommonReader {

    private static final String FILE_PATH = "src/main/resources/static/response/";


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
                String listFieldSplitByComma = null;
                if (Product.class.getSimpleName().equalsIgnoreCase(request.getTable())) {
                    listFieldSplitByComma = String.join(",", FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_PRODUCT));
                }
                if (Category.class.getSimpleName().equalsIgnoreCase(request.getTable())) {
                    listFieldSplitByComma = String.join(",", FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_CATEGORY));
                }
                assert listFieldSplitByComma != null;
                writer.write(listFieldSplitByComma);

            }
        });

        flatFileItemWriter.setLineAggregator(new DelimitedLineAggregator<>() {
            {
                setFieldExtractor(new BeanWrapperFieldExtractor<>() {
                    {
                        String[] listFieldSplitByComma = null;
                        if (Product.class.getSimpleName().equalsIgnoreCase(request.getTable())) {
                            listFieldSplitByComma = FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_PRODUCT).toArray(new String[0]);
                        }
                        if (Category.class.getSimpleName().equalsIgnoreCase(request.getTable())) {
                            listFieldSplitByComma = FunctionUtils.includeCreateUpdate(ConstantValue.LIST_FIELD_CATEGORY).toArray(new String[0]);
                        }
                        assert listFieldSplitByComma != null;
                        setNames(listFieldSplitByComma);
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
