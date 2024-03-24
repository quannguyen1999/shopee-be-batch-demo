package com.shopee.ecommer.utils;

import com.shopee.ecommer.batchs.statements.*;
import com.shopee.ecommer.constants.ConstantValue;
import com.shopee.ecommer.models.entities.*;
import com.shopee.ecommer.models.request.BatchRequest;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.util.ObjectUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BatchUtils {

    public static Function<Boolean, Boolean> isBooleanEmpty() {
        return (input) -> {
            return !ObjectUtils.isEmpty(input) && input;
        };
    }

    private static BiFunction<Class, BatchRequest, Class> compareString() {
        return (inputClass, inputBatch) -> {
            return inputClass.getSimpleName().equalsIgnoreCase(inputBatch.getTable()) ? inputClass : null;
        };
    }

    private static BiFunction<Class, BatchRequest, Boolean> compareClass() {
        return (inputClass, inputBatch) -> {
            return inputClass.getSimpleName().equalsIgnoreCase(inputBatch.getTable());
        };
    }

    private static Function<Class, List<String>> getListFieldByClass() {
        return (inputClass) -> {
            return Arrays.stream(inputClass.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());
        };
    }

    public static List<String> getListFieldByCondition(BatchRequest batchRequest) {
        return getListFieldByClass().apply(getClassByCondition(batchRequest));
    }

    public static ItemPreparedStatementSetter getStatementByCondition(BatchRequest request) {
        if (compareClass().apply(Account.class, request)) {
            return new AccountStatement();
        }

        if (compareClass().apply(AccountRoles.class, request)) {
            return new AccountRolesStatement();
        }

        if (compareClass().apply(Category.class, request)) {
            return new CategoryStatement();
        }

        if (compareClass().apply(Client.class, request)) {
            return new ClientStatement();
        }

        if (compareClass().apply(Product.class, request)) {
            return new ProductStatement();
        }

        if (compareClass().apply(RoleAccount.class, request)) {
            return new RoleAccountStatement();
        }

        return null;
    }

    public static Class getClassByCondition(BatchRequest request) {
        Class result = compareString().apply(Account.class, request);

        if (ObjectUtils.isEmpty(result)) {
            result = compareString().apply(AccountRoles.class, request);
        }

        if (ObjectUtils.isEmpty(result)) {
            result = compareString().apply(Category.class, request);
        }

        if (ObjectUtils.isEmpty(result)) {
            result = compareString().apply(Client.class, request);
        }

        if (ObjectUtils.isEmpty(result)) {
            result = compareString().apply(Product.class, request);
        }

        if (ObjectUtils.isEmpty(result)) {
            result = compareString().apply(RoleAccount.class, request);
        }
        return result;
    }

    public static String getListFieldName(BatchRequest request) {
        return String.join(",", FunctionUtils.includeCreateUpdate(getListFieldByCondition(request), request.getTable()));
    }

    public static String[] getListFieldByArray(BatchRequest request) {
        return FunctionUtils.includeCreateUpdate(getListFieldByCondition(request), request.getTable()).toArray(new String[0]);
    }

    public static List<Class> getListClassesByPackage(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader()
                .getResourceAsStream(packageName.replaceAll("[.]", "/"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        return reader.lines()
                .filter(line -> line.endsWith(".class"))
                .map(line -> getClass(line, packageName))
                .filter(t -> !t.getName().contains("$"))
                .filter(t -> ConstantValue.IGNORE_CLASS_EXPORT.stream()
                        .noneMatch(val -> val.equalsIgnoreCase(t.getSimpleName())))
                .collect(Collectors.toList());

    }

    private static Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
            // handle the exception
        }
        return null;
    }


}
