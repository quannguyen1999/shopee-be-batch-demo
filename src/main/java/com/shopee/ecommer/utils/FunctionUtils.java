package com.shopee.ecommer.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.request.BatchRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.shopee.ecommer.constants.ConstantValue.*;

@Slf4j
public class FunctionUtils {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    //Convert Object to Json
    public static final ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

    //Convert Json To Object
    public static final ObjectMapper mapper = new ObjectMapper();

    //IsActive -> is_active
    public static String camelCaseToSnakeCase(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(input.charAt(0)));

        for (int i = 1; i < input.length(); i++) {
            char currentChar = input.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                result.append('_').append(Character.toLowerCase(currentChar));
            } else {
                result.append(currentChar);
            }
        }

        return result.toString();
    }

    //Convert List
    public static String handlerListToCamelCase(List<String> list) {
        if (ObjectUtils.isEmpty(list)) {
            return null;
        }
        return list.stream().map(FunctionUtils::camelCaseToSnakeCase).collect(Collectors.joining(", "));
    }

    //Get Query
    public static String getQueryIncludeCreateUpdated(List<String> columns, String table) {
        includeCreateUpdate(columns, table);
        String resultQuery = String.join(SPACE, Arrays.asList(SELECT, handlerListToCamelCase(columns), FROM, camelCaseToSnakeCase(table)));
        log.info(resultQuery);
        return resultQuery;
    }

    //Get Query with Create Update
    public static List<String> includeCreateUpdate(List<String> columns, String tableName) {
        if (ConstantValue.IGNORE_CLASS_CREATED_UPDATED.stream().noneMatch(t -> t.equalsIgnoreCase(tableName))) {
            columns.addAll(LIST_FIELD_COMMON);
        }
        return columns;
    }

    public static String getCurrentFilePath(BatchRequest request) {
        StringBuilder path = new StringBuilder();
        path.append(ConstantValue.FILE_PATH).append(request.getTypeFile()).append("/")
                .append(request.getTable()).append(".").append(request.getTypeFile());
        log.info(path.toString());
        return path.toString();
    }


}
