package com.shopee.ecommer.constants;


import com.shopee.ecommer.models.entities.Category;
import com.shopee.ecommer.models.entities.CommonBaseEntities;
import com.shopee.ecommer.models.entities.Product;

import java.util.Arrays;
import java.util.List;


public class ConstantValue {
    public static final String JSON = "Json";
    public static final String CSV = "Csv";

    public static final List<String> FILES = Arrays.asList(JSON, CSV);

    //Define Common Field
    public static final String SELECT = "select";

    public static final String SPACE = " ";

    public static final String FROM = "from";

    public static final List<String> LIST_FIELD_COMMON = Arrays.asList(
            CommonBaseEntities.Fields.created,
            CommonBaseEntities.Fields.updated,
            CommonBaseEntities.Fields.userCreated,
            CommonBaseEntities.Fields.userUpdated
    );

    public static final List<String> LIST_FIELD_PRODUCT = Arrays.asList(
            Product.Fields.id
            , Product.Fields.name
            , Product.Fields.image
            , Product.Fields.quantity
            , Product.Fields.price
            , Product.Fields.discount
            , Product.Fields.description);

    public static final List<String> LIST_FIELD_CATEGORY = Arrays.asList(
            Category.Fields.id
            , Category.Fields.name
            , Category.Fields.image
    );

}
