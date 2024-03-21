package com.shopee.ecommer.utils;

import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.Product;
import com.shopee.ecommer.models.request.BatchRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

public class BatchUtils {

    public static BiFunction<Class, BatchRequest, Boolean> compareString() {
        return (inputClass, inputBatch) -> {
            return inputClass.getSimpleName().equalsIgnoreCase(inputBatch.getTable());
        };
    }

    public static List<String> getListFieldByCondition(BatchRequest batchRequest) {
        List<String> listResult = new ArrayList<>();
        if (compareString().apply(Product.class, batchRequest)) {
            listResult = ConstantValue.LIST_FIELD_PRODUCT;
        }
        if (compareString().apply(Category.class, batchRequest)) {
            listResult = ConstantValue.LIST_FIELD_CATEGORY;
        }
        return listResult;
    }

    public static Class getClassByCondition(BatchRequest request) {
        if (BatchUtils.compareString().apply(Product.class, request)) {
            return Product.class;
        }
        if (BatchUtils.compareString().apply(Category.class, request)) {
            return Category.class;
        }
        return null;
    }

    public static String getListFieldName(BatchRequest request) {
        return String.join(",", FunctionUtils.includeCreateUpdate(getListFieldByCondition(request)));
    }

    public static String[] getListFieldByArray(BatchRequest request) {
        return FunctionUtils.includeCreateUpdate(getListFieldByCondition(request)).toArray(new String[0]);
    }

}
