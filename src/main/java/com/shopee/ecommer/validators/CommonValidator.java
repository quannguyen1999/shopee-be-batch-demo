package com.shopee.ecommer.validators;

import com.shopee.ecommer.constants.MessageErrors;
import com.shopee.ecommer.exceptions.BadRequestException;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CommonValidator {
    static BiConsumer<Object, MessageErrors> checkEmpty() {
        return (input, messageError) -> {
            if (ObjectUtils.isEmpty(input) || (input instanceof String && !StringUtils.hasLength(input.toString()))
            ) {
                badRequest().accept(messageError);
            }
        };
    }

    static BiConsumer<Boolean, MessageErrors> checkCondition() {
        return (input, messageError) -> {
            if (input) {
                badRequest().accept(messageError);
            }
        };
    }

    static BiConsumer<Object, MessageErrors> checkIsExists() {
        return (input, messageError) -> {
            if (!ObjectUtils.isEmpty(input)
            ) {
                badRequest().accept(messageError);
            }
        };
    }

    static BiConsumer<Object, MessageErrors> checkIsNotExists() {
        return (input, messageError) -> {
            if (ObjectUtils.isEmpty(input)
            ) {
                badRequest().accept(messageError);
            }
        };
    }

    static BiConsumer<Double, MessageErrors> checkDouble() {
        return (input, messageError) -> {
            checkEmpty().accept(input, messageError);
            if (input < 0) {
                badRequest().accept(messageError);
            }
        };
    }

    static BiConsumer<List<String>, MessageErrors> checkList() {
        return (input, messageError) -> {
            if (CollectionUtils.isEmpty(input) || input.size() == 0) {
                badRequest().accept(messageError);
            }
        };
    }

    static Consumer<MessageErrors> badRequest() {
        return messageErrors -> {
            throw new BadRequestException(messageErrors);
        };
    }


}
