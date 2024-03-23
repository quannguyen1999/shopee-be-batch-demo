package com.shopee.ecommer.validators;

import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.exceptions.BadRequestException;
import com.shopee.ecommer.models.request.BatchRequest;
import com.shopee.ecommer.utils.BatchUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.shopee.ecommer.constants.MessageErrors.*;

@AllArgsConstructor
@Component
@Slf4j
public class BatchValidator extends CommonValidator {

    public void validateBatch(BatchRequest batchRequest) {
        checkEmpty().accept(batchRequest.getTable(), TABLE_IS_EMPTY);
        checkIsTableExists().accept(batchRequest.getTable(), false);
        checkIsTableExists().accept(batchRequest.getTable(), true);

        checkEmpty().accept(batchRequest.getTypeFile(), FILE_IS_EMPTY);
        checkIsFileExists().accept(batchRequest.getTypeFile());
    }

    public void validateBatchExecuteAllFile(BatchRequest batchRequest) {
        checkEmpty().accept(batchRequest.getTypeFile(), FILE_IS_EMPTY);
        checkIsFileExists().accept(batchRequest.getTypeFile());
    }

    static BiConsumer<String, Boolean> checkIsTableExists() {
        return (input, isCheckUpperCase) -> {
            if (BatchUtils.getListClassesByPackage(ConstantValue.SCAN_PACKAGE_ENTITIES).stream()
                    .map(Class::getSimpleName).noneMatch(value -> !isCheckUpperCase ? value.equalsIgnoreCase(input) : value.equals(input))) {
                throw new BadRequestException(!isCheckUpperCase ? TABLE_NOT_FOUND : TABLE_NOT_UPPERCASE);
            }
        };
    }

    static Consumer<String> checkIsFileExists() {
        return (input) -> {
            if (ConstantValue.FILES.stream().map(String::toLowerCase).noneMatch(value -> value.equalsIgnoreCase(input))) {
                throw new BadRequestException(FILE_NOT_FOUND);
            }
        };
    }
}
