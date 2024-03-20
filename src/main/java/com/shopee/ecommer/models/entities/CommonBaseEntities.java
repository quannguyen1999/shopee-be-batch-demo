package com.shopee.ecommer.models.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldNameConstants;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldNameConstants
public class CommonBaseEntities {

    private Date created;

    private Date updated;

    private String userCreated;

    private String userUpdated;

}
