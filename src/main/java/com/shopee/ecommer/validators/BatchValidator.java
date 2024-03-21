package com.shopee.ecommer.validators;

import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.exceptions.BadRequestException;
import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.models.request.BatchRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.shopee.ecommer.constants.MessageErrors.*;

@AllArgsConstructor
@Component
@Slf4j
public class BatchValidator extends CommonValidator {

    public void validateBatch(BatchRequest batchRequest) {
        checkEmpty().accept(batchRequest.getTable(), TABLE_IS_EMPTY);
        checkIsTableExists().accept(batchRequest.getTable());

        checkEmpty().accept(batchRequest.getTable(), FILE_IS_EMPTY);
        checkIsFileExists().accept(batchRequest.getTypeFile());
    }

    static Consumer<String> checkIsTableExists() {
        ;
        return (input) -> {
            if (Stream.of(
                    Product.class.getSimpleName(),
                    Category.class.getSimpleName()
            ).map(String::toLowerCase).noneMatch(value -> value.equalsIgnoreCase(input))) {
                throw new BadRequestException(TABLE_NOT_FOUND);
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
