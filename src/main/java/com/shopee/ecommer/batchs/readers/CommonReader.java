package com.shopee.ecommer.batchs.readers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.FunctionUtils;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Configuration
public class CommonReader {

    @Autowired
    private DataSource dataSource;

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
}
