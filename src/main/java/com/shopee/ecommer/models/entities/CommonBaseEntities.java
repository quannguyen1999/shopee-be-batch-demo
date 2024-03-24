package com.shopee.ecommer.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class CommonBaseEntities {

    private String created;

    private String updated;

    private String userCreated;

    private String userUpdated;

}
