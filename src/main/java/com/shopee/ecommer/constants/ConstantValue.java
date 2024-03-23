package com.shopee.ecommer.constants;


import com.shopee.ecommer.models.entities.AccountRoles;
import com.shopee.ecommer.models.entities.Client;
import com.shopee.ecommer.models.entities.CommonBaseEntities;
import com.shopee.ecommer.models.entities.RoleAccount;

import java.util.Arrays;
import java.util.List;


public class ConstantValue {
    public static final String FILE_PATH = "src/main/resources/static/response/";

    public static final String DELIMITER = "|";

    public static final String JSON = "Json";
    public static final String CSV = "Csv";

    public static final List<String> FILES = Arrays.asList(JSON, CSV);

    //Define Common Field
    public static final String SELECT = "select";

    public static final String SPACE = " ";

    public static final String FROM = "from";


    //Package Scan Entities
    public static final String SCAN_PACKAGE_ENTITIES = "com.shopee.ecommer.models.entities";

    public static final List<String> IGNORE_CLASS_CREATED_UPDATED = Arrays.asList(
            AccountRoles.class.getSimpleName(),
            RoleAccount.class.getSimpleName(),
            Client.class.getSimpleName(),
            CommonBaseEntities.class.getSimpleName()
    );

    public static final List<String> IGNORE_CLASS_EXPORT = List.of(
            CommonBaseEntities.class.getSimpleName()
    );


    public static final List<String> LIST_FIELD_COMMON = Arrays.asList(
            CommonBaseEntities.Fields.created,
            CommonBaseEntities.Fields.updated,
            CommonBaseEntities.Fields.userCreated,
            CommonBaseEntities.Fields.userUpdated
    );

}
